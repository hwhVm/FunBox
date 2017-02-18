package com.example.administrator.baseapp.base;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.baseapp.bind.ViewInjectorImpl;
import com.example.administrator.baseapp.utils.BLog;


/**
 * Created by beini on 2017/2/8.
 */


public abstract class BaseFragment extends Fragment {
    public BaseActivity baseActivity;
    public boolean isVisible;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = ViewInjectorImpl.registerInstance(this, inflater, container);
        this.initView();
        BLog.d("  onCreateView ");
        return view;
    }

    public abstract void initView();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        BLog.d("   setUserVisibleHint  getUserVisibleHint()= "+getUserVisibleHint());
        if(getUserVisibleHint()) {//可见时调用
            isVisible = true;
            onVisible();
            BLog.d("  onVisible");
        } else {
            isVisible = false;
            BLog.d("  onInvisible");
            onInvisible();
        }
    }

    protected abstract void onInvisible();

    protected abstract void onVisible();

    @Override
    public void onAttach(Context context) {
        baseActivity = (BaseActivity) context;
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
      
    }
    /**
     * 懒加载接口
     */
    protected abstract void lazyLoad();
}
