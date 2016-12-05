package net.args.mydailylook.model;

/**
 * Created by arseon on 2016-10-17.
 */
public class PostingInfo extends BaseModel {
    private String id;
    private String img_id;

    public PostingInfo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgId() {
        return img_id;
    }

    public void setImgId(String img_id) {
        this.img_id = img_id;
    }

}
