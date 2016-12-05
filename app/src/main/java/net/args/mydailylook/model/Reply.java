package net.args.mydailylook.model;

/**
 * Created by Administrator on 2016-09-25.
 */
public class Reply extends BaseModel {
    private String id;
    private String profile_img_id;
    private String username;
    private String userId;
    private String content;
    private String regdate;

    public Reply(String username, String content) {
        this.username = username;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getProfileImgId() {
        return profile_img_id;
    }

    public void setProfileImgId(String profile_img_id) {
        this.profile_img_id = profile_img_id;
    }

    public String getRegdate() {
        return regdate;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }

}
