package com.julu.weibouser.integration;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/17/12
 * Time: 3:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class IntegrationException extends Exception {
    
    public IntegrationException(String s, Exception e) {
        super(s, e);
    }
    
}
