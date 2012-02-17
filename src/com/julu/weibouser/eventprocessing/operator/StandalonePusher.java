package com.julu.weibouser.eventprocessing.operator;

import com.julu.weibouser.eventprocessing.event.Event;
import com.julu.weibouser.eventprocessing.queue.EventQueue;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/17/12
 * Time: 2:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class StandalonePusher<E extends Event, Q extends EventQueue<E>> extends AbstractPusher<E, Q> {
    public StandalonePusher(Q queueRelated) {
        super(queueRelated);
    }
}
