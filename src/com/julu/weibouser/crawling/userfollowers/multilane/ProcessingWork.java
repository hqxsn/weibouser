package com.julu.weibouser.crawling.userfollowers.multilane;

import com.julu.weibouser.crawling.userfollowers.UserFollowersCrawlingEvent;
import com.julu.weibouser.processing.UserProcessingEvent;
import com.tinybang.commonj.Work;
import com.tinybang.commonj.WorkStatus;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/26/12
 * Time: 5:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProcessingWork implements Work {

    private UserFollowersCrawlingEvent event;

    private WorkStatus status;

    public long getIdentifier() {
        return 0;
    }

    public WorkStatus getStatus() {
        return status;
    }

    public UserFollowersCrawlingEvent getEvent() {
        return event;
    }

    public void setEvent(UserFollowersCrawlingEvent event) {
        this.event = event;
    }

    public void setStatus(WorkStatus status) {
        this.status = status;
    }
}
