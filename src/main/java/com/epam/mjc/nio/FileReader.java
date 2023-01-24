package com.epam.mjc.nio;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;


public class FileReader {

    public Profile getDataFromFile(File file) {
        Profile profile = null;
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file.getAbsoluteFile(), "r");
             FileChannel fileChannel = randomAccessFile.getChannel()) {
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) randomAccessFile.length());
            StringBuilder stringBuilder = new StringBuilder();
            while (fileChannel.read(byteBuffer) > 0) {
                byteBuffer.flip();
                for (int i = 0; i < byteBuffer.limit(); i++) {
                    stringBuilder.append((char) byteBuffer.get());
                }
                byteBuffer.clear();
            }
            System.out.println(stringBuilder);
            List<String> list = List.of(stringBuilder.toString().split("\r\n"));
            String name = "";
            int age = 0;
            String email = "";
            long phone = 0L;
            for (String s : list) {
                int delimiter = s.indexOf(":");
                if (s.contains("Name")) {
                    name = s.substring(delimiter + 2);
                } else if (s.contains("Age")) {
                    age = Integer.parseInt(s.substring(delimiter + 2));
                } else if (s.contains("Email")) {
                    email = s.substring(delimiter + 2);
                } else {
                    phone = Long.parseLong(s.substring(delimiter + 2));
                }
            }
            profile = new Profile(name, age, email, phone);
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return profile;
    }
}