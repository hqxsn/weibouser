package com.julu.weibouser.processing.persistence;

import com.julu.weibouser.config.Configuration;
import com.julu.weibouser.model.User;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.UUID;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/20/12
 * Time: 4:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class FilePersistor {

    private static String directory;
    static {
        directory = System.getProperty(Configuration.PROCESSING_FILES_DIRECTORY) + 
                System.getProperty("file.separator") + System.getProperty(Configuration.PERSIST_FILES_DIRECTORY);    
    }
    
    //Persist and return file name
    public String persist(byte[] bytes) {
        UUID uuid = UUID.randomUUID();
        
        String filePath = getConfiguredDirectoryPath() + System.getProperty("file.separator") + uuid.toString();


        if (!createFile(filePath)) return null;

        RandomAccessFile aFile = null;
        FileChannel channel = null;
        try {
            
            aFile = new RandomAccessFile(filePath, "rw");
            channel = aFile.getChannel();

            ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
            while(byteBuffer.hasRemaining()) {
                channel.write(byteBuffer);
            }
            channel.force(true);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            filePath = null;
        } catch (IOException e) {
            e.printStackTrace();
            filePath = null;
        } finally {

            if (channel != null) {
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        return filePath;
    }

    private static String getConfiguredDirectoryPath() {
        return directory;
    }
    
    private boolean createFile(String filePath) {
        
        File file = new File(filePath);
        if (!file.exists()) try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

}
