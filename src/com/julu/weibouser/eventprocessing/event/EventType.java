package com.julu.weibouser.eventprocessing.event;

import com.julu.weibouser.crawling.user.SingleUserCrawlingEventQueue;
import com.julu.weibouser.crawling.userfollowers.UserFollowersCrawlingEventQueue;
import com.julu.weibouser.eventprocessing.operator.IPoller;
import com.julu.weibouser.eventprocessing.queue.EventQueue;
import com.julu.weibouser.processing.UserProcessingEventQueue;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/17/12
 * Time: 1:23 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EventType {
    FIND_USER_BY_SPECIFIED_UID {
        AtomicReference<SingleUserCrawlingEventQueue> queue = new AtomicReference<SingleUserCrawlingEventQueue>();
        public SingleUserCrawlingEventQueue getEventQueue() {
            if (queue.compareAndSet(null, new SingleUserCrawlingEventQueue(10))) {
                //Init here;
            }
            return queue.get();
        }
    },
    FIND_USERS_BY_SUGGESTION_HOT {

    },
    FIND_USER_FOLLOWERS {
        AtomicReference<UserFollowersCrawlingEventQueue> queue = new AtomicReference<UserFollowersCrawlingEventQueue>();
        public UserFollowersCrawlingEventQueue getEventQueue() {
            if (queue.compareAndSet(null, new UserFollowersCrawlingEventQueue(250))) {
                //Init here;
            }
            return queue.get();
        }
    },
    USER_PROCESSING {
        AtomicReference<UserProcessingEventQueue> queue = new AtomicReference<UserProcessingEventQueue>();
        public UserProcessingEventQueue getEventQueue() {
            if (queue.compareAndSet(null, new UserProcessingEventQueue(250))) {
                //Init here;
            }
            return queue.get();
        }
    };

    public EventQueue getEventQueue() {
        throw new AbstractMethodError();
    }

}
