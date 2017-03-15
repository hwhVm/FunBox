package com.example.administrator.baseapp.ui.view.edit;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;

/**
 * Created by beini on 2017/3/15.
 */

public class MEditChatText extends android.support.v7.widget.AppCompatEditText {
    public MEditChatText(Context context) {
        super(context);
    }

    public MEditChatText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MEditChatText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }
    static private String TAG = "MEditText";


    @Override
    public boolean dispatchKeyEventPreIme(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
        return super.dispatchKeyEventPreIme(event);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.setFilters(new EmojiAndChatFilter[]{new EmojiAndChatFilter()});
    }
}
