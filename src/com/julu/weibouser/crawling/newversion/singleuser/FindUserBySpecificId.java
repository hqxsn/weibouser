package com.julu.weibouser.crawling.newversion.singleuser;

import com.julu.weibouser.crawling.newversion.CrawlingAction;
import com.julu.weibouser.crawling.newversion.states.StatesMachine;
import com.julu.weibouser.integration.IntegrationException;
import com.julu.weibouser.integration.SinaWeibo;
import com.julu.weibouser.util.Utils;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/23/12
 * Time: 3:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class FindUserBySpecificId implements CrawlingAction<SingleUserCrawlingEventV2> {
    public boolean crawling(SingleUserCrawlingEventV2 event) {

        long uid = event.getOriginalSourceUid();

        boolean processing = true;

        if (uid > 0) {
            try {
                weibo4j.model.User user = SinaWeibo.getInstance().getUser(uid);

                if (user != SinaWeibo.USER_NOT_FOUND) {
                    com.julu.weibouser.model.User juluUser = com.julu.weibouser.model.User.compose(user, event.getCrawlingTarget());
                    event.setUser(juluUser);
                }
            } catch (IntegrationException e) {
                processing = false;
                //log the information

            }
        }

        if (!processing) {
            //event.setCurrentState(StatesMachine.getPreviousState(event.getCurrentState()));
            event.increaseRetryingCount();
            return false;
        } else {
            event.setCurrentState(StatesMachine.getNextState(event.getCurrentState()));
            event.resetRetryCount();
        }

        return processing;
    }
}
