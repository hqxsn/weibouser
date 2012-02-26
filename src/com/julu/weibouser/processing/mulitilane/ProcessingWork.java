package com.julu.weibouser.processing.mulitilane;

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

    private UserProcessingEvent event;

    private WorkStatus status;

    public long getIdentifier() {
        return 0;
    }

    public WorkStatus getStatus() {
        return status;
    }

    public UserProcessingEvent getEvent() {
        return event;
    }

    public void setEvent(UserProcessingEvent event) {
        this.event = event;
    }

    public void setStatus(WorkStatus status) {
        this.status = status;
    }
}
