package com.beini.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by beini on 2017/7/10.
 */

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent();
        intent.setClass(this, IndexActivity.class);
        startActivity(intent);
        finish();
    }
}
