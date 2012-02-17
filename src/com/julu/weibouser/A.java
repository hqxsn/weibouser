package com.julu.weibouser;

import weibo4j.Friendships;
import weibo4j.Users;
import weibo4j.Weibo;
import weibo4j.examples.oauth2.Log;
import weibo4j.model.User;
import weibo4j.model.UserWapper;
import weibo4j.model.WeiboException;

import java.lang.String;
import java.util.List;

public class A {
    
    public static void main(String[] args) {
        Weibo weibo = new Weibo();
        weibo.setToken("2.00tyhuHC09EMdOb738a2d351GLPZdE");
        String uid =  "1949215451";
        Users um = new Users();
        try {
            User user = um.showUserById(uid);
            Log.logInfo(user.toString());

            user.getFollowersCount();
        } catch (WeiboException e) {
            e.printStackTrace();
        }

        Friendships friendships = new Friendships();
        try {
            UserWapper userWapper = friendships.getFollowersById("1949215451");
            List<User> users = userWapper.getUsers();
            
            for(User user:users) {
                Log.logInfo(user.toString());
            }
        } catch (WeiboException e) {
            e.printStackTrace();
        }

        try {
            UserWapper userWapper = friendships.getFollowersById("1949215451", 2, 1);
            List<User> users = userWapper.getUsers();

            for(User user:users) {
                Log.logInfo(user.toString());
            }
        } catch (WeiboException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
    
}