package com.beini.ui.fragment.sms;

import android.os.Handler;
import android.os.Message;

/**
 * 短信处理
 */
public class SmsHandler extends Handler {

    private SmsResponseCallback mCallback;

    /***
     * 短信过滤器
     */
    private SmsFilter smsFilter;

    public SmsHandler(SmsResponseCallback callback) {
        this.mCallback = callback;
    }

    public SmsHandler(SmsResponseCallback callback, SmsFilter smsFilter) {
        this(callback);
        this.smsFilter = smsFilter;
    }

    /***
     * 设置短信过滤器
     * @param smsFilter 短信过滤器
     */
    public void setSmsFilter(SmsFilter smsFilter) {
        this.smsFilter = smsFilter;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (msg.what == SmsObserver.MSG_RECEIVED_CODE) {
            String[] smsInfos = (String[]) msg.obj;
            if (smsInfos != null && smsInfos.length == 2 && mCallback != null) {
                if (smsFilter == null) {
                    smsFilter = new DefaultSmsFilter();
                }
                mCallback.onCallbackSmsContent(smsFilter.filter(smsInfos[0], smsInfos[1]));
            }
        }
    }
}
