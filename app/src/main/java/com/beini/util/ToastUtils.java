package com.beini.util;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.beini.app.GlobalApplication;

/**
 * Create by beini 2017/7/11
 */
public class ToastUtils {

	static Toast mToast;
	static {
		mToast = Toast.makeText(GlobalApplication.getInstance(), "", Toast.LENGTH_SHORT);
	}
	private static Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if (msg.what==1){
				mToast.setText((String)msg.obj);
				mToast.setDuration(Toast.LENGTH_SHORT);
				mToast.show();
			}else if (msg.what==2){
				mToast.setText((String)msg.obj);
				mToast.setDuration(Toast.LENGTH_SHORT);
				mToast.show();
			}
		}
	};
	/**
	 * 显示短时间的Toast
	 * @param text 要显示的文本.
	 */
	public static void showShortToast(String text){
		Message msg=handler.obtainMessage(1);
		msg.obj=text;
		handler.sendMessage(msg);
	}

	public static void showLongToast(String toast){
		Message msg=handler.obtainMessage(2);
		msg.obj=toast;
		handler.sendMessage(msg);
	}
	
}
