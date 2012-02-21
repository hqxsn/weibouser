package com.julu.weibouser.eventprocessing.event;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/17/12
 * Time: 1:22 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Event {
    
    public EventType getEventType();

    public byte getRetryingCount();
    
}
