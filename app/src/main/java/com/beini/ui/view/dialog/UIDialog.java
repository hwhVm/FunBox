package com.beini.ui.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.beini.R;
import com.beini.ui.view.edit.EmojiAndChatFilter;
import com.beini.util.DensityUtils;
import com.beini.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by beini
 */

public class UIDialog {
	private Context context;
	private Dialog dialog;


	private Button btn_neg;
	private Button btn_pos;
	private ImageView img_line;
	private EditText txt_edit;
	private TextView txt_title;
	private TextView txt_msg;
	private TextView txt_loading;
	private LinearLayout loading;
	private LinearLayout lLayout_bg;
	private LinearLayout lLayout_content;
	private ImageView img_loading;
	private ImageView thread ;
	private Display display;
	private List<ItemClass> itemList;
	private boolean showTitle = false;
	private boolean showMsg = false;
	private boolean showPosBtn = false;
	private boolean showNegBtn = false;
	private boolean showEdit=false;
	private boolean showItem=false;
	private boolean showLoading=false;
	private int loadingDelayedTime = 300;//ShowLoading的延时时间

	static private UIDialog preDialog = null;
	static private Context preContext;

	static private String TAG = "UIDialog";

	public UIDialog(Context context) {
		this.context = context;
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();
		if(preDialog !=null && context == preContext){
			preDialog.dismiss();
		}
		preContext = context;
		preDialog = this;
	}


	public UIDialog builder() {

		View view = LayoutInflater.from(context).inflate(R.layout.view_alertdialog, null);


		lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
		thread=(ImageView)view.findViewById(R.id.xian);
		txt_title = (TextView) view.findViewById(R.id.txt_title);
		txt_title.setVisibility(View.GONE);
		txt_msg = (TextView) view.findViewById(R.id.txt_msg);
		txt_msg.setVisibility(View.GONE);
		btn_neg = (Button) view.findViewById(R.id.btn_neg);
		btn_neg.setVisibility(View.GONE);
		btn_pos = (Button) view.findViewById(R.id.btn_pos);
		btn_pos.setVisibility(View.GONE);
		img_line = (ImageView) view.findViewById(R.id.img_line);
		img_line.setVisibility(View.GONE);
		txt_edit= (EditText) view.findViewById(R.id.txt_edit);
		txt_edit.setFilters(new EmojiAndChatFilter[]{new EmojiAndChatFilter()});
		txt_edit.setVisibility(View.GONE);
		lLayout_content= (LinearLayout) view.findViewById(R.id.lLayout_content);
		loading=(LinearLayout) view.findViewById(R.id.loading);
		loading.setVisibility(View.GONE);
		txt_loading = (TextView) view.findViewById(R.id.txt_loading);
		img_loading=(ImageView)view.findViewById(R.id.img_loading);

		dialog = new Dialog(context, R.style.AlertDialogStyle);
		dialog.setContentView(view);

		//设置dialog大小
		lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display.getWidth() * 0.85), LayoutParams.WRAP_CONTENT));

		return this;
	}

	/**
	 * @param visibility 是否显示这个dialog
	 */
	public void setVisibility(int visibility){
		lLayout_bg.setVisibility(visibility);
	}

	/**
	 * @return
	 */
	public int getVisibility(){
		return lLayout_bg.getVisibility();
	}

	/**
	 * 添加条目
	 * 第一个参数，条目标题
	 * 第二个参数，十六进制颜色字符串不包括#，例（"00ff00"）,传空为默认黑色
	 * */
	public UIDialog addItem(String strItemTitle, String color, OnItemClickListener listener) {
		showItem=true;
		if (itemList == null) {
			itemList = new ArrayList<ItemClass>();
		}
		itemList.add(new ItemClass(strItemTitle, color, listener));
		return this;
	}
	/**
	 * 设置标题内容
	 * */
	public UIDialog setTitle(String title) {
		showTitle = true;
		if ("".equals(title)) {
			txt_title.setText("");
		} else {
			txt_title.setText(title);
		}
		return this;
	}
	/**
	 * 是否显示编辑框
	 * */
	public UIDialog setEdit(boolean show){

		showEdit=show;
		return this;
	}
	public UIDialog setEditText(String text){
		if (showEdit){
			txt_edit.setText(text);
		}
		return this;
	}

	/**
	 * 设置提示信息
	 * */
	public UIDialog setMsg(String msg) {
		showMsg = true;
		if ("".equals(msg)) {
			txt_msg.setText("");
		} else {
			txt_msg.setText(msg);
		}
		return this;
	}
	/**
	 * 设置loading
	 * */
	public UIDialog setLoading(String txt){
		showLoading=true;
		txt_loading.setText(txt);
		return this;
	}
	/**
	 * 回退键是否关闭对话框
	 * */
	public UIDialog setCancelable(boolean cancel) {
		dialog.setCancelable(cancel);
		return this;
	}
	/**
	 * 点击其他位置是否消失
	 * */
	public UIDialog setCanceledOnTouchOutside(boolean cancel){
		dialog.setCanceledOnTouchOutside(cancel);
		return this;
	}

	public UIDialog setPositiveButton(String text, final OnClickListener listener) {
		showPosBtn = true;
		if ("".equals(text)) {
			btn_pos.setText(StringUtils.getString(R.string.dialog_ok));
		} else {
			btn_pos.setText(text);
		}
		btn_pos.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listener!=null)
					listener.onClick(v);
				dialog.dismiss();
			}
		});
		return this;
	}

	public UIDialog setPositiveButtonNoDis(String text, final OnClickListener listener) {
		showPosBtn = true;
		if ("".equals(text)) {
			btn_pos.setText(StringUtils.getString(R.string.dialog_ok));
		} else {
			btn_pos.setText(text);
		}
		btn_pos.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listener!=null)
					listener.onClick(v);
