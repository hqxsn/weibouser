package com.julu.weibouser.processing;

import com.julu.weibouser.eventprocessing.event.EventType;
import com.julu.weibouser.eventprocessing.queue.AbstractEventBlockingQueue;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/20/12
 * Time: 12:16 AM
 * To change this template use File | Settings | File Templates.
 */
public class UserProcessingEventQueue extends AbstractEventBlockingQueue<UserProcessingEvent> {
    public UserProcessingEventQueue(int capacity) {
        super(capacity);
    }

    public EventType specifyRelatedEventType() {
        return EventType.USER_PROCESSING;
    }
}
