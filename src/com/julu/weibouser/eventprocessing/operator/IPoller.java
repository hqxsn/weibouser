package com.julu.weibouser.eventprocessing.operator;

import com.julu.weibouser.eventprocessing.event.Event;
import com.julu.weibouser.eventprocessing.exception.EventProcessingException;
import com.julu.weibouser.eventprocessing.queue.EventQueue;

import java.util.concurrent.TimeUnit;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/17/12
 * Time: 2:04 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IPoller<E extends Event, Q extends EventQueue<E>> {
    
    public E consume(long timeout, TimeUnit timeUnit) throws EventProcessingException;
    
}
