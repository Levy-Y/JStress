package io.levysworks.Utils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

public class FileGenerator {
    public static File generateBodyFile(int size) {
        UUID uuid = UUID.randomUUID();
        File file = new File(uuid.toString());

        try {
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            raf.setLength(size);
            raf.close();

            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
