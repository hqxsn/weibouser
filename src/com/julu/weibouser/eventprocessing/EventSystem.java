package com.julu.weibouser.eventprocessing;

import com.julu.weibouser.eventprocessing.event.Event;
import com.julu.weibouser.eventprocessing.event.EventType;
import com.julu.weibouser.eventprocessing.operator.StandalonePoller;
import com.julu.weibouser.eventprocessing.operator.StandalonePusher;
import com.julu.weibouser.eventprocessing.queue.EventQueue;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/17/12
 * Time: 5:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class EventSystem {

    public static <E extends Event, Q extends EventQueue<E>> StandalonePusher<E,Q> getPusher(EventType eventType) {

        return new
                StandalonePusher<E, Q>(
                (Q) eventType.getEventQueue());
    }
    
    public static <E extends Event, Q extends EventQueue<E>> StandalonePoller<E,Q> getPoller(EventType eventType) {
        return new StandalonePoller<E, Q>((Q)eventType.getEventQueue());
    }

}
