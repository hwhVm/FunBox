package com.beini.ui.fragment.cpptest;


import android.view.View;
import android.widget.TextView;

import com.beini.R;
import com.beini.app.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.ViewInject;
import com.beini.ndk.NDKMain;
import com.beini.util.BLog;

import java.util.Random;

/**
 * Create by beini 2017/5/2
 */
@ContentView(R.layout.fragment_cpp)
public class CppFragment extends BaseFragment {
    @ViewInject(R.id.text_cpp)
    TextView text_cpp;

    public int getRandom(int max) {
        BLog.e("getRandom");
        return new Random().nextInt(max);
    }

    @Override
    public void initView() {
        baseActivity.setTopBar(View.GONE);
        baseActivity.setBottom(View.GONE);
        NDKMain ndkMain = new NDKMain();
        //访问Java类的非静态属性
//        BLog.e("befor--------------->str=" + ndkMain.str);
//        ndkMain.accessField();
//        BLog.e("after--------------->str=" + ndkMain.str);
        //访问Java类的静态属性
//        ndkMain.accessStaticField();
//        BLog.e("--------------->ndkMain.num="+ndkMain.num);
        //访问Java类的非静态方法
//        ndkMain.accessMethod();
        //访问Java类的静态方法
//        ndkMain.accessStaticMethod();
        //间接访问Java类的父类的方法
//        ndkMain.accessNonvirtualMethod();
        //访问Java类的构造方法
//        long time = ndkMain.accessConstructor();
//        BLog.e("time=" + time);
        //jni中文乱码问题
//        ndkMain.testChineseIn("了打来了");
//        BLog.e("" + ndkMain.testChineseOut());
//        System.out.print(""+ndkMain.testChineseOut());
//        int[] numArray = {3, 14};
//        ndkMain.sortArray(numArray);
        //局部引用
//        ndkMain.localRef();
        //全局引用
//        ndkMain.createGlobalRef();
//        try {
//            BLog.e(" " + ndkMain.getGlobalRef());
//        } catch (Error  e) {
//            BLog.e("发生异常" + e.getLocalizedMessage());
//        }
//        ndkMain.deteleGlobalRef();
//        BLog.e("(ndkMain.getGlobalRef() == null)= " + (ndkMain.getGlobalRef() == null));
        //自己写的 c方法
        ndkMain.getCmethod();
    }


}
