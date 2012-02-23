package com.julu.weibouser;

import com.julu.weibouser.crawling.CrawlingSystem;
import com.julu.weibouser.processing.ProcessingSystem;
import com.julu.weibouser.system.AbstractBootstrap;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/17/12
 * Time: 10:37 AM
 * To change this template use File | Settings | File Templates.
 */
public class Bootstrap extends AbstractBootstrap {

    ProcessingSystem processingSystem;

    CrawlingSystem crawlingSystem;
    
    @Override
    public void doStartSystem() {
        //Start processing system
        processingSystem = ProcessingSystem.getInstance();
        //Start CrawlingSystem module and trigger specified user analysis
        crawlingSystem = CrawlingSystem.getInstance();
        //Add shutdown hook for close the system properly
        Runtime.getRuntime().addShutdownHook(new ShutdownHook(processingSystem, crawlingSystem));

        crawlingSystem.simpleAnalysis();

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
