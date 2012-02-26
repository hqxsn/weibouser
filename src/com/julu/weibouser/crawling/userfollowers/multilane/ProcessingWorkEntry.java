package com.julu.weibouser.crawling.userfollowers.multilane;


import com.tinybang.commonj.*;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/26/12
 * Time: 5:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProcessingWorkEntry extends AbstractWorkEntry {

    private ProcessingExecutor commandWorkExecutor;

    private ProcessingWork work;

    public WorkType getWorkType() {
        return WorkType.COMMAND;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Work getWork() {
        return work;
    }

    public WorkExecutor getWorkExecutor() {
        return new ProcessingExecutor(work);
    }

    public WorkStatus getStatus() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setWork(ProcessingWork work) {
        this.work = work;
    }
}
