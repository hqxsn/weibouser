package com.julu.weibouser.processing.persistence;

import com.julu.weibouser.model.User;
import com.julu.weibouser.processing.Processing;
import com.julu.weibouser.processing.UserProcessingEvent;
import com.julu.weibouser.processing.UserStreamUtil;
import com.julu.weibouser.processing.states.State;
import com.julu.weibouser.processing.states.StatesMachine;
import com.julu.weibouser.util.Utils;


import java.io.IOException;
import java.util.List;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/20/12
 * Time: 4:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class Peresistor implements Processing {
    
    public enum PERSIST_SELECTION {
        FILE, DB, FILE_AND_DB
    }
    
    public boolean processing(UserProcessingEvent event) {
        FilePersistor persistor = new FilePersistor();
        String filePath = persistor.persist(event.getRelatedValue());
        if (Utils.isNullOrEmpty(filePath)) {
            //event.setCurrentState(StatesMachine.getPreviousState(event.getCurrentState()));
            event.increaseRetryCount();
            return false;
        } else {
            event.setRelatedFilePath(filePath);
            event.setCurrentState(StatesMachine.getNextState(event.getCurrentState()));
            event.resetRetryCount();
        }

        return true;
    }
}
