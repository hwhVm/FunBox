package com.beini.ui.fragment.multimedia.ffmpeg;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.beini.R;
import com.beini.app.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.Event;
import com.beini.bind.ViewInject;
import com.beini.util.BLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create by beini 2017/7/29
 */
@ContentView(R.layout.fragment_ffmpeg4_android)
public class FFmpeg4AndroidFragment extends BaseFragment {
    private final int FILE_SELECT_CODE = 0x111;
    @ViewInject(R.id.gd_temp)
    GridView gd_temp;
    List<Map<String, Object>> data = new ArrayList<>();
    int[] dd = {R.mipmap._icon_zt, R.mipmap.app_icon_serch};

    @Override
    public void initView() {

        Map<String, Object> temp1 = new HashMap<>();
        temp1.put("aa", dd[0]);
        Map<String, Object> temp2 = new HashMap<>();
        temp2.put("aa", dd[1]);
        data.add(temp1);
        data.add(temp2);
        gd_temp.dispatchSetSelected(true);
        final SimpleAdapter simpleAdapter = new SimpleAdapter(baseActivity, data, R.layout.item_snap_pic, new String[]{"aa"}, new int[]{R.id.image_snap_pic});
        gd_temp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BLog.e("-------------->onItemClick");
                if (position == 0) {
                    ((ImageView) view.findViewById(R.id.image_snap_pic)).setImageDrawable(getResources().getDrawable(R.drawable.alert_btn_left_pressed));
                    ImageView imageView = ((ImageView) view.findViewById(R.id.image_snap_pic));
                    ViewGroup.LayoutParams lp = imageView.getLayoutParams();
                    lp.width = 555;
                    lp.height =55;
                    imageView.setLayoutParams(lp);
//                    gd_temp.setSelection(1);

                }

//                data.remove(1);
//                simpleAdapter.notifyDataSetChanged();
            }
        });

        gd_temp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                BLog.e("-------------->onItemSelected");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                BLog.e("-------------->onNothingSelected");
            }
        });
        gd_temp.setAdapter(simpleAdapter);
    }

    @Event({R.id.btn_ffmpeg_rotate_90, R.id.btn_choice_form_gallery})
    private void mEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_ffmpeg_rotate_90:
                break;
            case R.id.btn_choice_form_gallery://调用文件选择软件来选择文件
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                try {
                    startActivityForResult(Intent.createChooser(intent, "请选择一个要上传的文件"),
                            FILE_SELECT_CODE);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(), "请安装文件管理器", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            // Get the Uri of the selected file
            Uri uri = data.getData();
//                url = FFileUtils.getPath(getActivity(), uri);
//                Log.i("ht", "url" + url);
//                String fileName = url.substring(url.lastIndexOf("/") + 1);
//                intent = new Intent(getActivity(), UploadServices.class);
//                intent.putExtra("fileName", fileName);
//                intent.putExtra("url", url);
//                intent.putExtra("type ", "");
//                intent.putExtra("fuid", "");
//                intent.putExtra("type", "");
            BLog.e("      uri.getPath()==" + uri.getPath());
            BLog.e("     uri.getPort()==" + uri.getPort());
            BLog.e("      uri.getUserInfo()==" + uri.getUserInfo());

//                getActivity().startService(intent);

        }
        super.onActivityResult(requestCode, resultCode, data);

    }
}
