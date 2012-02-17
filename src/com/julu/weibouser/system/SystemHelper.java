package com.julu.weibouser.system;

import com.julu.weibouser.logger.ConsoleLogger;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/17/12
 * Time: 10:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class SystemHelper {

    private static ConsoleLogger consoleLogger = new ConsoleLogger(SystemHelper.class.getName());

    public static void exit(String systemInfo, Exception exception, boolean needNotify /*If need could sent out escalation email*/) {
        consoleLogger.logError(systemInfo, exception);
        //Quit the application
        System.exit(0);

    }
}
