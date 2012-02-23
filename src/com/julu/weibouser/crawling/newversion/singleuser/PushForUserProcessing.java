package com.julu.weibouser.crawling.newversion.singleuser;

import com.julu.weibouser.crawling.newversion.CrawlingAction;
import com.julu.weibouser.eventprocessing.exception.EventProcessingException;
import com.julu.weibouser.integration.Integration;
import com.julu.weibouser.logger.ConsoleLogger;
import com.julu.weibouser.model.IdService;
import com.julu.weibouser.model.User;
import com.julu.weibouser.processing.ProcessingSystem;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/23/12
 * Time: 4:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class PushForUserProcessing implements CrawlingAction<SingleUserCrawlingEventV2> {

    private static ConsoleLogger consoleLogger = new ConsoleLogger(PushForUserProcessing.class.getName());

    public boolean crawling(SingleUserCrawlingEventV2 event) {
        User juluUser = event.getUser();

        if (!Integration.hasProcessed(event.getCrawlingTarget(), juluUser.getOriginalSourceUid())) {
            try {
                juluUser.setUid(IdService.getUniqueId());
                ProcessingSystem.getInstance().push(Arrays.asList(juluUser));
            } catch (IOException e) {
                consoleLogger.logError("Cannot serialization user instance " + juluUser, e);
            } catch (EventProcessingException e) {
                //TODO here will involve retrying process;
                consoleLogger.logError("Cannot push user processing event into queue " + juluUser, e);
            }
        }
        
        return false;
    }
}
