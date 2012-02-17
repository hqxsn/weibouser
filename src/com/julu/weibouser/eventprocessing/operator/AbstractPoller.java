package com.julu.weibouser.eventprocessing.operator;

import com.julu.weibouser.eventprocessing.event.Event;
import com.julu.weibouser.eventprocessing.exception.EventProcessingException;
import com.julu.weibouser.eventprocessing.queue.EventQueue;

import java.util.concurrent.TimeUnit;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/17/12
 * Time: 2:06 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractPoller<E extends Event, Q extends EventQueue<E>> implements IPoller<E, Q> {
    
    private Q queueRelated;
    
    protected AbstractPoller(Q queueRelated) {
        this.queueRelated = queueRelated;
    }
    
    public E consume(long timeout, TimeUnit timeUnit) throws EventProcessingException {
        try {
            if (queueRelated != null)
                return queueRelated.pop(timeout, timeUnit);
            else
                return null; //TODO: Actually this is fatal error, in future we can improve this part.
        } catch (InterruptedException e) {
            throw new EventProcessingException("Not successfully retrieve the event from the queue", e);
        }
    }
}
