package net.args.mydailylook.model;

/**
 * Created by Administrator on 2016-09-04.
 */
public class ProfileModel extends BaseModel {
    private String accessToken;
    private String type;
    private String nickname;
    private String birth;
    private String gender;
    private String height_max;
    private String height_min;
    private String weight_max;
    private String weight_min;
    private String foot_max;
    private String foot_min;
    private String height_permission;
    private String weight_permission;
    private String foot_permission;
    private String description;

    public ProfileModel() {
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHeightMax() {
        return height_max;
    }

    public void setHeightMax(String height_max) {
        this.height_max = height_max;
    }

    public String getHeightMin() {
        return height_min;
    }

    public void setHeightMin(String height_min) {
        this.height_min = height_min;
    }

    public String getWeightMax() {
        return weight_max;
    }

    public void setWeightMax(String weight_max) {
        this.weight_max = weight_max;
    }

    public String getWeightMin() {
        return weight_min;
    }

    public void setWeightMin(String weight_min) {
        this.weight_min = weight_min;
    }

    public String getFootMax() {
        return foot_max;
    }

    public void setFootMax(String foot_max) {
        this.foot_max = foot_max;
    }

    public String getFootMin() {
        return foot_min;
    }

    public void setFootMin(String foot_min) {
        this.foot_min = foot_min;
    }

    public String getHeightPermission() {
        return height_permission;
    }

    public void setHeightPermission(String height_permission) {
        this.height_permission = height_permission;
    }

    public String getWeightPermission() {
        return weight_permission;
    }

    public void setWeightPermission(String weight_permission) {
        this.weight_permission = weight_permission;
    }

    public String getFootPermission() {
        return foot_permission;
    }

    public void setFootPermission(String foot_permission) {
        this.foot_permission = foot_permission;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
