package com.nio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileTest {


    public static void main(String[] args) {
        String pathname = "/Users/zhaopeng/noaa/ftp.ncdc.noaa.gov/pub/data/gsod/temp-all-input.txt";
        try {
            RandomAccessFile in_file = new RandomAccessFile(new File(pathname), "rw");
            RandomAccessFile out_file = new RandomAccessFile(new File("/Users/zhaopeng/Desktop/temp-all-input.txt"), "rw");
            FileChannel inChannel = in_file.getChannel();
            FileChannel outChannel = out_file.getChannel();

            ByteBuffer buffer = ByteBuffer.allocate(4096);
            long start = System.currentTimeMillis();
            while (true) {
                buffer.clear();
                int temp = inChannel.read(buffer);
                if (temp == -1) {
                    break;
                }
                buffer.flip();
                outChannel.write(buffer);
            }
            long end = System.currentTimeMillis();
            System.out.println("总共发费：" + (end - start) + "ms");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }
}
