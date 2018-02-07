package com.beini.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.beini.R;
import com.beini.bind.ContentView;
import com.beini.bind.Event;
import com.beini.bind.ViewInject;
import com.beini.bind.ViewInjectorImpl;
import com.bumptech.glide.Glide;

/**
 * Created by beini on 2017/7/10.
 */
@ContentView(R.layout.activity_splash)
public class SplashActivity extends Activity {
    @ViewInject(R.id.image_splash)
    ImageView image_splash;
    private boolean isFinish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewInjectorImpl.registerInstance(this);
        Glide.with(this)
                .load("http://ojyz0c8un.bkt.clouddn.com/b_9.jpg")
                .into(image_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                goToIndexActivity();
            }
        }, 3500);
    }

    @Event({R.id.text_splash_jump})
    private void mEvent(View view) {
        switch (view.getId()) {
            case R.id.text_splash_jump:
                goToIndexActivity();
                break;

        }
    }

    public void goToIndexActivity() {
        if (isFinish) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, IndexActivity.class);
        overridePendingTransition(R.anim.screen_zoom_in, R.anim.screen_zoom_out);
        startActivity(intent);
        finish();
        isFinish = true;
    }
}
