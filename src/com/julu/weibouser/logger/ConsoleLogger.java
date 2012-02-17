package com.julu.weibouser.logger;

import com.julu.weibouser.util.Utils;

import java.util.Date;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/17/12
 * Time: 10:27 AM
 * To change this template use File | Settings | File Templates.
 */
public class ConsoleLogger {
    private String moduleName;

    public ConsoleLogger(String moduleName) {
        this.moduleName = moduleName;
    }

    public void logInfo(String infoMsg) {
        System.out.println("[INFO] " + generatePrefix() + infoMsg);
    }

    public void logWarn(String infoMsg) {
        System.out.println("[WARN] " + generatePrefix() + infoMsg);
    }

    public void logError(String infoMsg) {
        System.out.println("[ERROR] " + generatePrefix() + infoMsg);
    }

    public void logError(String errorMsg, Throwable t) {
        System.out.println("[ERROR] " + generatePrefix() + errorMsg);

        if (t != null) {
            System.out.println("[ERROR] StackTrace: "
                    + Utils.outputStackTrace(t));
        }
    }

    protected String generatePrefix() {
        return String.format("[%s]  [%s] %s --", new Date(), Thread.currentThread().getName(), moduleName);
    }

}

