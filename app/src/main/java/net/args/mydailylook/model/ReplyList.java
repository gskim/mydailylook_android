package net.args.mydailylook.model;

import java.util.ArrayList;

/**
 * Created by arseon on 2016-10-19.
 */
public class ReplyList extends BaseModel {
    private ArrayList<Reply> data;

    public ArrayList<Reply> getData() {
        return data;
    }

    public void setData(ArrayList<Reply> data) {
        this.data = data;
    }

}
