package com.julu.weibouser.eventprocessing.queue;

import com.julu.weibouser.eventprocessing.event.Event;
import com.julu.weibouser.eventprocessing.event.EventType;

import java.util.concurrent.TimeUnit;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/17/12
 * Time: 1:38 PM
 * To change this template use File | Settings | File Templates.
 */
public interface EventQueue<E extends Event> {
    public EventType specifyRelatedEventType();
    
    public void push(E item) throws InterruptedException;

    public E pop(long timeout, TimeUnit unit) throws InterruptedException;
}
