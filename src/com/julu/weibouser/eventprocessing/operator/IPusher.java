package com.julu.weibouser.eventprocessing.operator;

import com.julu.weibouser.eventprocessing.event.Event;
import com.julu.weibouser.eventprocessing.exception.EventProcessingException;
import com.julu.weibouser.eventprocessing.queue.EventQueue;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/17/12
 * Time: 1:59 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IPusher<E extends Event, Q extends EventQueue<E>> {
    
    public void push(E item) throws EventProcessingException;
    
}
