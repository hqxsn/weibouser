package com.julu.weibouser.config;

import com.julu.weibouser.logger.ConsoleLogger;
import com.julu.weibouser.system.SystemHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/17/12
 * Time: 10:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class PushToRuntimeProperties {
    
    private static ConsoleLogger consoleLogger = new ConsoleLogger(PushToRuntimeProperties.class.getName());
    

    public static void setupRuntimProperty(InputStream inStream, String configPath){
        if(inStream == null){
            SystemHelper.exit("Cannot setup the runtime properties, the server cannot start successfully, please check the runtime property setting in the Bootstrap", null, false);
        }

        Properties props = new Properties();

        try {
            props.load(inStream);
            Set<Map.Entry<Object,Object>> propEntry = props.entrySet();

            if(configPath == null || configPath.isEmpty()){
                configPath = props.getProperty("config.path");
            }

            for (Map.Entry<Object, Object> entry : propEntry) {
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();

                if(value.startsWith("${config.path}")){
                    value = value.replace("${config.path}", configPath);
                }

                consoleLogger.logInfo(key + "=" + value);
                System.setProperty(key, value);
            }

            System.setProperty("root.path", configPath);

        } catch (IOException e) {
            consoleLogger
                    .logError("IO exception during the propertyFilePath init stop the server now!"
                            + e.getMessage());
            SystemHelper.exit("Cannot setup the runtime properties, the server cannot start successfully, please check the runtime property setting in the Bootstrap", e, false);
        }

        consoleLogger.logInfo("End setup the runtime.properties");
    }

    public static void setupRuntimeProperty(String propertyFilePath) {
        consoleLogger.logInfo("Start setup the runtime.properties");

        File propertyFile = new File(propertyFilePath);
        if (!propertyFile.exists()) {
            consoleLogger
                    .logError("Cannot find the property according the propertyFilePath "
                            + propertyFilePath);
            SystemHelper.exit("Cannot setup the runtime properties, the server cannot start successfully, please check the runtime property setting in the Bootstrap", null, false);
        }

        File directory = propertyFile.getParentFile();

        String directoryPath = null;

        if(directory.isDirectory()){
            directoryPath = directory.getAbsolutePath();
        }

        FileInputStream inStream = null;

        try {
            inStream = new FileInputStream(propertyFile);
            setupRuntimProperty(inStream, directoryPath);
        } catch (FileNotFoundException e) {
            consoleLogger
                    .logError("Cannot find the property according the propertyFilePath "
                            + propertyFilePath
                            + " stop the server now!"
                            + e.getMessage());
            SystemHelper.exit("Cannot setup the runtime properties, the server cannot start successfully, please check the runtime property setting in the Bootstrap", e, false);
        } finally {
            try {
                inStream.close();
            } catch (IOException e) {
                consoleLogger
                        .logError("IO exception during the input stream close stop the server now!"
                                + e.getMessage());
            }
        }
        consoleLogger.logInfo("End setup the runtime.properties");
    }
    
}
