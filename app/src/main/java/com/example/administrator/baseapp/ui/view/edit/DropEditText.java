package com.example.administrator.baseapp.ui.view.edit;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.administrator.baseapp.R;

import java.util.ArrayList;

/**
 * Created by beini on 2017/3/15.
 */

public class DropEditText extends FrameLayout implements View.OnClickListener, AdapterView.OnItemClickListener{
    private MEditChatText mEditText;  // 输入框
    private ImageView mDropImage; // 右边的图片按钮
    private PopupWindow mPopup; // 点击图片弹出popupwindow
    private DropListView mPopView; // popupwindow的布局
    private View mParentView;
    private int count;

    private int mDrawableLeft;
    private int mDropMode; // flower_parent or wrap_content
    private String mHit;
    static final String TAG = "DropEditText";

    public DropEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DropEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //文字输入框，点击的图片
        LayoutInflater.from(context).inflate(R.layout.edit_layout, this);
        //list view
        mPopView = (DropListView) LayoutInflater.from(context).inflate(R.layout.pop_view, null);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DropEditText, defStyle, 0);
        mDrawableLeft = ta.getResourceId(R.styleable.DropEditText_drawableRight, R.mipmap.ic_launcher);
        mDropMode = ta.getInt(R.styleable.DropEditText_dropMode, 0);
        mHit = ta.getString(R.styleable.DropEditText_hint);
        ta.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mEditText = (MEditChatText) findViewById(R.id.dropview_edit);
        mDropImage = (ImageView) findViewById(R.id.dropview_image);

        mEditText.setSelectAllOnFocus(true);
        mDropImage.setImageResource(mDrawableLeft);

        if(!TextUtils.isEmpty(mHit)) {
            mEditText.setHint(mHit);
        }

        mDropImage.setOnClickListener(this);
        mPopView.setOnItemClickListener(this);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // 如果布局发生改
        // 并且dropMode是flower_parent
        // 则设置ListView的宽度
        if(changed && 0 == mDropMode && mParentView != null) {
            int width2 = mParentView.getWidth();
            mPopView.setListWidth(mParentView.getWidth());
        }
    }

    /**
     * 设置Adapter
     * @param adapter ListView的Adapter
     */
    public void setParent(BaseAdapter adapter, View parentView) {
        mParentView = parentView;

        mPopView.setAdapter(adapter);

        count=adapter.getCount();
        mPopup = new PopupWindow(mPopView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopup.setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));
        mPopup.setFocusable(true); // 让popwin获取焦点
    }

    /** 会自动添加需要的项
     * @param parentView 要显示在父view下面，
     * @param list 显示数据的list
     */
    public void setParent(View parentView, final ArrayList<String> list, final IDropEdit iDropEdit){
        setParent(new BaseAdapter() {

            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Object getItem(int position) {
                return list.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.emall_spinner_item, null);
                //统一尺寸
                TextView tv = (TextView) convertView.findViewById(R.id.tv);
                ViewGroup.LayoutParams layoutParams = tv.getLayoutParams();

                int height = mParentView.getHeight();
                int width = mParentView.getWidth();
                layoutParams.width = width;
                layoutParams.height = height;
                tv.setLayoutParams(layoutParams);

                tv.setText(list.get(position));
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String email = list.get(position);
                        mEditText.setText(email);
                        mPopup.dismiss();
                        iDropEdit.OnClickCallback(email);
                    }
                });
                return convertView;
            }
        }, parentView);
    }

    /**
     * 获取输入框内的内容
     * @return String content
     */
    public String getText() {
        return mEditText.getText().toString();
    }


    public void setText(String text){
        mEditText.setText(text);
        mEditText.setTextColor(Color.parseColor("#000000"));
    }

    public EditText getmEditText() {
        return mEditText;
    }

    //    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.dropview_image || v.getId() == R.id.dropview_edit) {
            if(mPopup.isShowing()) {
                mPopup.dismiss();
                return;
            }

            mPopup.showAsDropDown(mParentView);
        }
    }
    public void setEnabled(boolean flag){
        mEditText.setEnabled(flag);
    }
    public void dismiss(){
        mPopup.dismiss();
    }

    /**
     * 设置不能用软键盘输入内容
     */
    public void setCannotInput() {
        mEditText.setFocusable(false);
        mEditText.setInputType(InputType.TYPE_NULL);
        mEditText.setOnClickListener(this);
        mDropImage.setVisibility(INVISIBLE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
//		mEditText.setText(mPopView.getAdapter().getItem(position).toString());
        mPopup.dismiss();
    }
}