//				    dialog.dismiss();
			}
		});
		return this;
	}
	/**
	 * 获取编辑框内容
	 * */
	public String getEditText(){
		return txt_edit.getText().toString();
	}

	public UIDialog setNegativeButton(String text, final OnClickListener listener) {
		showNegBtn = true;
		if ("".equals(text)) {
			btn_neg.setText(StringUtils.getString(R.string.dialog_cancel));
		} else {
			btn_neg.setText(text);
		}
		btn_neg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listener!=null)
				listener.onClick(v);
				dialog.dismiss();
			}
		});
		return this;
	}

	private void setLayout() {


		if (showTitle) {
			txt_title.setVisibility(View.VISIBLE);
		}

		if (showMsg) {
			txt_msg.setVisibility(View.VISIBLE);
		}
		if (showEdit){
			txt_edit.setVisibility(View.VISIBLE);
		}
		if (showEdit&!showMsg){
			txt_edit.setVisibility(View.VISIBLE);
		}



		if (showPosBtn && showNegBtn) {
			btn_pos.setVisibility(View.VISIBLE);
			btn_pos.setBackgroundResource(R.drawable.alertdialog_right_selector);
			btn_neg.setVisibility(View.VISIBLE);
			btn_neg.setBackgroundResource(R.drawable.alertdialog_left_selector);
			img_line.setVisibility(View.VISIBLE);
		}

		if (showPosBtn && !showNegBtn) {
			btn_pos.setVisibility(View.VISIBLE);
			btn_pos.setBackgroundResource(R.drawable.alertdialog_single_selector);
		}

		if (!showPosBtn && showNegBtn) {
			btn_neg.setVisibility(View.VISIBLE);
			btn_neg.setBackgroundResource(R.drawable.alertdialog_single_selector);
		}

		if (showLoading){
			thread.setVisibility(View.GONE);
			loading.setVisibility(View.VISIBLE);
			Animation operatingAnim = AnimationUtils.loadAnimation(context, R.anim.dialog_loading_anim);
			LinearInterpolator lin = new LinearInterpolator();
			operatingAnim.setInterpolator(lin);
			img_loading.startAnimation(operatingAnim);
			lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display.getWidth() * 0.5), (int) (display.getHeight() * 0.2)));
			lLayout_bg.getBackground().setAlpha(150);

		}
		if (showItem){
			// 循环添加条目
			for (int i = 0; i < itemList.size(); i++) {
				final ItemClass item = itemList.get(i);
				TextView textView = new TextView(context);
				textView.setText(item.name);
				textView.setTextSize(18);
				textView.setGravity(Gravity.CENTER);

				textView.setTextColor(Color.parseColor("#"+(item.color.equals("")?"000000":item.color)));
				textView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, DensityUtils.dp2px(context,45)));

				// 点击事件
				final int finalI = i;
				textView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						item.itemClickListener.onClick(finalI);
						dialog.dismiss();
					}
				});

				lLayout_content.addView(textView);
				btn_neg.setVisibility(View.GONE);
				btn_pos.setVisibility(View.GONE);
				txt_title.setVisibility(View.GONE);
				if (i!=itemList.size()-1){

					TextView thread = new TextView(context);
					thread.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, DensityUtils.dp2px(context,1)));
					thread.setGravity(Gravity.CENTER);
					thread.setBackgroundColor(Color.parseColor("#EE9A00"));
					lLayout_content.addView(thread);
				}

			}
		}
	}



	public void dismiss() {
		dialog.dismiss();
	}


	public interface OnItemClickListener {
		void onClick(int pos);
	}

	public class ItemClass {
		String name;
		OnItemClickListener itemClickListener;
		String color;

		public ItemClass(String name, String color, OnItemClickListener itemClickListener) {
			this.name = name;
			this.color = color;
			this.itemClickListener = itemClickListener;
		}
	}

	public Dialog getDialog() {
		return dialog;
	}

	public UIDialog setLoadingDelayedTime(int loadingDelayedTime) {
		this.loadingDelayedTime = loadingDelayedTime;
		return this;
	}
	public UIDialog show() {
		setLayout();
		dialog.show();
		return this;
	}
}
