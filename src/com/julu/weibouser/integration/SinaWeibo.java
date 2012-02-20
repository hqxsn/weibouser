package com.julu.weibouser.integration;

import com.julu.weibouser.config.Configuration;
import weibo4j.Friendships;
import weibo4j.Users;
import weibo4j.Weibo;
import weibo4j.examples.oauth2.Log;
import weibo4j.model.User;
import weibo4j.model.UserWapper;
import weibo4j.model.WeiboException;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/17/12
 * Time: 11:06 AM
 * To change this template use File | Settings | File Templates.
 */
public class SinaWeibo {
    
    private static int UNLIMITED_CALLING = -100000;
    
    private static int LIMITED_CALLING_BOTTOM = -1;
    
    private int callingLimitations = UNLIMITED_CALLING;
    
    public static boolean VALID_GRANT = true;

    public static boolean NO_MORE_GRANT = false;
    
    private final static SinaWeibo instance = new SinaWeibo();

    private ReentrantLock lock = new ReentrantLock();

    public static AtomicBoolean stillWorking = new AtomicBoolean(true);
    
    public final static User USER_NOT_FOUND = null;

        private SinaWeibo() {
            init();
    }

    private void init() {
        /**
         * Right now just assume static configuration, but in the future
         * will leverage with account/rate_limit_status of weibo api specify the calling limits.
         */
         //TODO will change to account/rate_limit_status rather than static
        callingLimitations = getCurrentSettingCallingLimits();
    }

    public static SinaWeibo getInstance() {
        return instance;
    }

    public boolean grantCallingPrivilege() {
        if (callingLimitations == UNLIMITED_CALLING) return VALID_GRANT;

        lock.lock();
        try {
            if (callingLimitations == LIMITED_CALLING_BOTTOM) return NO_MORE_GRANT;

            callingLimitations -= 1;
            if (callingLimitations == LIMITED_CALLING_BOTTOM) return NO_MORE_GRANT;

            return VALID_GRANT;
        } finally {
            lock.unlock();
        }
    }

    class ResetLimitationScheduler implements Runnable {

        Object parkObject = new Object();

        public void run() {

            while (stillWorking.get())
                synchronized (parkObject) {
                    TimeUnit unit = TimeUnit.MILLISECONDS;
                    try {
                        unit.timedWait(parkObject, Long.getLong(Configuration.RESET_LIMITATION_INTERVAL, 3600L));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                lock.lock();
                callingLimitations = getCurrentSettingCallingLimits();
                lock.unlock();
        }
    }
    
    int getCurrentSettingCallingLimits() {
        return Integer.getInteger(Configuration.CURRENT_USER_CALLING_LIMITS, 140);
    }

    long getResetCallingLimitsInterval() {
        return Long.getLong(Configuration.RESET_LIMITATION_INTERVAL, 3600L);
    }
    
    protected String getToken() {
        return System.getProperty(Configuration.WEIBO_TOKEN_STRING);
    }
    
    public User getUser(long uid) throws IntegrationException {
        //Actually we shouldn't always new the weibo instance, but due to project time constraints issue
        //Always new it from beginning, later will reuse instance or pooled it
        if (!grantCallingPrivilege()) throw new IntegrationException("Reach limitation please retrying later", null);

        Weibo weibo = new Weibo();
        weibo.setToken(getToken());
        Users um = new Users();
        try {
            User user = um.showUserById(String.valueOf(uid));
            //Log.logInfo(user.toString());
            return user;
        } catch (WeiboException e) {
            throw new IntegrationException("Fetch user with "+ uid + " from weibo meet exception", e);
        }
    }

    public List<User> getUserFollowers(long uid, int currentCursor, int counts) throws IntegrationException {
        if (!grantCallingPrivilege()) throw new IntegrationException("Reach limitation please retrying later", null);

        Weibo weibo = new Weibo();
        weibo.setToken(getToken());

        Friendships friendships = new Friendships();
        try {
            UserWapper userWapper = friendships.getFollowersById(String.valueOf(uid), counts, currentCursor);
            List<User> users = userWapper.getUsers();

            return users;
        } catch (WeiboException e) {
            throw new IntegrationException("Fetch user's followers with "+ uid +
                    " from " + currentCursor + " with # " + counts +  " from weibo meet exception", e);
        }
    }

}

