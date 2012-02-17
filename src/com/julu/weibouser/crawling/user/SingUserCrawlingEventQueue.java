package com.julu.weibouser.crawling.user;

import com.julu.weibouser.eventprocessing.event.Event;
import com.julu.weibouser.eventprocessing.event.EventType;
import com.julu.weibouser.eventprocessing.queue.AbstractEventBlockingQueue;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/17/12
 * Time: 1:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class SingUserCrawlingEventQueue extends AbstractEventBlockingQueue<SingleUserCrawlingEvent> {

    public SingUserCrawlingEventQueue(int capacity) {
        super(capacity);
    }

    public EventType specifyRelatedEventType() {
        return EventType.FIND_USER_BY_SPECIFIED_UID;
    }
}
