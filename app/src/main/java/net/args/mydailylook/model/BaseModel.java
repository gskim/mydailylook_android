package net.args.mydailylook.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016-07-17.
 */
public class BaseModel implements Serializable {
    private String code;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    
}
