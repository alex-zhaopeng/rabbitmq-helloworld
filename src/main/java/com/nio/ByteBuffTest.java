package com.nio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ByteBuffTest {

    public static void main(String[] args) {
        ByteBuffer buff = ByteBuffer.allocate(10240);
        System.out.println("================= before read =================");
        System.out.println(buff.capacity());
        System.out.println(buff.position());
        System.out.println(buff.limit());

        RandomAccessFile aFile = null;
        try {
            aFile = new RandomAccessFile("src/main/resources/logback.xml", "rw");
            FileChannel fileChannel = aFile.getChannel();
            fileChannel.read(buff);
            System.out.println("================= after read =================");
            System.out.println(buff.capacity());
            System.out.println(buff.position());
            System.out.println(buff.limit());
            System.out.println(buff.mark());
            buff.flip();
            System.out.println("================= after flip =================");
            System.out.println(buff.capacity());
            System.out.println(buff.position());
            System.out.println(buff.limit());
            System.out.println(buff.get(10));
            buff.mark();
            buff.get(10);
            System.out.println(buff.position());
            buff.reset();
            System.out.println(buff.position());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }


    }
}
