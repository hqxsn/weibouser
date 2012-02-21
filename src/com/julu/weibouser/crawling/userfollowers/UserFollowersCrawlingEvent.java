package com.julu.weibouser.crawling.userfollowers;

import com.julu.weibouser.eventprocessing.event.Event;
import com.julu.weibouser.eventprocessing.event.EventType;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/17/12
 * Time: 1:28 PM
 * Each time try begin from currentCursor specified position and retrieving
 * the counts specified numbers of user as followers of originalSourceHostUid specified user.
 */
public class UserFollowersCrawlingEvent implements Event {
    
    //This uid is mainly point to the weibo host uid. Like "姚晨“ etc.
    private long originalSourceHostUid;
    
    //The position begin fetch the user followers
    private int currentCursor;

    //The counts need retrieve
    private int counts;

    private byte crawlingTarget;

    private byte retryingCount = 0;
    
    public EventType getEventType() {
        return EventType.FIND_USER_FOLLOWERS;
    }

    public byte getRetryingCount() {
        return retryingCount;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void increaseRetryingCount() {
        ++ retryingCount;
    }

    public long getOriginalSourceHostUid() {
        return originalSourceHostUid;
    }

    public void setOriginalSourceHostUid(long originalSourceHostUid) {
        this.originalSourceHostUid = originalSourceHostUid;
    }

    public int getCurrentCursor() {
        return currentCursor;
    }

    public void setCurrentCursor(int currentCursor) {
        this.currentCursor = currentCursor;
    }

    public int getCounts() {
        return counts;
    }

    public void setCounts(int counts) {
        this.counts = counts;
    }

    public byte getCrawlingTarget() {
        return crawlingTarget;
    }

    public void setCrawlingTarget(byte crawlingTarget) {
        this.crawlingTarget = crawlingTarget;
    }
}
