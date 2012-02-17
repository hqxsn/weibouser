package com.julu.weibouser.eventprocessing.queue;

import com.julu.weibouser.eventprocessing.event.Event;
import com.julu.weibouser.eventprocessing.event.EventType;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/17/12
 * Time: 1:40 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractEventBlockingQueue<E extends Event> implements EventQueue<E> {

    private BlockingQueue<E> m_queue;

    protected AbstractEventBlockingQueue(int capacity) {
        m_queue = new LinkedBlockingDeque<E>(capacity);
    }

    public void push(E item) throws InterruptedException {
        m_queue.put(item);
    }

    public E pop(long timeout, TimeUnit unit) throws InterruptedException {
        return m_queue.poll(timeout, unit);
    }
}
