package com.julu.weibouser.system;

import com.julu.weibouser.config.PushToRuntimeProperties;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/17/12
 * Time: 10:21 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractBootstrap implements IBootstrap {

    public void initiateSystem(String configurationFile) {
        //Load runtime properties
        PushToRuntimeProperties.setupRuntimeProperty(configurationFile);
    }

    abstract public void doStartSystem();


    public void startSystem(){
        doStartSystem();
    }

}