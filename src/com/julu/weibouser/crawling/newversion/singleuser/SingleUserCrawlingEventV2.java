package com.julu.weibouser.crawling.newversion.singleuser;

import com.julu.weibouser.crawling.newversion.states.SingleUserCrawlingState;
import com.julu.weibouser.crawling.user.SingleUserCrawlingEvent;
import com.julu.weibouser.model.User;
import com.julu.weibouser.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/23/12
 * Time: 3:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class SingleUserCrawlingEventV2 extends SingleUserCrawlingEvent {

    private SingleUserCrawlingState currentState;

    private byte retryingCount = 0;
    
    private User user;

    public SingleUserCrawlingState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(SingleUserCrawlingState currentState) {
        this.currentState = currentState;
    }

    public byte getRetryingCount() {
        return retryingCount;
    }

    public void setRetryingCount(byte retryingCount) {
        this.retryingCount = retryingCount;
    }



    public void resetRetryCount() {

        retryingCount = 0;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
