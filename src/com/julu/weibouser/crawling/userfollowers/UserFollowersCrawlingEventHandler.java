package com.julu.weibouser.crawling.userfollowers;

import com.julu.weibouser.crawling.CrawlingSystem;
import com.julu.weibouser.eventprocessing.event.EventType;
import com.julu.weibouser.eventprocessing.exception.EventProcessingException;
import com.julu.weibouser.eventprocessing.handler.IHandler;
import com.julu.weibouser.eventprocessing.operator.StandalonePoller;
import com.julu.weibouser.integration.Integration;
import com.julu.weibouser.integration.IntegrationException;
import com.julu.weibouser.integration.SinaWeibo;
import com.julu.weibouser.logger.ConsoleLogger;
import com.julu.weibouser.processing.ProcessingSystem;
import weibo4j.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/18/12
 * Time: 12:45 AM
 * To change this template use File | Settings | File Templates.
 */
public class UserFollowersCrawlingEventHandler implements IHandler<UserFollowersCrawlingEvent>, Runnable {

    private static ConsoleLogger consoleLogger = new ConsoleLogger(UserFollowersCrawlingEventHandler.class.getName());

    private StandalonePoller<UserFollowersCrawlingEvent, UserFollowersCrawlingEventQueue> poller;

    public UserFollowersCrawlingEventHandler(StandalonePoller<UserFollowersCrawlingEvent, UserFollowersCrawlingEventQueue> poller) {
        this.poller = poller;
    }

    public void handle(UserFollowersCrawlingEvent event) {
        long followUid = event.getOriginalSourceHostUid();
        int currentCursor = event.getCurrentCursor();
        int counts = event.getCounts();
        
        if (followUid > 0) {
            List<com.julu.weibouser.model.User> juluUsers = new ArrayList<com.julu.weibouser.model.User>();
            try {
                List<User> users = SinaWeibo.getInstance().getUserFollowers(followUid, currentCursor, counts);
                

                for(User user : users) {
                    com.julu.weibouser.model.User juluUser = com.julu.weibouser.model.User.compose(user, Integration.getSinaWeiboType());
                    juluUsers.add(juluUser);
                }
            } catch (IntegrationException e) {
                //TODO here will involve some company policy, to be implement later
                consoleLogger.logError("Cannot get user followers from weibo with uid:" + followUid + " currentCursor:"
                        + currentCursor + " counts:" + counts, e);
            }

            if (juluUsers.size() > 0) {
                try {
                    ProcessingSystem.getInstance().push(juluUsers);
                } catch (IOException e) {
                    //TODO here will involve retrying process;
                    consoleLogger.logError("Cannot serialization user instance " + juluUsers, e);
                } catch (EventProcessingException e) {
                    //TODO here will involve retrying process;
                    consoleLogger.logError("Cannot push user processing event into queue " + juluUsers, e);
                }
            }
        }

    }

    public void run() {
        while (CrawlingSystem.getInstance().isRunning()) {
            try {
                UserFollowersCrawlingEvent event = poller.consume(500l, TimeUnit.MILLISECONDS);
                if (event != null) {
                    handle(event);
                }
            } catch (EventProcessingException e) {
                consoleLogger.logError("Not successfully get event from event queue:" + EventType.FIND_USER_BY_SPECIFIED_UID, e);
            }
        }
    }
}
