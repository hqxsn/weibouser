package com.julu.weibouser.crawling.user;

import com.julu.weibouser.eventprocessing.event.Event;
import com.julu.weibouser.eventprocessing.event.EventType;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/17/12
 * Time: 1:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class SingleUserCrawlingEvent implements Event {
    
    private long originalSourceUid;

    private byte crawlingTarget;

    public EventType getEventType() {
        return EventType.FIND_USER_BY_SPECIFIED_UID;
    }

    public long getOriginalSourceUid() {
        return originalSourceUid;
    }

    public void setOriginalSourceUid(long originalSourceUid) {
        this.originalSourceUid = originalSourceUid;
    }


    public byte getCrawlingTarget() {
        return crawlingTarget;
    }

    public void setCrawlingTarget(byte crawlingTarget) {
        this.crawlingTarget = crawlingTarget;
    }
}
