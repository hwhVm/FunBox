package com.beini.ui.fragment.green;


import android.view.View;
import android.widget.Toast;

import com.beini.R;
import com.beini.app.BaseFragment;
import com.beini.bean.green.User;
import com.beini.bind.ContentView;
import com.beini.bind.Event;
import com.beini.db.greendao.GreenDaoManage;
import com.beini.db.greendao.UserDao;
import com.beini.util.BLog;

import java.util.List;


/**
 * Create by beini 2017/4/5
 */
@ContentView(R.layout.fragment_green_dao)
public class GreenDaoFragment extends BaseFragment {
    UserDao userDao;

    @Override
    public void initView() {
        userDao = GreenDaoManage.getSingleton().getmDaoSession().getUserDao();
    }

    /**
     * @param view
     */
    @Event({R.id.btn_green_insert, R.id.btn_green_delete, R.id.btn_green_edit, R.id.btn_green_query, R.id.btn_green_delete_table, R.id.btn_green_query_by_condition})
    private void mEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_green_insert:
                User user1 = new User();
                user1.setUsername("beini1");
                userDao.insert(user1);
                User user2 = new User();
                user2.setUsername("beini2");
                userDao.insert(user2);
                break;
            case R.id.btn_green_delete:
                userDao.deleteByKey(2L);
                break;
            case R.id.btn_green_edit:
                User user3 = new User();
                user3.setId(2L);
                user3.setUsername("hwh");
                userDao.update(user3);
                break;
            case R.id.btn_green_query:
                List<User> users = userDao.loadAll();
                for (int i = 0; i < users.size(); i++) {
                    BLog.d("   --->" + users.get(i).getUsername());
                }
                break;
            case R.id.btn_green_delete_table:
                userDao.deleteAll();
                break;
            case R.id.btn_green_query_by_condition:
                User user = userDao.queryBuilder()
                        .where(UserDao.Properties.Username.eq("beini1")).build().unique();
                BLog.d("           " + (user == null));
                if (user == null) {
                    Toast.makeText(baseActivity, "用户不存在!", Toast.LENGTH_SHORT).show();
                } else {
                    user.setUsername("666666");
                    userDao.update(user);
                }
                break;
            case R.id.btn_green_query_condition:
                List<User> list = userDao.queryBuilder()
                        .where(UserDao.Properties.Id.between(2, 13)).limit(5).build().list();
                for (int i = 0; i < list.size(); i++) {
                  BLog.d(list.get(i).toString());
                }
                break;
        }
    }
}
