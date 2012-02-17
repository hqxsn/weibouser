package com.julu.weibouser.system;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/17/12
 * Time: 10:19 AM
 * To change this template use File | Settings | File Templates.
 */
public interface IBootstrap {

    public void initiateSystem(String configurationFile);

    public void startSystem();

}