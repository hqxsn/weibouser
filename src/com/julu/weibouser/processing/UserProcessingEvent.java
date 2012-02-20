package com.julu.weibouser.processing;

import com.julu.weibouser.eventprocessing.event.Event;
import com.julu.weibouser.eventprocessing.event.EventType;
import com.julu.weibouser.processing.states.State;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/20/12
 * Time: 12:03 AM
 * To change this template use File | Settings | File Templates.
 */
public class UserProcessingEvent implements Event {

    private byte[] relatedValue; //This value represents the List<User>, purpose for reduce memory consumption, leverage with msgpack
    
    private String relatedFilePath;

    private State currentState;

    private byte retryingCount = 0;

    public EventType getEventType() {
        return EventType.USER_PROCESSING;
    }

    public byte[] getRelatedValue() {
        return relatedValue;
    }

    public void setRelatedValue(byte[] relatedValue) {
        this.relatedValue = relatedValue;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public String getRelatedFilePath() {
        return relatedFilePath;
    }

    public void setRelatedFilePath(String relatedFilePath) {
        this.relatedFilePath = relatedFilePath;
    }

    public byte getRetryingCount() {
        return retryingCount;
    }

    public void increaseRetryCount() {
        ++this.retryingCount;
    }

    public void resetRetryCount() {
        this.retryingCount = 0;
    }
}
