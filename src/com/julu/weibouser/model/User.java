package com.julu.weibouser.model;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/17/12
 * Time: 12:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class User {

    private long uid;
    private long originalSourceUid;
    private byte cloneFrom;
    
    private String name;
    private String displayName;
    private String url;
    
    private String gender;

    private String location;

    private int followers;
    private int friendsCount;
    
    private boolean verified;


    public byte getCloneFrom() {
        return cloneFrom;
    }

    public void setCloneFrom(byte cloneFrom) {
        this.cloneFrom = cloneFrom;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFriendsCount() {
        return friendsCount;
    }

    public void setFriendsCount(int friendsCount) {
        this.friendsCount = friendsCount;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getOriginalSourceUid() {
        return originalSourceUid;
    }

    public void setOriginalSourceUid(long originalSourceUid) {
        this.originalSourceUid = originalSourceUid;
    }

    public static User compose(weibo4j.model.User sourceUser, byte cloneFrom) {
        User cloneUser = new User();

        cloneUser.setCloneFrom(cloneFrom);
        cloneUser.setOriginalSourceUid(Long.valueOf(sourceUser.getId())); //this is int64 type, in java should be long
        cloneUser.setName(sourceUser.getName());
        cloneUser.setDisplayName(sourceUser.getScreenName());
        cloneUser.setUrl(sourceUser.getUrl());
        cloneUser.setFollowers(sourceUser.getFollowersCount());
        cloneUser.setLocation(sourceUser.getLocation());
        cloneUser.setGender(sourceUser.getGender());//TODO:Need manipulate for more human reading like "Female" and "Male" rather than "F" and "M"
        cloneUser.setVerified(sourceUser.isVerified());
        cloneUser.setFriendsCount(sourceUser.getFriendsCount());

        return cloneUser;
    }
}
