package com.julu.weibouser.crawling.user;

import com.julu.weibouser.eventprocessing.event.Event;
import com.julu.weibouser.eventprocessing.event.EventType;
import com.julu.weibouser.eventprocessing.exception.EventProcessingException;
import com.julu.weibouser.eventprocessing.handler.IHandler;
import com.julu.weibouser.eventprocessing.operator.StandalonePoller;
import com.julu.weibouser.eventprocessing.queue.EventQueue;
import com.julu.weibouser.integration.Integration;
import com.julu.weibouser.integration.IntegrationException;
import com.julu.weibouser.integration.SinaWeibo;
import com.julu.weibouser.logger.ConsoleLogger;

import java.util.concurrent.TimeUnit;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/17/12
 * Time: 2:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class SingleUserCrawlingEventHandler implements IHandler<SingleUserCrawlingEvent>, Runnable  {
    
    private static ConsoleLogger consoleLogger = new ConsoleLogger(SingleUserCrawlingEventHandler.class.getName());
    
    private StandalonePoller<SingleUserCrawlingEvent, SingUserCrawlingEventQueue> poller;
    
    public SingleUserCrawlingEventHandler(StandalonePoller<SingleUserCrawlingEvent, SingUserCrawlingEventQueue> poller) {
        this.poller = poller;    
    }
    
    public void handle(SingleUserCrawlingEvent event) {
        long uid = event.getOriginalSourceUid();

        if (uid > 0) {
            try {
                weibo4j.model.User user = SinaWeibo.getInstance().getUser(uid);
                
                if (user != SinaWeibo.USER_NOT_FOUND) {
                    com.julu.weibouser.model.User juluUser = com.julu.weibouser.model.User.compose(user, event.getCrawlingTarget());


                } else {
                    //TODO here will involve some company policy, to be implement later
                }
                
            } catch (IntegrationException e) {
                //TODO here will involve retrying process, to be implement later
            }
        }
    }

    public void run() {
        while(true) {
            try {
                SingleUserCrawlingEvent event = poller.consume(500l, TimeUnit.MILLISECONDS);
                if (event != null) {
                    handle(event);
                }
            } catch (EventProcessingException e) {
                consoleLogger.logError("Not successfully get event from event queue:" + EventType.FIND_USER_BY_SPECIFIED_UID, e);
            }
        }
    }
}
