package com.example.zfrproject.user;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.example.zfrproject.R;
import com.example.zfrproject.dataBase.MassageChair;
import com.example.zfrproject.dataBase.SqlHelper;
import com.example.zfrproject.deal.dealEditToString;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;

import org.litepal.crud.DataSupport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ReportChairActivity extends AppCompatActivity {

private EditText chairNumEditText;
private Button reporterButton;
final private int  REQUEST_CODE_SCAN=0;
private Button scanButton;
private int number;
    private String DatabaseIp="192.168.2.102";
    private SqlHelper sh = new SqlHelper(DatabaseIp, "MassageChair", "sa", "123456");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_chair);
        chairNumEditText=findViewById(R.id.chair_num);
        reporterButton=(Button) findViewById(R.id.reporter);


        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(ReportChairActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.CAMERA);
        }
        if (ContextCompat.checkSelfPermission(ReportChairActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (!permissionList.isEmpty()) {
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(ReportChairActivity.this, permissions, 1);
        }


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

        reporterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //开启线程操作数据库
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
        });

        //扫二维码
        scanButton=findViewById(R.id.scan);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ReportChairActivity.this, CaptureActivity.class);
                /*ZxingConfig是配置类  可以设置是否显示底部布局，闪光灯，相册，是否播放提示音  震动等动能
                 * 也可以不传这个参数
                 * 不传的话  默认都为默认不震动  其他都为true
                 * */

                //ZxingConfig config = new ZxingConfig();
                //config.setShowbottomLayout(true);//底部布局（包括闪光灯和相册）
                //config.setPlayBeep(true);//是否播放提示音
                //config.setShake(true);//是否震动
                //config.setShowAlbum(true);//是否显示相册
                //config.setShowFlashLight(true);//是否显示闪光灯
                //intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
                startActivityForResult(intent, REQUEST_CODE_SCAN);

            }
        });
    }

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
                    Log.e("查询结果msg.obj.toString()",msg.obj.toString());

                    // Gson类，用于json的转类型操作
                    Gson gson = new Gson();
                    // 定义查询到的结果类型，每一行记录映射为对象，本程序查询的是生产计划，所以一行记录表示一个品种的生产计划，用Plan类表示，用List收集全部Plan类
                    Type type = new TypeToken<List<MassageChair>>() {
                    }.getType();
                    // 使用gson的fromJson()方法，参数1：json结果，参数2：想要转哪一个类型
                    List<MassageChair> massageChairs = gson.fromJson(jsonResult, type);
                    number=dealEditToString.dealIntNull(chairNumEditText);
                    boolean isTrue=false;
                    outLoop:
                    for(MassageChair massageChair:massageChairs){
                        if(massageChair.number==number){
                            isTrue=true;
                            break outLoop;
                        }

                    }
                    if(isTrue){
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
                                String jsonResult = Update();
                                msg.what = 1;
                                msg.obj = jsonResult;
                                // 执行完以后，把msg传到handler，并且触发handler的响应方法
                                handler1.sendMessage(msg);
                            }
                        });
                        // 进程开始，这行代码不要忘记
                        thread.start();
                    }
                    else {
                        //编号错误
                        Toast.makeText(getApplicationContext(), "编号错误", Toast.LENGTH_SHORT).show();
                    }

            }
        }
    };
    @SuppressLint("HandlerLeak")
    Handler handler1=new Handler(){
        public void handleMessage(Message msg){
            // 调用super的方法，传入handler对象接收到的msg对象
            super.handleMessage(msg);
            // 判断msg.what的值，有1和2，
            // 1表示操作是否成功，2表示查询时得到的json结果
                    // 获得执行的结果，String字符串，返回操作是否成功提示
                    String rst = msg.obj.toString();
                    // 使用气泡提示
                    Toast.makeText(getApplicationContext(), rst, Toast.LENGTH_SHORT).show();
        }
    };
    public String Update() {
        String sql = "UPDATE [MassageChair] SET [MC_state]=? where [number]=?";
        // params用于存放变量参数，即sql中的“？”
        List<Object> params = new ArrayList<Object>();
        params.add(2);
        params.add(number);
        try {
            int count = sh.ExecuteNonQuery(sql, params);
            if (count == 1) {
                return "报障成功！";
            } else {
                return "操作失败！";
            }
        } catch (Exception e) {
            // TODO: handle exception
            //System.out.println(e.getMessage());
            Log.e("e.getMessage()",e.getMessage());
            return "操作失败！";
        }
    }

    // Select()方法，查询生产计划
    public String Select() {
        Log.e("进入查询方法","进入");

//需要查询，x,y，满足MC状态，未使用,属于点击的MCL的，这个数从前面获取,已经获取intentMCL_id，放到哪，查询语句的条件里
        String sql = "SELECT [number] AS number FROM [MassageChair] where [MC_state]=0";
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {

                String content = data.getStringExtra(Constant.CODED_CONTENT);
                chairNumEditText.setText(content);//("扫描结果为：" + content);
            }
        }
    }

}
