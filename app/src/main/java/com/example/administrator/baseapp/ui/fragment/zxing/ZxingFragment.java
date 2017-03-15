package com.example.administrator.baseapp.ui.fragment.zxing;


import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.administrator.baseapp.R;
import com.example.administrator.baseapp.base.BaseFragment;
import com.example.administrator.baseapp.bind.ContentView;
import com.example.administrator.baseapp.bind.Event;
import com.example.administrator.baseapp.bind.ViewInject;
import com.example.administrator.baseapp.ui.fragment.zxing.activity.CaptureActivity;
import com.example.administrator.baseapp.ui.fragment.zxing.encoding.EncodingUtils;
import com.example.administrator.baseapp.utils.BLog;
import com.example.administrator.baseapp.utils.listener.ActivityResultListener;

/**
 * https://github.com/zxing/zxing
 */
@ContentView(R.layout.fragment_zxing)
public class ZxingFragment extends BaseFragment implements ActivityResultListener {
    @ViewInject(R.id.tv_content)
    private TextView tv_content;
    @ViewInject(R.id.et_input)
    private TextView et_input;
    @ViewInject(R.id.img)
    private ImageView img;

    @Event({R.id.btnSan,R.id.btn_generate})
    private void mEvent(View view) {
        switch (view.getId()) {
            case R.id.btnSan:
                baseActivity.startActivityForResult(new Intent(baseActivity, CaptureActivity.class), 0);
                break;
            case R.id.btn_generate:
                String str = et_input.getText().toString();
                if (str.equals("")) {
                    Toast.makeText(baseActivity, "不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    // 位图
                    try {
                        /**
                         * 参数：1.文本 2 3.二维码的宽高 4.二维码中间的那个logo
                         */
                        Bitmap bitmap = EncodingUtils.createQRCode(str, 500, 500, null);
                        // 设置图片
                        img.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                break;
        }

    }

    @Override
    public void initView() {
        baseActivity.setActivityResultListener(this);
    }

    @Override
    public void resultCallback(int requestCode, int resultCode, Intent data) {
        BLog.d("  resultCallback");
        if (resultCode == baseActivity.RESULT_OK) {
            BLog.d(" RESULT_OK resultCallback");
            String result = data.getExtras().getString("result");
            tv_content.setText(result);
        }
    }


}
