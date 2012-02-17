package com.julu.weibouser.crawling;


import com.julu.weibouser.config.Configuration;
import com.julu.weibouser.crawling.user.SingleUserCrawlingEvent;
import com.julu.weibouser.crawling.user.SingleUserCrawlingEventHandler;
import com.julu.weibouser.crawling.user.SingleUserCrawlingEventQueue;
import com.julu.weibouser.crawling.userfollowers.UserFollowersCrawlingEvent;
import com.julu.weibouser.crawling.userfollowers.UserFollowersCrawlingEventHandler;
import com.julu.weibouser.crawling.userfollowers.UserFollowersCrawlingEventQueue;
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
        UserFollowersCrawlingEventQueue userFollowersCrawlingEventQueue = (UserFollowersCrawlingEventQueue) EventType.FIND_USER_FOLLOWERS.getEventQueue();
        UserFollowersCrawlingEventHandler userFollowersCrawlingEventHandler = new UserFollowersCrawlingEventHandler(
                new StandalonePoller<UserFollowersCrawlingEvent, UserFollowersCrawlingEventQueue>(userFollowersCrawlingEventQueue));
        new Thread(userFollowersCrawlingEventHandler).start();
    }

    private void initSingleUserCrawling() {
        SingleUserCrawlingEventQueue singleUserCrawlingEventQueue = (SingleUserCrawlingEventQueue) EventType.FIND_USER_BY_SPECIFIED_UID.getEventQueue();
        SingleUserCrawlingEventHandler singleUserCrawlingEventHandler = new SingleUserCrawlingEventHandler(
                new StandalonePoller<SingleUserCrawlingEvent, SingleUserCrawlingEventQueue>(singleUserCrawlingEventQueue));
        new Thread(singleUserCrawlingEventHandler).start();
    }
    
    public void simpleAnalysis() {
        
        SingleUserCrawlingEvent event = new SingleUserCrawlingEvent();
        event.setCrawlingTarget(Integration.getSinaWeiboType());
        event.setOriginalSourceUid(Long.getLong(Configuration.SPECIFIED_USER_ID, 1709498127l /*孙楠 UID*/));
        
        StandalonePusher<SingleUserCrawlingEvent, SingleUserCrawlingEventQueue> pusher = new 
                StandalonePusher<SingleUserCrawlingEvent, SingleUserCrawlingEventQueue>((SingleUserCrawlingEventQueue) 
                EventType.FIND_USER_BY_SPECIFIED_UID.getEventQueue());

        try {
            pusher.push(event);
        } catch (EventProcessingException e) {
            //TODO add logic
        }
    }

}
