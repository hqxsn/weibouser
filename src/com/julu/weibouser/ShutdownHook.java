package com.julu.weibouser;

import com.julu.weibouser.crawling.CrawlingSystem;
import com.julu.weibouser.processing.ProcessingSystem;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/23/12
 * Time: 3:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class ShutdownHook extends Thread {

    ProcessingSystem processingSystem;

    CrawlingSystem crawlingSystem;

    public ShutdownHook(ProcessingSystem processingSystem, CrawlingSystem crawlingSystem) {
        this.processingSystem = processingSystem;
        this.crawlingSystem = crawlingSystem;
    }

    public void run() {
        crawlingSystem.stopRunning();
        processingSystem.stopRunning();
        //Need some validation check
    }

}
