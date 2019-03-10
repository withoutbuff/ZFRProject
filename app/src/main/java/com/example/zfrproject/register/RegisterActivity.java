package com.example.zfrproject.register;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zfrproject.R;
import com.example.zfrproject.dataBase.SqlHelper;
import com.example.zfrproject.dataBase.User;
import com.example.zfrproject.login.LoginActivity;
import com.example.zfrproject.deal.dealEditToString;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class RegisterActivity extends AppCompatActivity {

    private EditText usernameEdit;

    private EditText passwordEdit;

    private EditText passwordAgainEdit;
    private String username = "";
    private String password = "";
    private String DatabaseIp="192.168.2.102";
    private SqlHelper sh = new SqlHelper(DatabaseIp, "MassageChair", "sa", "123456");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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

        usernameEdit = (EditText) findViewById(R.id.username);
        passwordEdit = (EditText) findViewById(R.id.password);
        passwordAgainEdit= (EditText) findViewById(R.id.passwordAgain);


        Button startLoginActivity =findViewById(R.id.login);
        startLoginActivity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final String username=dealEditToString.dealStringNull(usernameEdit);
                Log.d("用户名", "username="+username);
                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Button register =findViewById(R.id.register);
        //register.setOnClickListener(clickEvent());
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Thread thread = new Thread(new Runnable() {

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
                        handler.getMessageName(msg);

                    }
                });
                //进程开始，这行代码不要忘记
                thread.start();

            }
        });


    }

    @SuppressLint("HandlerLeak")
    Handler handler2=new Handler(){
        public void handleMessage(Message msg1){
            super.handleMessage(msg1);
            // 获得执行的结果，String字符串，返回操作是否成功提示
            String rst = msg1.obj.toString();
            // 使用气泡提示
            Toast.makeText(getApplicationContext(), rst, Toast.LENGTH_SHORT).show();

        }
    };

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
                    //判断密码一致

                    username=dealEditToString.dealStringNull(usernameEdit);
                    password=dealEditToString.dealStringNull(passwordEdit);
                    Log.e("赋值username","");
                    boolean isInsertDo=false;
                    outLoop:
                    for (User user : users) {
                        //.trim()去除空格
                        Log.e("开始循环","");


                        //判断用户名重复
                        if(username.length()!=0){
                            if(user.username.trim().equals(username)){
                                isInsertDo=false;
                                Toast.makeText(RegisterActivity.this, "用户名已占用",
                                        Toast.LENGTH_SHORT).show();
                                break outLoop;
                            }
                            else {
                                isInsertDo=true;
                            }
                        }

                    }
                    if(username.length()==0){
                        Toast.makeText(RegisterActivity.this, "未输入用户名",
                                Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if(password.length()==0){
                            Toast.makeText(RegisterActivity.this, "未输入密码",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if(isInsertDo){//已占用不必再做以下判断
                                //先是不小于6位，再是一致
                                if(password.length()<6){
                                    Toast.makeText(RegisterActivity.this, "密码长度至少6位",
                                            Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    isInsertDo=true;
                                    if(dealEditToString.surePassword(passwordEdit,passwordAgainEdit)){
                                        Thread thread1 = new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Message msg1 = Message.obtain();
                                                Log.e("isInsertDo", "true");
                                                msg1.what = 1;
                                                msg1.obj = Insert();
                                                // 执行完以后，把msg传到handler，并且触发handler的响应方法
                                                handler2.sendMessage(msg1);
                                            }
                                        });
                                        thread1.start();
                                    }
                                    else {
                                        isInsertDo=false;
                                        Toast.makeText(RegisterActivity.this, "密码不一致",
                                                Toast.LENGTH_SHORT).show();
                                        isInsertDo=false;
                                    }
                                }
                            }
                        }
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

    // Insert()方法，通过判断受影响行数，返回“添加成功”或“操作失败”
    public String Insert() {
        username=dealEditToString.dealStringNull(usernameEdit);
        password=dealEditToString.dealStringNull(passwordEdit);
        String sql = "INSERT INTO [User]([USERNAME],[PASSWORD]) VALUES (?,?)";
        List<Object> params = new ArrayList<Object>();
        params.add(username);
        Log.e("Insert_username",username);
        params.add(password);
        Log.e("Insert_password",password);
        try {
            int count = sh.ExecuteNonQuery(sql, params);
            if (count == 1) {
                return "注册成功！";
            } else {
                return "注册失败！";
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            //System.out.println(e.getMessage());
            Log.e("e.getMessage()",e.getMessage());
            return "注册失败！";
        }
    }

}
