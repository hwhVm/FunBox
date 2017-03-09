package com.example.administrator.baseapp.ui.fragment.sideslip;


import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;

import com.example.administrator.baseapp.R;
import com.example.administrator.baseapp.base.BaseFragment;
import com.example.administrator.baseapp.bind.ContentView;
import com.example.administrator.baseapp.bind.ViewInject;

@ContentView(R.layout.fragment_sideslip)
public class SideslipFragment extends BaseFragment implements NavigationView.OnNavigationItemSelectedListener {
    @ViewInject(R.id.drawer_layout)
    DrawerLayout drawer;
    @ViewInject(R.id.nav_view)
    NavigationView navigationView;

    @Override
    public void initView() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                baseActivity, drawer, baseActivity.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
