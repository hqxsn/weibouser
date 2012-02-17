package com.julu.weibouser.eventprocessing.exception;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/17/12
 * Time: 2:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class EventProcessingException extends Exception {
    public EventProcessingException(String s, Exception e) {
        super(s, e);
    }
}
