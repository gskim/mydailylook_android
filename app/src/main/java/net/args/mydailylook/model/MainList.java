package net.args.mydailylook.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016-09-25.
 */
public class MainList extends BaseModel {
    private ArrayList<MainListModel> data;

    public ArrayList<MainListModel> getData() {
        return data;
    }

    public void setData(ArrayList<MainListModel> data) {
        this.data = data;
    }
}
