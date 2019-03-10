package com.example.zfrproject.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zfrproject.R;
import com.example.zfrproject.dataBase.SqlHelper;
import com.example.zfrproject.deal.dealEditToString;
import com.example.zfrproject.user.UserActivity;
import com.example.zfrproject.dataBase.User;
import com.example.zfrproject.register.RegisterActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends BaseActivity {

    private SharedPreferences pref;

    private SharedPreferences.Editor editor;

    private EditText usernameEdit;

    private EditText passwordEdit;

    private Button login;
    private Button admin;
    private Button fettler;

    private CheckBox rememberPass;

    private String username = "";
    private String password = "";
    private String DatabaseIp="192.168.2.102";
    private SqlHelper sh = new SqlHelper(DatabaseIp, "MassageChair", "sa", "123456");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        usernameEdit = (EditText) findViewById(R.id.username);
        passwordEdit = (EditText) findViewById(R.id.password);
        rememberPass = (CheckBox) findViewById(R.id.remember_pass);

        login = (Button) findViewById(R.id.login);
        admin=(Button) findViewById(R.id.admin);
        fettler=(Button) findViewById(R.id.fetter);
        boolean isRemember = pref.getBoolean("remember_password", false);
        if (isRemember) {

            // 将账号和密码都设置到文本框中
            String usernamepref = pref.getString("username", "");
            String passwordpref = pref.getString("password", "");
            usernameEdit.setText(usernamepref);
            passwordEdit.setText(passwordpref);
            rememberPass.setChecked(true);
        }



        Button startRegisterActivity =findViewById(R.id.register);
        startRegisterActivity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);

            }
        });

        login.setOnClickListener(clickEvent());
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,AdminLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        fettler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,FettlerLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // clickEvent()
    private View.OnClickListener clickEvent() {
        return new View.OnClickListener() {

            // clickEvent()方法体，参数是控件的基类View，必须加上final
            @Override
            public void onClick(final View view) {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Message msg = Message.obtain();
                                String jsonResult = Select();
                                msg.what = 2;
                                msg.obj = jsonResult;
                            handler.sendMessage(msg);
                        }
                    });
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
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<User>>() {
                    }.getType();
                    List<User> users = gson.fromJson(jsonResult, type);
                    username=dealEditToString.dealStringNull(usernameEdit);
                    password=dealEditToString.dealStringNull(passwordEdit);
                    boolean isTrue=false;
                    if(username.length()!=0&password.length()!=0){
                        outLoop:
                        for (User user : users) {
                            if(user.username.trim().equals(username)&user.password.trim().equals(password)){
                                isTrue=true;
                                break outLoop;
                            }

                        }
                        if(!isTrue){
                            Toast.makeText(LoginActivity.this, "用户名或密码不正确",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            editor=pref.edit();
                            if(rememberPass.isChecked()){
                                editor.putBoolean("remember_password",true);
                                editor.putString("username",username);
                                editor.putString("password",password);
                            }
                            else {editor.clear();}
                            editor.apply();
                            Intent intent = new Intent(LoginActivity.this, UserActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "请输入用户名及密码",
                                Toast.LENGTH_SHORT).show();
                    }
        }
        }
    };

    // Select()方法，查询生产计划
    public String Select() {
        Log.e("进入查询方法","进入");

        String sql = "SELECT [USERNAME] AS username,[PASSWORD] AS password FROM [User]";
        String jsonResult = null;
        try {
            jsonResult = sh.ExecuteQuery(sql, new ArrayList<Object>());
            Log.e("jsonResult",jsonResult);
        } catch (Exception e) {
            Log.e("e.getMessage()",e.getMessage());
            return null;
        }
        return jsonResult;
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

