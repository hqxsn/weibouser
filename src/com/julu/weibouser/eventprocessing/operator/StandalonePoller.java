package com.julu.weibouser.eventprocessing.operator;

import com.julu.weibouser.eventprocessing.event.Event;
import com.julu.weibouser.eventprocessing.queue.EventQueue;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/17/12
 * Time: 2:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class StandalonePoller<E extends Event, Q extends EventQueue<E>> extends AbstractPoller<E, Q> {

    public StandalonePoller(Q queueRelated) {
        super(queueRelated);
    }
}
