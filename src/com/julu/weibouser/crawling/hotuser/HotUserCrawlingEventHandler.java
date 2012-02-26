package com.julu.weibouser.crawling.hotuser;

import com.julu.weibouser.crawling.CrawlingSystem;
import com.julu.weibouser.crawling.userfollowers.UserFollowersCrawlingEvent;
import com.julu.weibouser.crawling.userfollowers.UserFollowersCrawlingEventFactory;
import com.julu.weibouser.crawling.userfollowers.UserFollowersCrawlingEventQueue;
import com.julu.weibouser.eventprocessing.EventSystem;
import com.julu.weibouser.eventprocessing.event.EventType;
import com.julu.weibouser.eventprocessing.exception.EventProcessingException;
import com.julu.weibouser.eventprocessing.handler.IHandler;
import com.julu.weibouser.eventprocessing.operator.StandalonePoller;
import com.julu.weibouser.eventprocessing.operator.StandalonePusher;
import com.julu.weibouser.integration.Integration;
import com.julu.weibouser.integration.IntegrationException;
import com.julu.weibouser.integration.SinaWeibo;
import com.julu.weibouser.logger.ConsoleLogger;
import com.julu.weibouser.model.IdService;
import com.julu.weibouser.processing.ProcessingSystem;
import weibo4j.Weibo;
import weibo4j.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/26/12
 * Time: 12:03 AM
 * To change this template use File | Settings | File Templates.
 */
public class HotUserCrawlingEventHandler implements IHandler<HotUserCrawlingEvent>, Runnable {

    private static ConsoleLogger consoleLogger = new ConsoleLogger(HotUserCrawlingEventHandler.class.getName());

    private StandalonePoller<HotUserCrawlingEvent, HotUserCrawlingEventQueue> poller;

    public HotUserCrawlingEventHandler(StandalonePoller<HotUserCrawlingEvent, HotUserCrawlingEventQueue> poller) {
        this.poller = poller;
    }

    public void handle(HotUserCrawlingEvent event) {
        //long timeMilli = System.currentTimeMillis();
        try {
            List<User> users = SinaWeibo.getInstance().getHotUsers();

            List<com.julu.weibouser.model.User> juluUsers = new ArrayList<com.julu.weibouser.model.User>();
            for (User user : users) {
                com.julu.weibouser.model.User juluUser = com.julu.weibouser.model.User.compose(user, event.getCrawlingTarget());
                if (!Integration.hasProcessed(event.getCrawlingTarget(), juluUser.getOriginalSourceUid())) {
                    juluUser.setUid(IdService.getUniqueId());
                }
                juluUsers.add(juluUser);
            }

            for (com.julu.weibouser.model.User juluUser : juluUsers) {
                List<UserFollowersCrawlingEvent> subsequentEvents = UserFollowersCrawlingEventFactory.
                        create(juluUser.getOriginalSourceUid(), juluUser.getFollowersCount());

                StandalonePusher<UserFollowersCrawlingEvent, UserFollowersCrawlingEventQueue> pusher = EventSystem.getPusher(EventType.FIND_USER_FOLLOWERS);
                for (UserFollowersCrawlingEvent userFollowersCrawlingEvent : subsequentEvents) {
                    try {
                        pusher.push(userFollowersCrawlingEvent);
                    } catch (EventProcessingException e) {
                        consoleLogger.logError("Cannot push user followers finding event into queue " + userFollowersCrawlingEvent, e);
                    }
                }
            }

                try {
                    ProcessingSystem.getInstance().push(juluUsers);
                } catch (IOException e) {
                    consoleLogger.logError("Cannot serialization user instance " + juluUsers, e);
                } catch (EventProcessingException e) {
                    //TODO here will involve retrying process;
                    consoleLogger.logError("Cannot push user processing event into queue " + juluUsers, e);
                }

        } catch (IntegrationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        
        //System.out.println("HotUserCrawlingEventHandler.handle() need:" + (System.currentTimeMillis()-timeMilli));

    }

    public void run() {
        while (CrawlingSystem.getInstance().isRunning()) {
            try {
                HotUserCrawlingEvent event = poller.consume(500l, TimeUnit.MILLISECONDS);
                if (event != null) {
                    handle(event);
                }
            } catch (EventProcessingException e) {
                consoleLogger.logError("Not successfully get event from event queue:" + EventType.FIND_USER_BY_SPECIFIED_UID, e);
            }
        }
    }
}
