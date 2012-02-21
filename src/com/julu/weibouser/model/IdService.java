package com.julu.weibouser.model;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by StarCite.
 * User: andy.song
 * Date: 2/21/12
 * Time: 4:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class IdService {

    private static AtomicLong id_ = new AtomicLong(1000000000l);

    public static long getUniqueId() {
        return id_.incrementAndGet();
    }

}
