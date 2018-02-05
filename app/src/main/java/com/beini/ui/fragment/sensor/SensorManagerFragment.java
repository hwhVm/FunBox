package com.beini.ui.fragment.sensor;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;

import com.beini.R;
import com.beini.app.BaseFragment;
import com.beini.app.GlobalApplication;
import com.beini.bind.ContentView;
import com.beini.util.BLog;

import java.util.Date;
import java.util.List;

/**
 * Create by beini 2018/2/5
 *
 */
@ContentView(R.layout.fragment_sensor_manager)
public class SensorManagerFragment extends BaseFragment implements SensorEventListener {
    SensorManager sensorManager;

    @Override
    public void initView() {
        sensorManager = (SensorManager) GlobalApplication.getInstance().getSystemService(Context.SENSOR_SERVICE);
        showSensorList();//矩形
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int sensorType = event.sensor.getType();
        if (sensorType == Sensor.TYPE_ACCELEROMETER) {
            // values[0]:X轴，values[1]：Y轴，values[2]：Z轴
            float[] values = event.values;
            if ((Math.abs(values[0]) > 15 || Math.abs(values[1]) > 15
                    || Math.abs(values[2]) > 15)) {
                BLog.e("摇一摇");
            }
        } else if (sensorType == Sensor.TYPE_LIGHT) {
            float light_strength = event.values[0];
//            BLog.e(" 当前光线强度为" + light_strength + "   -" + new Date().toLocaleString());
        } else if (sensorType == Sensor.TYPE_PROXIMITY) {
            float distance = event.values[0];
            BLog.e(" 有不明物体接近！距离" + distance + "厘米  -" + new Date().toLocaleString());
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void showSensorList() {
        baseActivity.setBottom(View.GONE);
        baseActivity.setTopBar(View.GONE);
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
        for (Sensor sensor : sensorList) {
            BLog.e(" sensor.getName()=" + sensor.getName());
        }
    }


}
