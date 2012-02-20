package com.julu.weibouser.processing;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/20/12
 * Time: 4:48 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Processing {

    public boolean processing(UserProcessingEvent event);

}
