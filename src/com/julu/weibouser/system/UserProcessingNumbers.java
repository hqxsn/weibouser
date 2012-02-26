package com.julu.weibouser.system;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/25/12
 * Time: 12:40 AM
 * To change this template use File | Settings | File Templates.
 */
public class UserProcessingNumbers {
    
    private static AtomicLong atomicLong = new AtomicLong(0);

    public static void addUser() {
        atomicLong.incrementAndGet();
    }

    public static long addUser(int numbers) {
        return atomicLong.addAndGet(numbers);
    }

    public static long processedNumber() {
        return atomicLong.get();
    }
}
