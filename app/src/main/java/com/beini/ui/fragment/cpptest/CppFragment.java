package com.beini.ui.fragment.cpptest;


import android.widget.TextView;

import com.beini.R;
import com.beini.app.BaseFragment;
import com.beini.app.GlobalApplication;
import com.beini.bind.ContentView;
import com.beini.bind.ViewInject;

/**
 * Create by beini 2017/5/2
 */
@ContentView(R.layout.fragment_cpp)
public class CppFragment extends BaseFragment {
    @ViewInject(R.id.text_cpp)
    TextView text_cpp;

    @Override
    public void initView() {
        showToast();
        GlobalApplication.getInstance().getNdk().withArgs(" hellow  C++");
    }

    public  void showToast() {
        text_cpp.setText(GlobalApplication.getInstance().getNdk().getPassword());
    }
}
