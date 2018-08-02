package com.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;

public class HelloWorldMsgServer {

    private static final int TIMEOUT = 3000;

    private String host;
    private int port;

    public static void main(String[] args) {
        new HelloWorldMsgServer("localhost", 8080).start();
    }


    public HelloWorldMsgServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() {
        Selector selector = null;
        ServerSocketChannel serverSocketChannel = null;
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(host, port));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("listening port " + port + " in " + host);

            while (true) {
                if (selector.select(TIMEOUT) == 0) {
                    System.out.print("=");
                    continue;
                }

                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isAcceptable()) {
                        System.out.println("handler accept");
                        handlerAcceptable(key);
                    }
                    if (key.isReadable()) {
                        System.out.println("handler read");
                        handlerReadable(key);
                    }
                    if (key.isWritable()) {
                        System.out.println("handler write");
                        handlerWriteable(key);
                    }
                    //切记要remove
                    iterator.remove();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void handlerWriteable(SelectionKey key) throws IOException {
        SocketChannel writeSocketChannel = (SocketChannel) key.channel();
        ByteBuffer writeBuff = (ByteBuffer) key.attachment();
//        writeSocketChannel.read(writeBuff);
        //ByteBuffer writeBuff = (ByteBuffer) key.attachment();
        writeBuff.flip();
        while (writeBuff.hasRemaining()) {
            writeSocketChannel.write(writeBuff);
        }
        writeBuff.clear();
        writeSocketChannel.register(key.selector(), SelectionKey.OP_READ);
    }

    private void handlerReadable(SelectionKey key) throws IOException {
        //获取读取客户端数据的通道
        SocketChannel socketChannel = (SocketChannel) key.channel();
        //构造一个1024长度的缓冲区，来接收从SocketChannel里获取的数据
//        ByteBuffer inByteBuff = (ByteBuffer) key.attachment();
        ByteBuffer inByteBuff = ByteBuffer.allocate(1024);
        ByteBuffer outByteBuff = ByteBuffer.allocate(1024);
        int readSize = socketChannel.read(inByteBuff);
        //循环读取channel通道里的数据
        while (readSize > 0) {
            //将byteBuffer从写模式转换为读模式
            inByteBuff.flip();
            byte[] temp = new byte[readSize];
            inByteBuff.get(temp);
            System.out.println(new String(temp));

            inByteBuff.clear();
            readSize = socketChannel.read(inByteBuff);

            String response = "recv time:" + new Date();
            outByteBuff.put(response.getBytes());
            socketChannel.register(key.selector(), SelectionKey.OP_WRITE, outByteBuff);

        }
//        if (readSize == 0) {
//            socketChannel.close();
//            //socketChannel.write(outByteBuff.put(response.getBytes()));
//        }
    }

    private void handlerAcceptable(SelectionKey key) throws IOException {
        //拿到channel,并强转为ServerSocketChannel
        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
        //接受请求，拿到一个socketChannel，来授受客户端的数据。
        SocketChannel sc = ssc.accept();
        //配置为非阻塞
        sc.configureBlocking(false);
        System.out.println("handler client : " + sc.socket().getInetAddress());
        //为socketChannel注册读数据监听。
        sc.register(key.selector(), SelectionKey.OP_READ);

        /*ServerSocketChannel ssChannel = (ServerSocketChannel) key.channel();
        SocketChannel sc = ssChannel.accept();
        sc.configureBlocking(false);
        System.out.println("handler client : " + sc.socket().getInetAddress());
        sc.register(key.selector(), SelectionKey.OP_READ, ByteBuffer.allocateDirect(1024));*/
    }


}
