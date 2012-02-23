package com.julu.weibouser.crawling.newversion;

import com.julu.weibouser.eventprocessing.event.Event;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/23/12
 * Time: 3:24 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CrawlingAction<E extends Event> {
    
    public boolean crawling(E event);
    
}
