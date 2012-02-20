package com.julu.weibouser.processing.states;

import com.julu.weibouser.processing.Processing;
import com.julu.weibouser.processing.persistence.Peresistor;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/20/12
 * Time: 1:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class StatesMachine {
    
    public static State getNextState(State currentState) {

        State nextState = State.BEGIN;
        switch (currentState) {
            case BEGIN:
                nextState = State.PERSIST;
                break;
            case PERSIST:
                nextState = State.INDEXING;
                break;
            case INDEXING:
                nextState = State.STATS;
                break;
            case STATS:
                nextState = State.END;
                break;
        }

        return nextState;
    }

    public static Processing getProcessing(State requiredState) {

        Processing processing = null;
        
        switch (requiredState) {
            case BEGIN:
                break;
            case PERSIST:
                processing = new Peresistor();
                break;
            case INDEXING:
                break;
            case STATS:
                break;
            case END:
                break;
        }
        
        return processing;
    }
}
