package Client;

import Utils.FileMessage;
import Utils.FileRequest;
import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class Client {
    public static void main(String[] args) throws Exception {

        CountDownLatch cdl = new CountDownLatch(1);
        Socket socket = new Socket("localhost", 8188);
        ObjectEncoderOutputStream out = new ObjectEncoderOutputStream(socket.getOutputStream());
        ObjectDecoderInputStream in = new ObjectDecoderInputStream(socket.getInputStream(), 100 * 1024 * 1024);

        String root = "Client_storage";
        String filename = "test.txt";
        out.writeObject(new FileMessage(filename,0,0, Files.readAllBytes(Path.of(root + "/" + filename))));
        out.flush();

        out.writeObject(new FileRequest(filename));
        out.flush();
            new Thread(() -> {
                try {
                    while (true) {
                        Object input = in.readObject();
                        FileMessage fm = (FileMessage) input;
                        fm.filename = "Client_storage/" + filename + 1;
                        boolean append = true;
                        if (fm.partNumber == 1) {
                            append = false;
                        }
                        System.out.println(fm.partNumber + " / " + fm.partsCount);
                        FileOutputStream fos = new FileOutputStream(fm.filename, append);
                        fos.write(fm.data);
                        fos.close();
                        if (fm.partNumber == fm.partsCount) {
                            cdl.countDown();
                        }
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
            cdl.await();
            System.out.println("Фаил скачен");
            in.close();
            out.close();
            socket.close();

    }
}
