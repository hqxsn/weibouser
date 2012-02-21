package com.julu.weibouser.crawling;


import com.julu.weibouser.config.Configuration;
import com.julu.weibouser.crawling.user.SingleUserCrawlingEvent;
import com.julu.weibouser.crawling.user.SingleUserCrawlingEventHandler;
import com.julu.weibouser.crawling.user.SingleUserCrawlingEventQueue;
import com.julu.weibouser.crawling.userfollowers.UserFollowersCrawlingEvent;
import com.julu.weibouser.crawling.userfollowers.UserFollowersCrawlingEventHandler;
import com.julu.weibouser.crawling.userfollowers.UserFollowersCrawlingEventQueue;
import com.julu.weibouser.eventprocessing.EventSystem;
import com.julu.weibouser.eventprocessing.event.EventType;
import com.julu.weibouser.eventprocessing.exception.EventProcessingException;
import com.julu.weibouser.eventprocessing.operator.StandalonePoller;
import com.julu.weibouser.eventprocessing.operator.StandalonePusher;
import com.julu.weibouser.integration.Integration;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/17/12
 * Time: 3:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class CrawlingSystem {

    private volatile boolean running = true;

    private static CrawlingSystem crawlingSystem = new CrawlingSystem();

    private CrawlingSystem() {
        init();
    }

    public static CrawlingSystem getInstance() {
        return crawlingSystem;
    }

    public void init() {
        initSingleUserCrawling();
        initUserFollowerCrawling();
    }

    public boolean isRunning() {
        return running;
    }

    public void stopRunning() {
        running = false;
    }

    private void initUserFollowerCrawling() {
        StandalonePoller<UserFollowersCrawlingEvent, UserFollowersCrawlingEventQueue> poller = EventSystem.getPoller(EventType.FIND_USER_FOLLOWERS);
        UserFollowersCrawlingEventHandler userFollowersCrawlingEventHandler = new UserFollowersCrawlingEventHandler(
                poller);
        new Thread(userFollowersCrawlingEventHandler).start();
    }

    private void initSingleUserCrawling() {


        StandalonePoller<SingleUserCrawlingEvent, SingleUserCrawlingEventQueue> poller = EventSystem.getPoller(EventType.FIND_USER_BY_SPECIFIED_UID);
        SingleUserCrawlingEventHandler singleUserCrawlingEventHandler = new SingleUserCrawlingEventHandler(
                poller);
        new Thread(singleUserCrawlingEventHandler).start();
    }
    
    public void simpleAnalysis() {
        
        SingleUserCrawlingEvent event = new SingleUserCrawlingEvent();
        event.setCrawlingTarget(Integration.getSinaWeiboType());
        event.setOriginalSourceUid(Long.getLong(Configuration.SPECIFIED_USER_ID, 1709498127l /*孙楠 UID*/));
        
        StandalonePusher<SingleUserCrawlingEvent, SingleUserCrawlingEventQueue> pusher =
                EventSystem.getPusher(EventType.FIND_USER_BY_SPECIFIED_UID);

        try {
            pusher.push(event);
        } catch (EventProcessingException e) {
            //TODO add logic
        }
    }
    
    public boolean needDeeperAnalysis() {
        return Boolean.getBoolean(Configuration.NEED_DEEP_ANALYSIS);
    }
    
    public static void main(String[] args) {
        System.setProperty(Configuration.WEIBO_TOKEN_STRING, "2.00tyhuHC09EMdOe80ef5d168vfOZbD");
        System.setProperty(Configuration.PROCESSING_FILES_DIRECTORY, "d:\\log");
        System.setProperty(Configuration.PERSIST_FILES_DIRECTORY, "files");
        com.julu.weibouser.processing.ProcessingSystem.getInstance();
        CrawlingSystem.getInstance().simpleAnalysis();
    }

}
