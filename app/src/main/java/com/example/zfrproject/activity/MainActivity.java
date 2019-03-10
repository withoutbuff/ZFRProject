package com.example.zfrproject.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.support.v7.widget.Toolbar;
import com.example.zfrproject.R;
import com.example.zfrproject.deal.hasUnamePword;
import com.example.zfrproject.login.LoginActivity;
import com.example.zfrproject.user.UserActivity;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    public static final String TAG="MainActivity";
    private final int SKIP_DELAY_TIME = 1;//延长显示时间ms，判断用户登陆已经延迟2000
    private static final int GO_HOME = 0;//去主页
    private static final int GO_LOGIN = 1;//去登录页
    /**
     * 跳转判断
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GO_HOME://去主页
                    Intent intent = new Intent(MainActivity.this, UserActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case GO_LOGIN://去登录页
                    Intent intent2 = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent2);
                    finish();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate");
        setContentView(R.layout.activity_main);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Context mContext=getApplicationContext();
        Timer time = new Timer();
        TimerTask task = new TimerTask(){
            @Override
            public void run() {
                int i=0;
                if(i==0){
                }
                i++;
                if (hasUnamePword.getInstance().hasUserInfo(mContext))//自动登录判断，SharePrefences中有数据，则跳转到主页，没数据则跳转到登录页
                {
                    mHandler.sendEmptyMessageDelayed(GO_HOME, 300);
                } else {
                    mHandler.sendEmptyMessageAtTime(GO_LOGIN, 1);
                }
            }
        };
        time.schedule(task, SKIP_DELAY_TIME);

    }

}
