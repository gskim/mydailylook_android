package net.args.mydailylook.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016-10-10.
 */
public class Recommend extends BaseModel {
    private ArrayList<RecommendMember> data;

    public Recommend() {
    }

    public void setData(ArrayList<RecommendMember> data) {
        this.data = data;
    }

    public ArrayList<RecommendMember> getData() {
        return data;
    }
}
