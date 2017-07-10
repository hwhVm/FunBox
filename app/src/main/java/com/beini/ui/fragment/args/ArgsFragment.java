package com.beini.ui.fragment.args;


import android.content.Intent;
import android.widget.TextView;

import com.beini.R;
import com.beini.app.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.ViewInject;

/**
 * Create by beini 2017/5/23
 */
@ContentView(R.layout.fragment_args)
public class ArgsFragment extends BaseFragment {
    @ViewInject(R.id.text_show_name)
    TextView text_show_name;

    /**
     * mBundle.putSerializable(SER_KEY,mPerson);  传递对象
     * getArguments().getParcelable()
     */

    @Override
    public void initView() {
        String name = (String) getArguments().get("name");
        text_show_name.setText(name);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
