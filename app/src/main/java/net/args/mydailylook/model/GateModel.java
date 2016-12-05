package net.args.mydailylook.model;

/**
 * Created by Administrator on 2016-08-21.
 */
public class GateModel extends BaseModel {
    private String eventUrl;
    private String appVersion;
    private String isForce;

    public String getEventUrl() {
        return eventUrl;
    }

    public void setEventUrl(String eventUrl) {
        this.eventUrl = eventUrl;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getIsForce() {
        return isForce;
    }

    public void setIsForce(String isForce) {
        this.isForce = isForce;
    }

}
