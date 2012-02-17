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
        int repeatCnts = followerCnt / batchSize;
        
        int startPos = 0;
        List<UserFollowersCrawlingEvent> results = new ArrayList<UserFollowersCrawlingEvent>(repeatCnts);
        for(int i = 0; i < repeatCnts; ++i) {


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

}
