package com.julu.weibouser.crawling.newversion.states;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/20/12
 * Time: 1:30 PM
 * To change this template use File | Settings | File Templates.
 */
public enum SingleUserCrawlingState {
    BEGIN, RETRIEVING, PUSHING_FOR_PROCESSING, FINDING_RELATED_FOLLOWERS, END
}
