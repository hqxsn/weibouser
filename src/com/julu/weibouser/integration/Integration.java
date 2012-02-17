package com.julu.weibouser.integration;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/17/12
 * Time: 3:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class Integration {

    private static byte SINA_WEIBO = 1;

    public static byte getSinaWeiboType() {
        return SINA_WEIBO;
    }

    public static boolean isSinaWeibo(byte type) {
        return (SINA_WEIBO ^ type) == 0;
    }

}
