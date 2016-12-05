package net.args.mydailylook.model;

/**
 * Created by arseon on 2016-09-27.
 */
public class RecommendMember extends BaseModel {
    private String id;
    private String username;
    private String description;
    private String profile_photo;
    private String follow;

    public RecommendMember(String username, String description, String profile_photo, String follow) {
        this.username = username;
        this.description = description;
        this.profile_photo = profile_photo;
        this.follow = follow;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProfilePhoto() {
        return profile_photo;
    }

    public void setProfilePhoto(String profile_photo) {
        this.profile_photo = profile_photo;
    }

    public String getFollow() {
        return follow;
    }

    public void setFollow(String follow) {
        this.follow = follow;
    }

}
