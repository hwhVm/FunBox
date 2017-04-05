package com.beini.ui.fragment.recording.model;

import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beini.R;
import com.beini.base.BaseApplication;

/**
 *
 */

public class VoiceModel {
    private static int[] micImg= new int[]{
            R.mipmap.ysq_icon_m_w_1, R.mipmap.ysq_icon_m_w_2, R.mipmap.ysq_icon_m_w_3, R.mipmap.ysq_icon_m_w_4,
            R.mipmap.ysq_icon_m_w_5, R.mipmap.ysq_icon_m_w_6, R.mipmap.ysq_icon_m_w_7, R.mipmap.ysq_icon_m_w_8
    };
    public void startRecording(){

    }


    public static void setMic(TextView voice_text, ImageView voiceMic, ImageView voice_say, ImageView voice_blackspot, boolean isStart){
        if (isStart) {
//            SPPService.getInstance().write(CmdManager.setPlayVoice((byte) 0));
            voice_say.setImageDrawable(BaseApplication.getInstance().getResources().getDrawable(R.mipmap.ysq_btn_dian_k));
            voice_blackspot.setImageDrawable(BaseApplication.getInstance().getResources().getDrawable(R.mipmap.ysq_btn_b_k));
            voice_text.setVisibility(View.VISIBLE);
            voiceMic.setBackground(null);
            voiceMic.setImageDrawable(null);
            AnimationDrawable animation = new AnimationDrawable();
            if (!animation.isRunning()) {

                animation.setOneShot(true);
                for (int i = 0; i < micImg.length; i++) {
                    animation.addFrame(BaseApplication.getInstance().getResources().getDrawable(micImg[(int) (Math.random() * 8)]), 500);
                }
                voiceMic.setBackground(animation);
                animation.start();

            }

        } else  {

            voice_text.setVisibility(View.INVISIBLE);
            voiceMic.setBackground(null);
            voiceMic.setImageDrawable(BaseApplication.getInstance().getResources().getDrawable(R.mipmap.ysq_icon_m_d));
        }
    }
}
