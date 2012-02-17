package com.julu.weibouser.eventprocessing.operator;

import com.julu.weibouser.eventprocessing.event.Event;
import com.julu.weibouser.eventprocessing.exception.EventProcessingException;
import com.julu.weibouser.eventprocessing.queue.EventQueue;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/17/12
 * Time: 2:12 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractPusher<E extends Event, Q extends EventQueue<E>> implements IPusher<E, Q> {

    private Q queueRelated;

    protected AbstractPusher(Q queueRelated) {
        this.queueRelated = queueRelated;
    }
    
    public void push(E item) throws EventProcessingException {
        try {
            if (queueRelated != null)
                queueRelated.push(item);
            else
                return; //TODO: Actually this is fatal error, in future we can improve this part.
        } catch (InterruptedException e) {
            throw new EventProcessingException("Not successfully push the event into the queue", e);
        }
    }
}
