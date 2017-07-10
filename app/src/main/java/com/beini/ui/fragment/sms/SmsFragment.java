package com.beini.ui.fragment.sms;

import android.util.Log;

import com.beini.R;
import com.beini.app.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.util.BLog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by beini on 2017/3/15
 */
@ContentView(R.layout.fragment_sms)
public class SmsFragment extends BaseFragment {
    SmsObserver smsObserver;

    @Override
    public void initView() {
        smsObserver = new SmsObserver(getActivity(), new SmsResponseCallback() {
            @Override
            public void onCallbackSmsContent(String smsContent) {
                //这里接收短信
                Log.d("com.beini", "smsContent==" + smsContent);  //null;
            }
        }, new VerificationCodeSmsFilter("13570419137"));

        smsObserver.registerSMSObserver();
    }

    public class VerificationCodeSmsFilter implements SmsFilter {
        /**
         * 需要过滤的发短信的人
         */
        private String filterAddress;

        public VerificationCodeSmsFilter(String filterAddress) {
            this.filterAddress = filterAddress;
        }

        @Override
        public String filter(String address, String smsContent) {
            BLog.d("address==" + address + "   smsContent==" + smsContent);
            if (address.startsWith(filterAddress)) {
                BLog.d("开始匹配");
                Pattern pattern = Pattern.compile("(\\d{4,8})");//匹配4-8位的数字
                Matcher matcher = pattern.matcher(smsContent);
                if (matcher.find()) {
                    return matcher.group(0);
                }
            }
            return null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        smsObserver.unregisterSMSObserver();
    }
}
