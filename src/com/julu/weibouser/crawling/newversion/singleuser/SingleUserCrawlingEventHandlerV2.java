package com.julu.weibouser.crawling.newversion.singleuser;

import com.julu.weibouser.crawling.CrawlingSystem;
import com.julu.weibouser.crawling.newversion.CrawlingAction;
import com.julu.weibouser.crawling.newversion.states.SingleUserCrawlingState;
import com.julu.weibouser.crawling.newversion.states.StatesMachine;
import com.julu.weibouser.eventprocessing.EventSystem;
import com.julu.weibouser.eventprocessing.event.EventType;
import com.julu.weibouser.eventprocessing.exception.EventProcessingException;
import com.julu.weibouser.eventprocessing.handler.IHandler;
import com.julu.weibouser.eventprocessing.operator.StandalonePoller;
import com.julu.weibouser.eventprocessing.operator.StandalonePusher;
import com.julu.weibouser.logger.ConsoleLogger;


import java.util.concurrent.TimeUnit;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/23/12
 * Time: 3:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class SingleUserCrawlingEventHandlerV2 implements IHandler<SingleUserCrawlingEventV2>, Runnable {


    private static ConsoleLogger consoleLogger = new ConsoleLogger(SingleUserCrawlingEventHandlerV2.class.getName());

    StandalonePoller<SingleUserCrawlingEventV2, SingleUserCrawlingEventQueueV2> poller;

    public SingleUserCrawlingEventHandlerV2(StandalonePoller<SingleUserCrawlingEventV2, SingleUserCrawlingEventQueueV2> poller) {
        this.poller = poller;
    }

    public void run() {
        while (CrawlingSystem.getInstance().isRunning()) {
            try {
                SingleUserCrawlingEventV2 event = poller.consume(500l, TimeUnit.MILLISECONDS);
                if (event != null) {
                    handle(event);
                }
            } catch (EventProcessingException e) {
                consoleLogger.logError("Not successfully get event from event queue:" + EventType.FIND_USER_BY_SPECIFIED_UID, e);
            }
        }
    }

    public void handle(SingleUserCrawlingEventV2 event) {
        SingleUserCrawlingState currentState = event.getCurrentState();
        SingleUserCrawlingState nextState = StatesMachine.getNextState(currentState);

        if (event.getRetryingCount() > 3) {
            // throw out and dump into places for future investigation
        } else {
            CrawlingAction<SingleUserCrawlingEventV2> action = StatesMachine.getSingleUserCrawlingAction(nextState);
            if(action != null) {
                StandalonePusher<SingleUserCrawlingEventV2, SingleUserCrawlingEventQueueV2> pusher = EventSystem.
                        getPusher(EventType.FIND_USER_BY_SPECIFIED_UID);
                if(action.crawling(event)) {
                    //Successfully

                } else {
                    //Failed will retry at least 3 times
                }

                try {
                    pusher.push(event);
                } catch (EventProcessingException e) {
                    //Add logic future
                    consoleLogger.logError("Cannot push into the queue " + EventType.USER_PROCESSING, e);
                }
            } else if (action == null && currentState == SingleUserCrawlingState.END) {
                //Reach the final tasks we can finished the round of processing.
            } else if (action == null) {
                //May caused by one step didn't have configuration, wired situation.
            }
        }
    }
}
