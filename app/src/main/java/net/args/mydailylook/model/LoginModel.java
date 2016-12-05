package net.args.mydailylook.model;

/**
 * Created by Administrator on 2016-08-21.
 */
public class LoginModel extends BaseModel {
    private String nickName;
    private String accessToken;

    public LoginModel() {
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    
}
