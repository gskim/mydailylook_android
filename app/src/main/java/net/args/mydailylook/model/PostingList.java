package net.args.mydailylook.model;

import java.util.ArrayList;

/**
 * Created by arseon on 2016-10-17.
 */
public class PostingList extends BaseModel {
    private ArrayList<PostingInfo> data;

    public PostingList() {
    }

    public ArrayList<PostingInfo> getData() {
        return (data == null) ? new ArrayList<PostingInfo>() : data;
    }

    public void setData(ArrayList<PostingInfo> data) {
        this.data = data;
    }

}
