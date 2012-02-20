package com.julu.weibouser.crawling.user;

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
import com.julu.weibouser.integration.IntegrationException;
import com.julu.weibouser.integration.SinaWeibo;
import com.julu.weibouser.logger.ConsoleLogger;
import com.julu.weibouser.processing.ProcessingSystem;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/17/12
 * Time: 2:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class SingleUserCrawlingEventHandler implements IHandler<SingleUserCrawlingEvent>, Runnable {

    private static ConsoleLogger consoleLogger = new ConsoleLogger(SingleUserCrawlingEventHandler.class.getName());

    private StandalonePoller<SingleUserCrawlingEvent, SingleUserCrawlingEventQueue> poller;

    public SingleUserCrawlingEventHandler(StandalonePoller<SingleUserCrawlingEvent, SingleUserCrawlingEventQueue> poller) {
        this.poller = poller;
    }

    public void handle(SingleUserCrawlingEvent event) {
        long uid = event.getOriginalSourceUid();

        if (uid > 0) {
            try {
                weibo4j.model.User user = SinaWeibo.getInstance().getUser(uid);

                if (user != SinaWeibo.USER_NOT_FOUND) {
                    com.julu.weibouser.model.User juluUser = com.julu.weibouser.model.User.compose(user, event.getCrawlingTarget());
                    //TODO push into persist queue

                    List<UserFollowersCrawlingEvent> subsequentEvents = UserFollowersCrawlingEventFactory.
                            create(juluUser.getOriginalSourceUid(), juluUser.getFollowersCount());

                    StandalonePusher<UserFollowersCrawlingEvent, UserFollowersCrawlingEventQueue> pusher = EventSystem.getPusher(EventType.FIND_USER_FOLLOWERS);
                    for (UserFollowersCrawlingEvent userFollowersCrawlingEvent : subsequentEvents) {
                        try {
                            pusher.push(userFollowersCrawlingEvent);
                        } catch (EventProcessingException e) {
                            //TODO here will involve retrying process, to be implement later
                            consoleLogger.logError("Cannot push user followers finding event into queue " + userFollowersCrawlingEvent, e);
                        }
                    }

                    try {
                        ProcessingSystem.getInstance().push(Arrays.asList(juluUser));
                    } catch (IOException e) {
                        //TODO here will involve retrying process;
                        consoleLogger.logError("Cannot serialization user instance " + juluUser, e);
                    } catch (EventProcessingException e) {
                        //TODO here will involve retrying process;
                        consoleLogger.logError("Cannot push user processing event into queue " + juluUser, e);
                    }

                } else {
                    //TODO here will involve some company policy, to be implement later
                }

            } catch (IntegrationException e) {
                //TODO here will involve retrying process, to be implement later
            }
        }
    }

    public void run() {
        while (CrawlingSystem.getInstance().isRunning()) {
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
