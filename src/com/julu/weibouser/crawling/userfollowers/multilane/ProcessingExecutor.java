package com.julu.weibouser.crawling.userfollowers.multilane;

import com.julu.weibouser.crawling.CrawlingSystem;
import com.julu.weibouser.crawling.userfollowers.UserFollowersCrawlingEvent;
import com.julu.weibouser.crawling.userfollowers.UserFollowersCrawlingEventFactory;
import com.julu.weibouser.crawling.userfollowers.UserFollowersCrawlingEventQueue;
import com.julu.weibouser.eventprocessing.EventSystem;
import com.julu.weibouser.eventprocessing.event.EventType;
import com.julu.weibouser.eventprocessing.exception.EventProcessingException;
import com.julu.weibouser.eventprocessing.operator.StandalonePusher;
import com.julu.weibouser.integration.Integration;
import com.julu.weibouser.integration.IntegrationException;
import com.julu.weibouser.integration.SinaWeibo;
import com.julu.weibouser.logger.ConsoleLogger;
import com.julu.weibouser.model.IdService;
import com.julu.weibouser.model.User;
import com.julu.weibouser.processing.ProcessingSystem;
import com.tinybang.commonj.AbstractWorkExecutor;
import com.tinybang.commonj.EventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/26/12
 * Time: 5:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProcessingExecutor extends AbstractWorkExecutor<ProcessingWork> {
    private static ConsoleLogger consoleLogger = new ConsoleLogger(ProcessingExecutor.class.getName());

    public ProcessingExecutor(ProcessingWork work) {
        super(work);
    }

    public void execute(EventListener eventListener) {
        //long timeMilli = System.currentTimeMillis();
        UserFollowersCrawlingEvent event = work.getEvent();
        long followUid = event.getOriginalSourceHostUid();
        int currentCursor = event.getCurrentCursor();
        int counts = event.getCounts();

        if (followUid > 0) {
            List<User> juluUsers = new ArrayList<User>();
            try {
                List<weibo4j.model.User> users = SinaWeibo.getInstance().getUserFollowers(followUid, currentCursor, counts);

                //System.out.println("UserFollowersCrawlingEventHandler.getinguserfollowers () need:" + (System.currentTimeMillis()-timeMilli));


                for(weibo4j.model.User user : users) {
                    com.julu.weibouser.model.User juluUser = com.julu.weibouser.model.User.compose(user, Integration.getSinaWeiboType());

                    if(!Integration.hasProcessed(event.getCrawlingTarget(), juluUser.getOriginalSourceUid())) {
                        juluUser.setUid(IdService.getUniqueId());
                        juluUsers.add(juluUser);
                    }

                    if (CrawlingSystem.getInstance().needDeeperAnalysis()) {
                        //Resend to the queue for further analysis
                        List<UserFollowersCrawlingEvent> subsequentEvents = UserFollowersCrawlingEventFactory.
                                create(juluUser.getOriginalSourceUid(), juluUser.getFollowersCount());

                        StandalonePusher<UserFollowersCrawlingEvent, UserFollowersCrawlingEventQueue> pusher = EventSystem.getPusher(EventType.FIND_USER_FOLLOWERS);
                        for (UserFollowersCrawlingEvent userFollowersCrawlingEvent : subsequentEvents) {
                            try {
                                pusher.push(userFollowersCrawlingEvent);
                            } catch (EventProcessingException e) {
                                //TODO here will involve retrying process, to be implement later
                                consoleLogger.logError("Cannot push user followers finding event into queue " + userFollowersCrawlingEvent, e);
                            }
                        }

                    }

                }
            } catch (IntegrationException e) {
                //TODO here will involve some company policy, to be implement later
                consoleLogger.logError("Cannot get user followers from weibo with uid:" + followUid + " currentCursor:"
                        + currentCursor + " counts:" + counts, e);
            }

            //System.out.println("UserFollowersCrawlingEventHandler.handle() constructing need:" + (System.currentTimeMillis()-timeMilli));

            if (juluUsers.size() > 0) {
                try {
                    ProcessingSystem.getInstance().push(juluUsers);
                } catch (IOException e) {
                    //TODO here will involve retrying process;
                    consoleLogger.logError("Cannot serialization user instance " + juluUsers, e);
                } catch (EventProcessingException e) {
                    //TODO here will involve retrying process;
                    consoleLogger.logError("Cannot push user processing event into queue " + juluUsers, e);
                }
            }

            //System.out.println("UserFollowersCrawlingEventHandler.handle() pushing need:" + (System.currentTimeMillis()-timeMilli));
        }
        //System.out.println("UserFollowersCrawlingEventHandler.handle() need:" + (System.currentTimeMillis()-timeMilli));
    }

    @Override
    public EventListener<ProcessingWork> getEventListener() {
        return null;
    }
}
