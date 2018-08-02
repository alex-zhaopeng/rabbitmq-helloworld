package com.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class AnotherNioClient {

    public static void main(String[] args) {
        try {
            SocketChannel sc = SocketChannel.open();
            sc.configureBlocking(false);
            Selector selector = Selector.open();
            sc.connect(new InetSocketAddress("localhost", 8080));
            sc.register(selector, SelectionKey.OP_CONNECT);

            while (true) {
                selector.select();
                Iterator iter = selector.selectedKeys().iterator();
                while (iter.hasNext()) {
                    SelectionKey key = (SelectionKey) iter.next();
                    iter.remove();
                    if (key.isConnectable()) {
                        handleConnect(key);
                    }
                    if (key.isWritable()) {
                        handleWrite(key);
                    }
                    if (key.isReadable()) {
                        handleRead(key);
                    }
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void handleRead(SelectionKey key) throws IOException {
        SocketChannel out = (SocketChannel) key.channel();
        ByteBuffer outByteBuffer = ByteBuffer.allocate(1024);
        int readSize = out.read(outByteBuffer);
        while (readSize > 0){
            outByteBuffer.flip();
            byte[] b = new byte[readSize];
            outByteBuffer.get(b);
            System.out.println(new String(b));
            outByteBuffer.clear();
            readSize = out.read(outByteBuffer);
        }
    }

    private static void handleWrite(SelectionKey key) throws IOException {
        SocketChannel in = (SocketChannel) key.channel();
        ByteBuffer inByteBuffer = ByteBuffer.allocate(1024);
        int i = 0;
        while(true){
            String info = "I'm "+ i++ +"-th information from client.";
            inByteBuffer.put(info.getBytes());
            //TODO 如何解决发送的问题
            inByteBuffer.flip();
            in.write(inByteBuffer);
            inByteBuffer.clear();
            in.register(key.selector(), SelectionKey.OP_READ);
        }

    }

    private static void handleConnect(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        if (socketChannel.finishConnect()) {
            socketChannel.configureBlocking(false);
            socketChannel.register(key.selector(), SelectionKey.OP_WRITE);
        }
    }
}
