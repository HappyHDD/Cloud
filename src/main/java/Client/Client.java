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









    }
}
