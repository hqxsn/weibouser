package com.julu.weibouser.crawling.newversion.singleuser;

import com.julu.weibouser.eventprocessing.event.EventType;
import com.julu.weibouser.eventprocessing.queue.AbstractEventBlockingQueue;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/23/12
 * Time: 3:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class SingleUserCrawlingEventQueueV2 extends AbstractEventBlockingQueue<SingleUserCrawlingEventV2> {

    public SingleUserCrawlingEventQueueV2(int capacity) {
        super(capacity);
    }

    public EventType specifyRelatedEventType() {
        return EventType.FIND_USER_BY_SPECIFIED_UID;
    }
}
