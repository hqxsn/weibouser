package com.julu.weibouser.crawling.newversion.states;

import com.julu.weibouser.crawling.newversion.CrawlingAction;
import com.julu.weibouser.crawling.newversion.singleuser.FindUserBySpecificId;
import com.julu.weibouser.crawling.newversion.singleuser.PushForUserFollowers;
import com.julu.weibouser.crawling.newversion.singleuser.PushForUserProcessing;
import com.julu.weibouser.eventprocessing.event.Event;
import com.julu.weibouser.processing.Processing;
import com.julu.weibouser.processing.indexing.Indexer;
import com.julu.weibouser.processing.persistence.Peresistor;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/20/12
 * Time: 1:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class StatesMachine {
    
    public static SingleUserCrawlingState getNextState(SingleUserCrawlingState currentState) {

        SingleUserCrawlingState nextState = SingleUserCrawlingState.BEGIN;
        switch (currentState) {
            case BEGIN:
                nextState = SingleUserCrawlingState.RETRIEVING;
                break;
            case RETRIEVING:
                nextState = SingleUserCrawlingState.PUSHING_FOR_PROCESSING;
                break;
            case PUSHING_FOR_PROCESSING:
                nextState = SingleUserCrawlingState.FINDING_RELATED_FOLLOWERS;
                break;
            case FINDING_RELATED_FOLLOWERS:
                nextState = SingleUserCrawlingState.END;
                break;
        }

        return nextState;
    }

    public static <E extends Event> CrawlingAction<E> getSingleUserCrawlingAction(SingleUserCrawlingState requiredState) {
        CrawlingAction<E> action = null;

        switch (requiredState) {
            case BEGIN:
                break;
            case RETRIEVING:
                action = (CrawlingAction<E>) new FindUserBySpecificId();
                break;
            case PUSHING_FOR_PROCESSING:
                action = (CrawlingAction<E>) new PushForUserProcessing();
                break;
            case FINDING_RELATED_FOLLOWERS:
                action = (CrawlingAction<E>) new PushForUserFollowers();
                break;
            case END:
                break;
        }

        return action;
    }
}
