package com.example.zfrproject.user;


import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.zfrproject.R;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.zfrproject.activity.AboutActivity;
import com.example.zfrproject.fragmentUser.CommentUtils;
import com.example.zfrproject.fragmentUser.BaseFragment;
import com.example.zfrproject.fragmentUser.FragmentFactory;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private TextView headerTextView;

    private TabLayout mTab;
    private ViewPager mViewPager;
    CharSequence username;

    private SharedPreferences pref;
    private String usernamepref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(UserActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(UserActivity.this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(UserActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(UserActivity.this, permissions, 1);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        //为了监听事件，在此处定义NavigationHeader
        View headView=navView.inflateHeaderView(R.layout.nav_header);
        headerTextView = headView.findViewById(R.id.nav_username);
        pref = PreferenceManager.getDefaultSharedPreferences(UserActivity.this);
        usernamepref = pref.getString("username", "");
        headerTextView.setText(usernamepref);
        navView.setItemIconTintList(null);

       // navView.setCheckedItem(R.id.nav_improveInfo);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                //mDrawerLayout.closeDrawers();
                switch (item.getItemId()) {
                    case R.id.nav_improveInfo:
                        Intent intent=new Intent(UserActivity.this,SurePasswordActivity.class);
                        intent.putExtra("username",username);
                        startActivity(intent);
                        break;
//                    case R.id.nav_admin:
//                        Intent intent1=new Intent(UserActivity.this,AdminLoginActivity.class);
//                        startActivity(intent1);
//                        //finish();
//                        break;
//                    case R.id.nav_fetter:
//                        Intent intent2=new Intent(UserActivity.this,FettlerLoginActivity.class);
//                        startActivity(intent2);
//                        finish();
//                        break
                    case R.id.nav_balance:
                        Intent intent3=new Intent(UserActivity.this,RechargeActivity.class);
                        intent3.putExtra("username",username);
                        startActivity(intent3);
                        break;
                    case R.id.nav_about:
                        Intent intent4=new Intent(UserActivity.this,AboutActivity.class);
                        startActivity(intent4);
                        break;
                    case R.id.nav_rport:
                        Intent intent5=new Intent(UserActivity.this,ReportChairActivity.class);
                        startActivity(intent5);
                        break;
                }
                return true;
            }
        });
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Data deleted", Snackbar.LENGTH_SHORT)
//                        .setAction("Undo", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Toast.makeText(UserActivity.this, "Data restored", Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                        .show();
//            }
//        });

        initView();
        initData();

        Intent intent = getIntent();
        username=(CharSequence)intent.getStringExtra("username");
        Log.e("用户界面username",""+username);



    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
//            case R.id.backup:
//                Toast.makeText(this, "You clicked Backup", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.delete:
//                Toast.makeText(this, "You clicked Delete", Toast.LENGTH_SHORT).show();
//                break;
            case R.id.settings:

                break;
            default:
        }
        return true;
    }

    private void initData() {
//        实例化，得到父容器的适配器
        ShortPagerAdapter adapter = new ShortPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mTab.setupWithViewPager(mViewPager);

    }

    private void initView() {
        mTab = (TabLayout) findViewById(R.id.tab);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
    }

    private class ShortPagerAdapter extends FragmentPagerAdapter {
        public String[] mTilte;

        public ShortPagerAdapter(FragmentManager fm) {
            super(fm);
            //获取标题布局
            mTilte = getResources().getStringArray(R.array.tab_user_Title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTilte[position];
        }

        @Override
        public BaseFragment getItem(int position) {
            //设置绑定Fragment
            BaseFragment fragment = FragmentFactory.createUserFragment(position);
            return fragment;
        }

        //标题数量,长度
        @Override
        public int getCount() {
            return CommentUtils.TAB_USER_COUNT;
        }
    }

    //点击返回键返回桌面而不是退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
