package Server;

import Utils.FileMessage;
import Utils.FileRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;

public class MainHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (msg instanceof FileRequest) {
                FileRequest FR = (FileRequest) msg;
                File file = new File("Server_storage/" + FR.getName());
                int bufSize = 1024 * 1024 * 10;
                int partsCount = Long.valueOf(file.length() / bufSize).intValue();
                if (file.length() % bufSize != 0) {
                    partsCount++;
                    FileMessage fmOut = new FileMessage(FR.getName(), -1, partsCount, new byte[bufSize]);
                    FileInputStream in = new FileInputStream(file);
                    for (int i = 0; i < partsCount; i++) {
                        int readedBytes = in.read(fmOut.data);
                        fmOut.partNumber = i + 1;
                        if (readedBytes < bufSize) {
                            fmOut.data = Arrays.copyOfRange(fmOut.data, 0, readedBytes);
                        }
                        ctx.writeAndFlush(fmOut);
                        System.out.println("Отправлена часть #" + (i + 1));
                    }
                    in.close();
                }
            }else{
                FileMessage fm = (FileMessage) msg;
                FileOutputStream fous = new FileOutputStream("Server_storage/"+ fm.filename);
                fous.write(fm.data);
                fous.close();

            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
