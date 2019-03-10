package com.example.zfrproject.user;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.zfrproject.R;
import com.example.zfrproject.dataBase.SqlHelper;
import com.example.zfrproject.dataBase.Statement;
import com.example.zfrproject.dataBase.User;
import com.example.zfrproject.deal.dealEditToString;
import com.example.zfrproject.login.LoginActivity;
import com.example.zfrproject.register.RegisterActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;

import org.litepal.crud.DataSupport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RechargeActivity extends AppCompatActivity {
    private TextView balanceTextView;
    private TextView usernameTextView;
    private EditText rechargeEditText;
    private Button rechargeButton;
    private Button scanButton;
    private String recharge;
    private int account;
    private int balance;
    private int number;
    final private int  REQUEST_CODE_SCAN=0;
    private SharedPreferences pref;
    private String usernamepref;
    String content="0";
    private String DatabaseIp="192.168.2.102";
    private SqlHelper sh = new SqlHelper(DatabaseIp, "MassageChair", "sa", "123456");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);

        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(RechargeActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.CAMERA);
        }
        if (ContextCompat.checkSelfPermission(RechargeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (!permissionList.isEmpty()) {
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(RechargeActivity.this, permissions, 1);
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
        Intent intent1 = getIntent();
        balanceTextView=(TextView)findViewById(R.id.balanceTextView);
        usernameTextView=(TextView)findViewById(R.id.username);
        rechargeEditText=(EditText)findViewById(R.id.rechargeEditText);
        rechargeButton=(Button)findViewById(R.id.rechargeButton);
        scanButton=(Button)findViewById(R.id.scan);
//从SharedPreferences读取值
        pref = PreferenceManager.getDefaultSharedPreferences(RechargeActivity.this);
        usernamepref = pref.getString("username", "");
        Log.e("username",usernamepref);
        usernameTextView.setText(usernamepref);

        rechargeButton.setOnClickListener(clickEvent());

//查询余额
//        List<User> users=DataSupport.select("username","balance")
//                .where("username = ?",usernameCache)
//                .find(User.class);
//        int intBalance=0;
//        outLoop:
//        for (User userQuery : users) {
//            intBalance=userQuery.getBalance();
//                Log.e("Balance",""+userQuery.getBalance());
//                break outLoop;
//        }
//        String StingBalance=""+intBalance;
//        balanceTextView.setText(StingBalance);

//查询充值卡号
//        final int finalIntBalance=intBalance;
//        final int statementId=Integer.parseInt(dealEditToString.dealStringNull(rechargeEditText));
//        Log.e("scontent",content);
//        Log.e("statementId",""+statementId);
//        Log.e("icontent",""+Integer.parseInt(content));
//        rechargeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                List<Statement> statements=DataSupport.select("id","amount","state")
//                        .where("id = ?",""+statementId)
//                        .where("state = ?","未使用")
//                        .find(Statement.class);
//                Log.e("statementId",""+statementId);
//                int increaBalance=0;
//
//                boolean doInsert = false;
//                outLoop:
//                for (Statement statementQuery : statements) {
//                    increaBalance=statementQuery.getAmount();
//                    Log.e("Balance",""+statementQuery.getAmount());
//                    int balance=statementQuery.getAmount()+finalIntBalance;
//                    //更新用户
//                    User user=new User();
//                    user.setBalance(balance);
//                    user.updateAll("username = ?",usernameCache);
//
//                    //更新充值卡
//                    Statement statement=new Statement();
//                    statement.setState("已使用");
//                    statement.updateAll("id = ?",""+statementId);
//
//                    Toast.makeText(RechargeActivity.this, "充值成功",
//                            Toast.LENGTH_SHORT).show();
//
//
//                    break outLoop;
//                }
//                Toast.makeText(RechargeActivity.this, "充值失败，请验证订单号",
//                        Toast.LENGTH_SHORT).show();
//
//            }
//        });

        scanButton=findViewById(R.id.scan);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RechargeActivity.this, CaptureActivity.class);
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
//查询余额
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {

                // TODO Auto-generated method stub
                // 通过Message类来传递结果值，先实例化
                Message msg1 = Message.obtain();
                // 下面分别是增删改查方法

                // 设定msg的类型，用what属性，便于后面的代码区分返回的结果是什么类型
                // 这里的1是指操作是否成功，String
                // 这里的2是指查询的结果，String，用json的形式表示
                String jsonResult1 = Select1();
                msg1.what = 2;
                msg1.obj = jsonResult1;
                // 执行完以后，把msg传到handler，并且触发handler的响应方法
                handler1.sendMessage(msg1);
            }
        });
        // 进程开始，这行代码不要忘记
        thread1.start();
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
                //查询卡号
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
                    Type type = new TypeToken<List<Statement>>() {
                    }.getType();
                    // 使用gson的fromJson()方法，参数1：json结果，参数2：想要转哪一个类型
                    List<Statement> statements = gson.fromJson(jsonResult, type);

                    // 由于要使用GridView表示，绑定数据时只能使用Map<K,T>的类型，并且多个记录时，要用List<Map<K,T>>
                    // 先实例化
                    // for循环从json转过来的List<Plan>
                    boolean isTrue=false;
                    recharge=dealEditToString.dealStringNull(rechargeEditText);
                    Log.e("recharge",recharge);
                        outLoop:
                        for (Statement statement : statements) {
                            //.trim()去除空格
                            if(statement.number.equals(recharge)){
                                //相等则立即跳出，并赋值true,卡号正确
                                isTrue=true;
                                account=statement.amount;
                                number=Integer.parseInt(statement.number);
                                break outLoop;
                            }
                            else {//遍历输出不正确的值，仅用于debug
                                Log.e("number",statement.number);
                                Log.e("recharge",recharge);

                            }
                        }
                        if(!isTrue){//为false，弹出卡号不正确
                            Toast.makeText(RechargeActivity.this, "输入卡号不正确",
                                    Toast.LENGTH_SHORT).show();
                        }
                        //为true，执行后续操作：更新卡状态，查出余额，加上前面的余额，更新余额
                        else {
                            //更新User
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
                                    handler3.sendMessage(msg);
                                }
                            });
                            // 进程开始，这行代码不要忘记
                            thread.start();
                            //更新Statement
                            Thread thread1 = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    // TODO Auto-generated method stub
                                    // 通过Message类来传递结果值，先实例化
                                    Message msg = Message.obtain();
                                    // 下面分别是增删改查方法
                                    msg.what = 1;
                                    msg.obj = Update1();
                                    // 执行完以后，把msg传到handler，并且触发handler的响应方法
                                    handler2.sendMessage(msg);
                                }
                            });
                            // 进程开始，这行代码不要忘记
                            thread1.start();
                            int i=balance+account;
                            Toast.makeText(RechargeActivity.this, "充值成功，当前余额为："+i,
                                    Toast.LENGTH_SHORT).show();
                            //充值完点击返回按钮，避免数据库被被污染
                            onBackPressed();
                        }
            }
        }
    };
    // Handler类用于接收Message的值，并且其父类有一个默认的handleMessage方法，用super。handleMessage()方法，传入msg，就能控制主线程的控件了
    @SuppressLint("HandlerLeak")
    Handler handler1=new Handler(){
        public void handleMessage(Message msg){
            // 调用super的方法，传入handler对象接收到的msg对象
            super.handleMessage(msg);
            // 判断msg.what的值，有1和2，
            // 1表示操作是否成功，2表示查询时得到的json结果
            switch (msg.what) {
                case 1:
                    // 获得执行的结果，String字符串，返回操作是否成功提示
                    String rst1 = msg.obj.toString();
                    // 使用气泡提示
                    Toast.makeText(getApplicationContext(), rst1, Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    // 获得查询的json结果
                    String jsonResult1 = msg.obj.toString();
                    // 控制台输出，用于监视，与实际使用无关
                    // System.out.println(jsonResult);
                    Log.e("查询结果1jsonResult",jsonResult1);

                    // Gson类，用于json的转类型操作
                    Gson gson = new Gson();
                    // 定义查询到的结果类型，每一行记录映射为对象，本程序查询的是生产计划，所以一行记录表示一个品种的生产计划，用Plan类表示，用List收集全部Plan类
                    Type type = new TypeToken<List<User>>() {
                    }.getType();
                    // 使用gson的fromJson()方法，参数1：json结果，参数2：想要转哪一个类型
                    List<User> users = gson.fromJson(jsonResult1, type);

                    // 由于要使用GridView表示，绑定数据时只能使用Map<K,T>的类型，并且多个记录时，要用List<Map<K,T>>
                    // 先实例化
                    // for循环从json转过来的List<Plan>

                    for (User user : users) {
                        //.trim()去除空格
                        balance=user.balance;
                        balanceTextView.setText(balance+"");
                        Log.e("balance",""+balance);
                    }
            }
        }
    };
    // Handler类用于接收Message的值，并且其父类有一个默认的handleMessage方法，用super。handleMessage()方法，传入msg，就能控制主线程的控件了
    @SuppressLint("HandlerLeak")
    private Handler handler2 = new Handler() {
        public void handleMessage(Message msg) {
            // 调用super的方法，传入handler对象接收到的msg对象
            super.handleMessage(msg);
            // 判断msg.what的值，有1和2，
            // 1表示操作是否成功，2表示查询时得到的json结果
            switch (msg.what) {
                case 1:
                    // 获得执行的结果，String字符串，返回操作是否成功提示
                    String rst = msg.obj.toString();
                    //Toast.makeText(getApplicationContext(), rst, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(getApplicationContext(), "S1更新失败！", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    @SuppressLint("HandlerLeak")
    private Handler handler3 = new Handler() {
        public void handleMessage(Message msg) {
            // 调用super的方法，传入handler对象接收到的msg对象
            super.handleMessage(msg);
            // 判断msg.what的值，有1和2，
            // 1表示操作是否成功，2表示查询时得到的json结果
            switch (msg.what) {
                case 1:
                    // 获得执行的结果，String字符串，返回操作是否成功提示
                    String rst = msg.obj.toString();
                    //Toast.makeText(getApplicationContext(), rst, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(getApplicationContext(), "u1更新失败！", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    // Select()方法，查询卡号
    public String Select() {
        Log.e("进入查询方法","进入");

        String sql = "SELECT [amount] AS amount, [number] AS number FROM [Statement] WHERE [state]=0";
        String jsonResult = null;
        try {
            // sh.ExecuteQuery()，参数1：查询语句，参数2：查询用到的变量，用于本案例不需要参数，所以用空白的new
            // ArrayList<Object>()
            jsonResult = sh.ExecuteQuery(sql, new ArrayList<Object>());
        } catch (Exception e) {
            // TODO: handle exception
            //System.out.println(e.getMessage());
            Log.e("e.getMessage()",e.getMessage());
            return null;
        }
        return jsonResult;
    }
    // Select()方法，查询余额
    public String Select1() {
        Log.e("进入查询方法1","进入");
        String sql1 = "SELECT [balance] AS balance  FROM [User] where [USERNAME] = "+"'"+usernamepref+"'";
        String jsonResult1 = null;
        try {
            // sh.ExecuteQuery()，参数1：查询语句，参数2：查询用到的变量，用于本案例不需要参数，所以用空白的new
            // ArrayList<Object>()
            jsonResult1 = sh.ExecuteQuery(sql1, new ArrayList<Object>());
            Log.e("jsonResult1",jsonResult1);
        } catch (Exception e) {
            // TODO: handle exception
            //System.out.println(e.getMessage());
            Log.e("e.getMessage()",e.getMessage());
            return null;
        }
        return jsonResult1;
    }

    public String Update() {
        String sql = "UPDATE [User] SET [balance]=?" +
                "where [username]=?";
        //一个需要注意的地方，prepareStatement这个方法会把问号做处理，还有顺序一定要一样
        List<Object> params = new ArrayList<Object>();
        int i=balance+account;
        params.add(i);
        Log.e("i",i+"");
        params.add(usernamepref);
        Log.e("username",usernamepref);
        try {
            int count = sh.ExecuteNonQuery(sql, params);
            if (count == 1) {
                return "u更新成功！";
            } else {
                return "u2更新失败！";
            }
        } catch (Exception e) {
            // TODO: handle exception
            //System.out.println(e.getMessage());
            return "u3更新失败！";
        }
    }
    public String Update1() {
        String sql = "UPDATE [Statement] SET [state]=?" +
                "where [number]=?";
        //一个需要注意的地方，prepareStatement这个方法会把问号做处理，还有顺序一定要一样
        List<Object> params = new ArrayList<Object>();
        params.add(1);
        params.add(number);
        try {
            int count = sh.ExecuteNonQuery(sql, params);
            if (count == 1) {
                return "s更新成功！";
            } else {
                return "s2更新失败！";
            }
        } catch (Exception e) {
            // TODO: handle exception
            //System.out.println(e.getMessage());
            return "3s更新失败！";
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




    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                content=recharge;
                content = data.getStringExtra(Constant.CODED_CONTENT);
                rechargeEditText.setText(content);//("扫描结果为：" + content);
            }
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
