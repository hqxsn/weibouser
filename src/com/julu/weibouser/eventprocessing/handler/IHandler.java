package com.julu.weibouser.eventprocessing.handler;

import com.julu.weibouser.eventprocessing.event.Event;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/17/12
 * Time: 2:25 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IHandler<E extends Event> {
    
    public void handle(E event);
    
}
