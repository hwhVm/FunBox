package com.beini.db.io;

import android.os.Environment;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by beini on 2017/3/30.
 */

public class FileUtil {

    public static void writeBytesToSD(String fileDir, byte[] data) {
        try {
            RandomAccessFile fos = new RandomAccessFile(fileDir, "rw");
            FileChannel fileChannel = fos.getChannel();
            MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, data.length);
            buffer.put(data);
            fileChannel.close();
            fos.close();
        } catch (IOException e) {
            System.out.println(" " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}
