package com.julu.weibouser.system;

import java.util.concurrent.TimeUnit;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/25/12
 * Time: 12:44 AM
 * To change this template use File | Settings | File Templates.
 */
public class ExecutionStats {

    private static long millis;
    private static long executedTime;

    public static void setStart() {
        millis = System.currentTimeMillis();
    }

    public static long executionTime() {
        executedTime = System.currentTimeMillis() - millis;
        return executedTime;
    }

    public static long getExecutedTime() {
        return executedTime;
    }



}


