package net.args.mydailylook.model;

/**
 * Created by Administrator on 2016-10-16.
 */
public class PersonalInfo extends BaseModel {
    private String username;
    private String userId;
    private String profile_img_id;
    private String follow;
    private String postCount;
    private String followCount;
    private String followingCount;
    private String mine;

    public PersonalInfo() {
    }

    public boolean getMine() {
        if (mine == null)
            mine = "";

        return mine.equalsIgnoreCase("y");
    }

    public void setMine(String mine) {
        this.mine = mine;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId == null ? "" : userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProfileImgId() {
        return profile_img_id;
    }

    public void setProfileImgId(String profile_img_id) {
        this.profile_img_id = profile_img_id;
    }

    public String getFollow() {
        return follow == null ? "n" : follow;
    }

    public void setFollow(String follow) {
        this.follow = follow;
    }

    public String getPostCount() {
        return postCount;
    }

    public void setPostCount(String postCount) {
        this.postCount = postCount;
    }

    public String getFollowCount() {
        return followCount;
    }

    public void setFollowCount(String followCount) {
        this.followCount = followCount;
    }

    public String getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(String followingCount) {
        this.followingCount = followingCount;
    }

    @Override
    public String toString() {
        String toStr = "userId : " + userId + ",\n"
                + "username : " + username + ",\n"
                + "follow : " + follow + ",\n"
                + "followCount : " + followCount + ",\n"
                + "followingCount : " + followingCount + ",\n"
                + "postCount : " + postCount + ",\n"
                + "mine : " + mine;
        return toStr;
    }

}
