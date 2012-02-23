package com.julu.weibouser.crawling.newversion.singleuser;

import com.julu.weibouser.crawling.newversion.CrawlingAction;
import com.julu.weibouser.crawling.newversion.states.StatesMachine;
import com.julu.weibouser.crawling.userfollowers.UserFollowersCrawlingEvent;
import com.julu.weibouser.crawling.userfollowers.UserFollowersCrawlingEventFactory;
import com.julu.weibouser.crawling.userfollowers.UserFollowersCrawlingEventQueue;
import com.julu.weibouser.eventprocessing.EventSystem;
import com.julu.weibouser.eventprocessing.event.EventType;
import com.julu.weibouser.eventprocessing.exception.EventProcessingException;
import com.julu.weibouser.eventprocessing.operator.StandalonePusher;
import com.julu.weibouser.logger.ConsoleLogger;

import java.util.List;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/23/12
 * Time: 3:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class PushForUserFollowers implements CrawlingAction<SingleUserCrawlingEventV2> {

    private static ConsoleLogger consoleLogger = new ConsoleLogger(PushForUserFollowers.class.getName());

    public boolean crawling(SingleUserCrawlingEventV2 event) {

        List<UserFollowersCrawlingEvent> subsequentEvents = UserFollowersCrawlingEventFactory.
                create(event.getUser().getOriginalSourceUid(), event.getUser().getFollowersCount());

        StandalonePusher<UserFollowersCrawlingEvent, UserFollowersCrawlingEventQueue> pusher = EventSystem.getPusher(EventType.FIND_USER_FOLLOWERS);
        for (UserFollowersCrawlingEvent userFollowersCrawlingEvent : subsequentEvents) {
            try {
                pusher.push(userFollowersCrawlingEvent);
            } catch (EventProcessingException e) {
                consoleLogger.logError("Cannot push user followers finding event into queue " + userFollowersCrawlingEvent, e);
            }
        }


        event.setCurrentState(StatesMachine.getNextState(event.getCurrentState()));
        event.resetRetryCount();

        /**
         * This kind of error processing could generate new wrong event for retrying
         */

        return false;
    }
}
