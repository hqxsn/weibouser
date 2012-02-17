package com.julu.weibouser;

import com.julu.weibouser.system.AbstractBootstrap;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/17/12
 * Time: 10:37 AM
 * To change this template use File | Settings | File Templates.
 */
public class Bootstrap extends AbstractBootstrap {
    
    
    @Override
    public void doStartSystem() {

    }
    
    
    public static void main(String[] args) {
        if (null == args || args.length == 0 || args.length > 1)
            throw new IllegalArgumentException("Please input the correct args and the project is working w/ runtime.properties");

        if (!args[0].endsWith("runtime.properties"))
            throw new IllegalArgumentException("Please input the correct args and the project is working w/ runtime.properties");


        Bootstrap bootstrap = new Bootstrap();
        bootstrap.initiateSystem(args[0]);
        bootstrap.startSystem();
    }
}
