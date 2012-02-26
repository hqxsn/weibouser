package com.julu.weibouser.processing.mulitilane;

import com.julu.weibouser.eventprocessing.EventSystem;
import com.julu.weibouser.eventprocessing.event.EventType;
import com.julu.weibouser.eventprocessing.exception.EventProcessingException;
import com.julu.weibouser.eventprocessing.operator.StandalonePusher;
import com.julu.weibouser.logger.ConsoleLogger;
import com.julu.weibouser.processing.Processing;
import com.julu.weibouser.processing.UserProcessingEvent;
import com.julu.weibouser.processing.UserProcessingEventQueue;
import com.julu.weibouser.processing.states.State;
import com.julu.weibouser.processing.states.StatesMachine;
import com.tinybang.commonj.AbstractWorkExecutor;
import com.tinybang.commonj.EventListener;
import com.tinybang.commonj.WorkExecutor;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/26/12
 * Time: 5:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProcessingExecutor extends AbstractWorkExecutor<ProcessingWork> {
    private static ConsoleLogger consoleLogger = new ConsoleLogger(ProcessingExecutor.class.getName());

    public ProcessingExecutor(ProcessingWork work) {
        super(work);
    }

    public void execute(EventListener eventListener) {
        UserProcessingEvent event = work.getEvent();

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

    @Override
    public EventListener<ProcessingWork> getEventListener() {
        return null;
    }
}
