package com.julu.weibouser.crawling.hotuser;

import com.julu.weibouser.eventprocessing.event.Event;
import com.julu.weibouser.eventprocessing.event.EventType;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/26/12
 * Time: 12:01 AM
 * To change this template use File | Settings | File Templates.
 */
public class HotUserCrawlingEvent implements Event {

    private byte crawlingTarget;

    public EventType getEventType() {
        return EventType.FIND_USERS_BY_SUGGESTION_HOT;
    }

    public byte getRetryingCount() {
        return 0;
    }

    public byte getCrawlingTarget() {
        return crawlingTarget;
    }

    public void setCrawlingTarget(byte crawlingTarget) {
        this.crawlingTarget = crawlingTarget;
    }
}
