package com.example.zfrproject.activity;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.zfrproject.R;
import com.example.zfrproject.fragmentUser.BaseFragment;
import com.example.zfrproject.fragmentUser.CommentUtils;
import com.example.zfrproject.fragmentUser.FragmentFactory;
//需解决Fragment释放问题
//静态类或者有个Activity拿着这个fragment的实例引用，那就没法回收的，只有没有别的地方引用了，才会回收。
public class AdminActivity extends AppCompatActivity {
    //tablayout和viewpager
    private TabLayout mTab;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
//设置toolbar等
        Intent intent = getIntent();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        setSupportActionBar(toolbar);
        //启用HomeAsUp
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initView();
        initData();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void initData() {
//        实例化，得到父容器的适配器
        AdminActivity.UserPagerAdapter adapter = new AdminActivity.UserPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mTab.setupWithViewPager(mViewPager);

    }

    private void initView() {
        mTab = (TabLayout) findViewById(R.id.admin_tab);
        mViewPager = (ViewPager) findViewById(R.id.admin_viewpager);
    }
//内部类适配Fragment
    private class UserPagerAdapter extends FragmentPagerAdapter {
        public String[] mTilte;

        public UserPagerAdapter(FragmentManager fm) {
            super(fm);
            mTilte = getResources().getStringArray(R.array.tab_admin_Title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTilte[position];
        }

        @Override
        public BaseFragment getItem(int position) {
            BaseFragment fragment = FragmentFactory.createAdminFragment(position);
            return fragment;
        }

        @Override
        public int getCount() {
            return CommentUtils.TAB_ADMIN_COUNT;
        }
    }

}
