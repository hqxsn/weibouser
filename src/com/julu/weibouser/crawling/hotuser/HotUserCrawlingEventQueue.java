package com.julu.weibouser.crawling.hotuser;

import com.julu.weibouser.eventprocessing.event.EventType;
import com.julu.weibouser.eventprocessing.queue.AbstractEventBlockingQueue;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/26/12
 * Time: 12:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class HotUserCrawlingEventQueue extends AbstractEventBlockingQueue<HotUserCrawlingEvent> {
    public HotUserCrawlingEventQueue(int capacity) {
        super(capacity);
    }

    public EventType specifyRelatedEventType() {
        return EventType.FIND_USERS_BY_SUGGESTION_HOT;
    }
}
