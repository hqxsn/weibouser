package com.julu.weibouser.crawling.userfollowers;

import com.julu.weibouser.config.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/18/12
 * Time: 12:16 AM
 * To change this template use File | Settings | File Templates.
 */
public class UserFollowersCrawlingEventFactory {

    public static List<UserFollowersCrawlingEvent> create(long followUid, int followerCnt) {

        int batchSize = getBatchSize();


        if (followerCnt > getMaximumSize()) {
            followerCnt = getMaximumSize();
        }

        int repeatCnts = (followerCnt / batchSize) + (followerCnt % batchSize != 0 ? 1 : 0);
        int startPos = 0;
        List<UserFollowersCrawlingEvent> results = new ArrayList<UserFollowersCrawlingEvent>(repeatCnts);
        for (int i = 0; i < repeatCnts; ++i) {


            UserFollowersCrawlingEvent event = new UserFollowersCrawlingEvent();
            event.setOriginalSourceHostUid(followUid);
            event.setCurrentCursor(startPos);
            event.setCounts(batchSize);
            results.add(event);

            startPos += batchSize;
        }

        return results;
    }

    static int getBatchSize() {
        return Integer.getInteger(Configuration.BATCH_CALLING_SIZE, 200);
    }

    public static int getMaximumSize() {
        return Integer.getInteger(Configuration.GET_FOLLOWERS_MAXIMUM_SIZE, 5000);
    }
}
