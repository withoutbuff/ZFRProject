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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ImproveInfoActivity extends AppCompatActivity {

    private SharedPreferences.Editor editor;

    private EditText usernameEdit;

    private EditText passwordEdit;

    private EditText realNameEdit;

    private EditText phoneNumberEdit;

    private EditText areaEdit;

    private EditText idNumberEdit;

    private EditText emailEdit;
    private SharedPreferences pref;

    private RadioGroup sexRadioGroup;
    private RadioButton maleRadioButton;
    private RadioButton femaleRadioButton;
    int sex;
    int id;
    String username;
    String password;
    String realName;
    String area;
    String email;
    String idNumber;
    String phoneNumber;
    boolean isUpdateDo=false;
    private SqlHelper sh = new SqlHelper("192.168.2.102", "MassageChair", "sa", "123456");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_improve_info);

        id=getIntent().getIntExtra("id",id);

        usernameEdit = (EditText) findViewById(R.id.username);
        passwordEdit = (EditText) findViewById(R.id.password);
        realNameEdit = (EditText) findViewById(R.id.realName);
        phoneNumberEdit = (EditText) findViewById(R.id.phoneNumber);
        areaEdit = (EditText) findViewById(R.id.area);
        idNumberEdit = (EditText) findViewById(R.id.idNumber);
        emailEdit = (EditText) findViewById(R.id.email);
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
        //取得user表的id,已取得
//        int sex;
//        int phoneNumber;
//        int id;
//        String username;
//        String password;
//        String realName;
//        String area;
//        String email;
//        String idNumber;
        //初始化，获得框里的内容
            Button addUser=(Button)findViewById(R.id.update);
            addUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //判断都不为空

                    if((phoneNumberEdit.length()==11&
                            usernameEdit.length()!=0)&
                            (passwordEdit.length()>=6&
                            realNameEdit.length()!=0)&
                            (areaEdit.length()!=0&
                            emailEdit.length()!=0)&
                            idNumberEdit.length()==18) {
                        GetMsg();
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                // 通过Message类来传递结果值，先实例化
                                Message msg = Message.obtain();
                                // 下面分别是增删改查方法
                                msg.what = 1;
                                msg.obj = Update();
                                // 执行完以后，把msg传到handler，并且触发handler的响应方法
                                handler.sendMessage(msg);
                            }
                        });
                        // 进程开始，这行代码不要忘记
                        thread.start();
                    }
                    else {
                        Toast.makeText(ImproveInfoActivity.this, "有未填项，或填写错误",
                            Toast.LENGTH_SHORT).show();
                    }
                }
            });


        //获取实例
        sexRadioGroup=(RadioGroup)findViewById(R.id.sexRadioGroup);
        femaleRadioButton=(RadioButton)findViewById(R.id.femaleRadioButton);
        maleRadioButton=(RadioButton)findViewById(R.id.maleRadioButton);
        //设置监听
        sexRadioGroup.setOnCheckedChangeListener(new RadioGroupListener());

    }

    // Handler类用于接收Message的值，并且其父类有一个默认的handleMessage方法，用super。handleMessage()方法，传入msg，就能控制主线程的控件了
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            // 调用super的方法，传入handler对象接收到的msg对象
            super.handleMessage(msg);
            // 判断msg.what的值，有1和2，
            // 1表示操作是否成功，2表示查询时得到的json结果
            switch (msg.what) {
                case 1:
                    // 获得执行的结果，String字符串，返回操作是否成功提示
                    String rst = msg.obj.toString();
                    // 使用气泡提示
                    pref = PreferenceManager.getDefaultSharedPreferences(ImproveInfoActivity.this);
                    editor=pref.edit();
                    editor.clear();
                    editor.apply();
                    Intent intent=new Intent(getApplication(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(getApplicationContext(), rst, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(getApplicationContext(), "1更新失败！", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    // 用于接收EditText的输入值，并赋值到字符串
    public void GetMsg() {
        username= dealEditToString.dealStringNull(usernameEdit).trim();
        password=dealEditToString.dealStringNull(passwordEdit).trim();
        realName=dealEditToString.dealStringNull(realNameEdit).trim();
        phoneNumber=dealEditToString.dealStringNull(phoneNumberEdit);
        area=dealEditToString.dealStringNull(areaEdit).trim();
        email=dealEditToString.dealStringNull(emailEdit).trim();
        idNumber=dealEditToString.dealStringNull(idNumberEdit).trim();
    }

    public String Update() {
        String sql = "UPDATE [User] SET [password]=?" +
                ",[realname]=? " +
                ",[phonenumber]=? " +
                ",[area]=?" +
                ",[email]=?" +
                ",[idnumber]=?" +
                ",[sex]=?" +
                ",[username]=?"+
                "where [id]=?";
        GetMsg();
        //一个需要注意的地方，prepareStatement这个方法会把问号做处理，还有顺序一定要一样
        List<Object> params = new ArrayList<Object>();
        params.add(password);
        params.add(realName);
        params.add(phoneNumber);
        params.add(area);
        params.add(email);
        params.add(idNumber);
        params.add(sex);
        params.add(username);
        params.add(id);
        try {
            int count = sh.ExecuteNonQuery(sql, params);
            if (count == 1) {
                return "更新成功！";
            } else {
                return "2更新失败！";
            }
        } catch (Exception e) {
            // TODO: handle exception
            //System.out.println(e.getMessage());
            return "3更新失败！";
        }
    }



    //定义一个RadioGroup的OnCheckedChangeListener
    //RadioGroup  绑定的是RadioGroup.OnCheckedChangeListener
    //CheckBox    绑定的是CompoundButton.OnCheckedChangeListener 或者 view.OnClickListener

    class RadioGroupListener implements RadioGroup.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId==femaleRadioButton.getId()){
                sex=1;//1女
            }else if (checkedId==maleRadioButton.getId()){
                sex=0;//0男
            }
        }
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
