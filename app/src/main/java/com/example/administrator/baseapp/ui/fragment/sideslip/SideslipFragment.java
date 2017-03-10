package com.example.administrator.baseapp.ui.fragment.sideslip;


import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import com.example.administrator.baseapp.R;
import com.example.administrator.baseapp.base.BaseFragment;
import com.example.administrator.baseapp.bind.ContentView;
import com.example.administrator.baseapp.bind.ViewInject;
import com.example.administrator.baseapp.utils.listener.KeyBackListener;

@ContentView(R.layout.fragment_sideslip)
public class SideslipFragment extends BaseFragment implements NavigationView.OnNavigationItemSelectedListener, KeyBackListener {
    @ViewInject(R.id.drawer_layout)
    DrawerLayout drawer;
    @ViewInject(R.id.nav_view)
    NavigationView navigationView;


    @Override
    public void initView() {
        baseActivity.setBottom(View.GONE);
        baseActivity.setTopBar(View.VISIBLE);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                baseActivity, drawer, baseActivity.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        drawer.addDrawerListener(drawerListener);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        baseActivity.setKeyBackListener(null);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_camera) {
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void keyBack() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onKeyDown(KeyEvent event) {

    }

    DrawerLayout.DrawerListener drawerListener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(View drawerView) {

            baseActivity.setKeyBackListener(SideslipFragment.this);
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            baseActivity.setKeyBackListener(null);
        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };


}
