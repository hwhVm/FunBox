package com.example.administrator.baseapp.net.response;

/**
 * Created by beini on 2017/2/10.
 */

public class BaseResponseJson {
    /**
     * ReturnCode : 0
     * ReturnMessage :
     */
    private int ReturnCode;
    private String ReturnMessage;

    public int getReturnCode() {
        return ReturnCode;
    }

    public void setReturnCode(int ReturnCode) {
        this.ReturnCode = ReturnCode;
    }

    public String getReturnMessage() {
        return ReturnMessage;
    }

    public void setReturnMessage(String ReturnMessage) {
        this.ReturnMessage = ReturnMessage;
    }
}
