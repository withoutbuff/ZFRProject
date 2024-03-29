package com.example.zfrproject.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zfrproject.R;
import com.example.zfrproject.dataBase.SqlHelper;
import com.example.zfrproject.dataBase.User;
import com.example.zfrproject.deal.dealEditToString;
import com.example.zfrproject.login.LoginActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SurePasswordActivity extends AppCompatActivity {
    private Button sureButton;
    private EditText passwordEdit;
    private TextView usernameText;
    private String usernamepref;
    private String password;
    private int id;
    private SharedPreferences pref;
    private String DatabaseIp="192.168.2.102";
    private SqlHelper sh = new SqlHelper(DatabaseIp, "MassageChair", "sa", "123456");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sure_password);
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


        usernameText=(TextView)findViewById(R.id.username);
        pref = PreferenceManager.getDefaultSharedPreferences(SurePasswordActivity.this);
        usernamepref = pref.getString("username", "");
        Log.e("username",usernamepref);
        usernameText.setText(usernamepref);
        sureButton=(Button) findViewById(R.id.sure);
        passwordEdit=(EditText) findViewById(R.id.password);
        sureButton.setOnClickListener(clickEvent());
    }
    // clickEvent()
    private View.OnClickListener clickEvent() {
        // TODO Auto-generated method stub
        return new View.OnClickListener() {

            // clickEvent()方法体，参数是控件的基类View，必须加上final
            @Override
            public void onClick(final View view) {
                // TODO Auto-generated method stub
                // 如果不是btnClear，那就是增删改查的按钮，必须开启新的线程进行操作
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        // TODO Auto-generated method stub
                        // 通过Message类来传递结果值，先实例化
                        Message msg = Message.obtain();
                        // 下面分别是增删改查方法

                        // 设定msg的类型，用what属性，便于后面的代码区分返回的结果是什么类型
                        // 这里的1是指操作是否成功，String
                        // 这里的2是指查询的结果，String，用json的形式表示
                        String jsonResult = Select();
                        msg.what = 2;
                        msg.obj = jsonResult;
                        // 执行完以后，把msg传到handler，并且触发handler的响应方法
                        handler.sendMessage(msg);
                    }
                });
                // 进程开始，这行代码不要忘记
                thread.start();
            }
        };
    }



    // Handler类用于接收Message的值，并且其父类有一个默认的handleMessage方法，用super。handleMessage()方法，传入msg，就能控制主线程的控件了
    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        public void handleMessage(Message msg){
            // 调用super的方法，传入handler对象接收到的msg对象
            super.handleMessage(msg);
            // 判断msg.what的值，有1和2，
            // 1表示操作是否成功，2表示查询时得到的json结果
            switch (msg.what) {
                case 1:
                    // 获得执行的结果，String字符串，返回操作是否成功提示
                    String rst = msg.obj.toString();
                    // 使用气泡提示
                    Toast.makeText(getApplicationContext(), rst, Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    // 获得查询的json结果
                    String jsonResult = msg.obj.toString();
                    // 控制台输出，用于监视，与实际使用无关
                    // System.out.println(jsonResult);
                    Log.e("查询结果jsonResult",jsonResult);

                    // Gson类，用于json的转类型操作
                    Gson gson = new Gson();
                    // 定义查询到的结果类型，每一行记录映射为对象，本程序查询的是生产计划，所以一行记录表示一个品种的生产计划，用Plan类表示，用List收集全部Plan类
                    Type type = new TypeToken<List<User>>() {
                    }.getType();
                    // 使用gson的fromJson()方法，参数1：json结果，参数2：想要转哪一个类型
                    List<User> users = gson.fromJson(jsonResult, type);

                    // 由于要使用GridView表示，绑定数据时只能使用Map<K,T>的类型，并且多个记录时，要用List<Map<K,T>>
                    // 先实例化
                    // for循环从json转过来的List<Plan>
                    password=dealEditToString.dealStringNull(passwordEdit);
                    boolean isTrue=false;
                    if(password.length()!=0){
                        outLoop:
                        for (User user : users) {
                            //.trim()去除空格
                            if(user.password.trim().equals(password)){
                                //相等则立即跳出，并赋值true
                                id=user.id;
                                isTrue=true;
                                break outLoop;
                            }
                            else {
                                Log.e("密码",password+password.length());
                                Log.e("密码",user.password.trim()+user.password.trim().length());
                            }
                        }
                        if(!isTrue){
                            Toast.makeText(SurePasswordActivity.this, "密码不正确",
                                    Toast.LENGTH_SHORT).show();
                        }
                        //为true，执行后续操作
                        else {
                            Intent intent = new Intent(SurePasswordActivity.this, ImproveInfoActivity.class);
                            intent.putExtra("id",id);
                            startActivity(intent);
                            finish();
                        }
                    }
                    else {
                        Toast.makeText(SurePasswordActivity.this, "请输入密码",
                                Toast.LENGTH_SHORT).show();
                    }
            }
        }
    };

    // Select()方法，查询生产计划
    public String Select() {
        Log.e("进入查询方法id","进入");
        final String username=usernamepref;
        String sql = "SELECT [PASSWORD] AS password ,[ID] AS id FROM [User] where [USERNAME] = "+"'"+username+"'";
        Log.e("sql",sql);
        Log.e("username",username);
        String jsonResult = null;
        try {
            // sh.ExecuteQuery()，参数1：查询语句，参数2：查询用到的变量，用于本案例不需要参数，所以用空白的new
            // ArrayList<Object>()
            jsonResult = sh.ExecuteQuery(sql, new ArrayList<Object>());
            Log.e("jsonResult",jsonResult);
        } catch (Exception e) {
            // TODO: handle exception
            //System.out.println(e.getMessage());
            Log.e("e.getMessage()",e.getMessage());
            return null;
        }
        return jsonResult;
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
}
