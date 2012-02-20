package com.julu.weibouser.processing;

import com.julu.weibouser.eventprocessing.EventSystem;
import com.julu.weibouser.eventprocessing.event.EventType;
import com.julu.weibouser.eventprocessing.exception.EventProcessingException;
import com.julu.weibouser.eventprocessing.operator.StandalonePoller;
import com.julu.weibouser.eventprocessing.operator.StandalonePusher;
import com.julu.weibouser.model.User;
import com.julu.weibouser.processing.states.State;
import org.msgpack.MessagePack;
import org.msgpack.packer.Packer;
import org.msgpack.template.ListTemplate;
import org.msgpack.template.Template;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/20/12
 * Time: 12:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProcessingSystem {

    private volatile boolean running = true;

    private static ProcessingSystem processingSystem = new ProcessingSystem();

    private ProcessingSystem() {
        init();
    }

    private void init() {
        initUserProcessing();
    }

    private void initUserProcessing() {
        UserProcessingEventQueue userFollowersCrawlingEventQueue = (UserProcessingEventQueue) EventType.USER_PROCESSING.getEventQueue();
        UserProcessingEventHandler userFollowersCrawlingEventHandler = new UserProcessingEventHandler(
                new StandalonePoller<UserProcessingEvent, UserProcessingEventQueue>(userFollowersCrawlingEventQueue));
        new Thread(userFollowersCrawlingEventHandler).start();
    }

    public static ProcessingSystem getInstance() {
        return processingSystem;
    }

    public boolean isRunning() {
        return running;
    }

    public void stopRunning() {
        running = false;
    }

    public void push(List<User> needProcessingUsers) throws IOException, EventProcessingException {
        byte[] bytes = UserStreamUtil.serialization(needProcessingUsers);
        UserProcessingEvent event = new UserProcessingEvent();
        event.setCurrentState(State.BEGIN);
        event.setRelatedValue(bytes);

        StandalonePusher<UserProcessingEvent, UserProcessingEventQueue> pusher = EventSystem.getPusher(EventType.USER_PROCESSING);

        pusher.push(event);
    }

}
