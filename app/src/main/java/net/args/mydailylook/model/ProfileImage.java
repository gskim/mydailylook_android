package net.args.mydailylook.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016-10-23.
 */
public class ProfileImage extends BaseModel {
    @SerializedName("img_id")
    private String imgId;

    public ProfileImage() {
    }

    public String getImgId() {
        return imgId == null ? "" : imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }
}
