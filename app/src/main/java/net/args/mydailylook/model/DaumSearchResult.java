package net.args.mydailylook.model;

import java.util.ArrayList;

/**
 * Created by arseon on 2016-10-27.
 */
public class DaumSearchResult extends BaseModel {
    private ArrayList<DaumSearchItem> item;
    private DaumSearchInfo info;

    public ArrayList<DaumSearchItem> getItem() {
        return item;
    }

    public void setItem(ArrayList<DaumSearchItem> item) {
        this.item = item;
    }

    public DaumSearchInfo getInfo() {
        return info;
    }

    public void setInfo(DaumSearchInfo info) {
        this.info = info;
    }
}

