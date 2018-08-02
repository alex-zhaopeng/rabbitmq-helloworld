package com.nio;



import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

public class NioClient {

    public static void main(String[] args) {

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        ByteBuffer response = ByteBuffer.allocate(1024);
        SocketChannel channel = null;

        try {
            channel = SocketChannel.open();
            channel.configureBlocking(false);
            channel.connect(new InetSocketAddress("localhost", 8080));
            //windows系统在blocking为false的情况下，如果连接一个不存在的端口，finishConnect判断会给false
            //但是不会catch connect refused exception,只能通过finishConnect的结果判断
            //linux或者mac os下的话，在finishConnect的时候就会抛出connect refused exception.
            System.out.println(channel.finishConnect());

            if (channel.finishConnect()) {
                int i = 0;
                while (true) {

                    String info = "I'm " + i++ + "-th information from client.";
                    byteBuffer.clear();
                    byteBuffer.put(info.getBytes());
                    //
                    byteBuffer.flip();
                    while (byteBuffer.hasRemaining()) {
                        //System.out.println(byteBuffer.toString());
                        channel.write(byteBuffer);
                    }
                    TimeUnit.SECONDS.sleep(2);
                    response.clear();
                    int readSize = channel.read(response);
                    response.flip();
                    while (readSize > 0) {
                        byte[] recvMsg = new byte[readSize];
                        response.get(recvMsg);
                        System.out.println(new String(recvMsg));
                        response.clear();
                        readSize = channel.read(response);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("1这是我打出来的！！！");
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                if (channel != null) {
                    channel.close();
                }
            } catch (IOException e) {
                System.out.println("2这是我打出来的！！！");
                e.printStackTrace();
            }
        }

    }

}
