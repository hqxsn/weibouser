package com.julu.weibouser.util;

import com.julu.weibouser.model.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/17/12
 * Time: 10:28 AM
 * To change this template use File | Settings | File Templates.
 */
public class Utils {

    public static String outputStackTrace(Throwable ex) {
        String returnValue = "";

        ByteArrayOutputStream bos = null;
        PrintStream ps = null;
        try {
            bos = new ByteArrayOutputStream();
            ps = new PrintStream(bos, true);
            ex.printStackTrace(ps);

            ps.flush();
            bos.flush();

            returnValue = bos.toString();
        } catch (Exception ext) {
            //Swallow
        } finally {
            if (ps != null) {
                ps.close();
            }

            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                }
            }
        }

        return returnValue;
    }

    public static boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty();
    }

    public static boolean isNullOrEmpty(List<User> list) {
        return list == null || list.size() == 0;
    }
}
