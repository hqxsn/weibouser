package com.julu.weibouser.processing;

import com.julu.weibouser.crawling.userfollowers.UserFollowersCrawlingEvent;
import com.julu.weibouser.crawling.userfollowers.UserFollowersCrawlingEventQueue;
import com.julu.weibouser.eventprocessing.EventSystem;
import com.julu.weibouser.eventprocessing.event.EventType;
import com.julu.weibouser.eventprocessing.exception.EventProcessingException;
import com.julu.weibouser.eventprocessing.handler.IHandler;
import com.julu.weibouser.eventprocessing.operator.StandalonePoller;
import com.julu.weibouser.eventprocessing.operator.StandalonePusher;
import com.julu.weibouser.logger.ConsoleLogger;
import com.julu.weibouser.model.User;
import com.julu.weibouser.processing.states.State;
import com.julu.weibouser.processing.states.StatesMachine;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/20/12
 * Time: 12:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class UserProcessingEventHandler implements IHandler<UserProcessingEvent>, Runnable {

    private static ConsoleLogger consoleLogger = new ConsoleLogger(UserProcessingEventHandler.class.getName());
    private StandalonePoller<UserProcessingEvent, UserProcessingEventQueue> poller;

    public UserProcessingEventHandler(StandalonePoller<UserProcessingEvent, UserProcessingEventQueue> poller) {
        this.poller = poller;
    }

    public void handle(UserProcessingEvent event) {

        State currentState = event.getCurrentState();
        State nextState = StatesMachine.getNextState(currentState);
        
        if (event.getRetryingCount() > 3) {
            // throw out and dump into places for future investigation
        } else {
            Processing processing = StatesMachine.getProcessing(nextState);
            if(processing != null) {
                 StandalonePusher<UserProcessingEvent, UserProcessingEventQueue> pusher = EventSystem.getPusher(EventType.USER_PROCESSING);
                 if(processing.processing(event)) {
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
            } else if (processing == null && currentState == State.END) {
                //Reach the final tasks we can finished the round of processing.
            } else if (processing == null) {
                //May caused by one step didn't have configuration, wired situation.
            }
        }


    }

    public void run() {

        while (ProcessingSystem.getInstance().isRunning()) {
            try {
                UserProcessingEvent event = poller.consume(500l, TimeUnit.MILLISECONDS);
                if (event != null) {
                    handle(event);
                }
            } catch (EventProcessingException e) {
                consoleLogger.logError("Not successfully get event from event queue:" + EventType.USER_PROCESSING, e);
            }
        }

    }
}
