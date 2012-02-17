package com.julu.weibouser.crawling.userfollowers;

import com.julu.weibouser.eventprocessing.event.Event;
import com.julu.weibouser.eventprocessing.event.EventType;
import com.julu.weibouser.eventprocessing.queue.AbstractEventBlockingQueue;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/17/12
 * Time: 1:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserFollowersCrawlingEventQueue<E extends Event> extends AbstractEventBlockingQueue<E> {
    public UserFollowersCrawlingEventQueue(int capacity) {
        super(capacity);
    }

    public EventType specifyRelatedEventType() {
        return EventType.FIND_USER_FOLLOWERS;
    }
}
