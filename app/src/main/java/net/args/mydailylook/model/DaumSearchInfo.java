package net.args.mydailylook.model;

/**
 * Created by arseon on 2016-10-27.
 */
public class DaumSearchInfo extends BaseModel {
    private String count;
    private String page;
    private String totalCount;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

}
