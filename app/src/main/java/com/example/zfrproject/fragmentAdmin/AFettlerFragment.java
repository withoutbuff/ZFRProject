package com.example.zfrproject.fragmentAdmin;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zfrproject.R;
import com.example.zfrproject.dataBase.Admin;
import com.example.zfrproject.dataBase.Fettler;
import com.example.zfrproject.dataBase.SqlHelper;
import com.example.zfrproject.fragmentUser.BaseFragment;
import com.example.zfrproject.user.ImproveInfoActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.litepal.crud.Many2ManyAnalyzer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AFettlerFragment extends BaseFragment{

    // 定义控件
    private EditText adminIdEdit;
    private EditText adminUsernameEdit;
    private EditText adminPasswordEdit;
    private EditText adminRealNameEdit;
    private EditText adminPhoneNumberEdit;
    private EditText adminAreaEdit;
    private EditText adminIdNumberEdit;
    private EditText adminLocationXEdit;
    private EditText adminLocationYEdit;
    private EditText adminJobNumberEdit;
    private RadioGroup sexRadioGroup;
    private RadioButton maleRadioButton;
    private RadioButton femaleRadioButton;

    private int sexRadioButton;

    private Button btnInsert;
    private Button btnUpdate;
    private Button btnDelete;
    private Button btnClear;
    private Button btnSelect;
    private GridView queryAdminGridView;
    // 定义变量
    private String id = "";
    private String username = "";
    private String password = "";
    private String realName = "";
    private String phoneNumber = "";
    private String area = "";
    private String idNumber = "";
    private String locationX = "";
    private String locationY = "";
    private String jobNumber = "";
    // SQL帮助类，参数用于设置连接字符串，参数1：主机ip，参数2：数据库名，参数3：用户名，参数4：用户密码
    private SqlHelper sh = new SqlHelper("192.168.2.102", "MassageChair", "sa", "123456");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        mContent = getContext();
        View view=inflater.inflate(R.layout.fragment_a_fettler,container,false);
        adminIdEdit=(EditText) view.findViewById(R.id.id);
        adminAreaEdit=(EditText) view.findViewById(R.id.area);
        adminIdNumberEdit=(EditText) view.findViewById(R.id.idNumber);
        adminPasswordEdit=(EditText) view.findViewById(R.id.password);
        adminPhoneNumberEdit=(EditText) view.findViewById(R.id.phoneNumber);
        adminUsernameEdit=(EditText) view.findViewById(R.id.username);
        adminRealNameEdit=(EditText) view.findViewById(R.id.realName);
        adminLocationXEdit=(EditText) view.findViewById(R.id.locationX);
        adminLocationYEdit=(EditText) view.findViewById(R.id.locationY);
        adminJobNumberEdit=(EditText) view.findViewById(R.id.jobNumber);

        btnInsert = (Button) view.findViewById(R.id.btnInsert);
        btnUpdate = (Button) view.findViewById(R.id.btnUpdate);
        btnDelete = (Button) view.findViewById(R.id.btnDelete);
        btnClear = (Button) view.findViewById(R.id.btnClear);
        btnSelect = (Button) view.findViewById(R.id.btnSelect);
        queryAdminGridView = (GridView) view.findViewById(R.id.queryAdminGridView);

        //获取实例
        sexRadioGroup=(RadioGroup)view.findViewById(R.id.sexRadioGroup);
        femaleRadioButton=(RadioButton)view.findViewById(R.id.femaleRadioButton);
        maleRadioButton=(RadioButton)view.findViewById(R.id.maleRadioButton);
        //设置监听
        sexRadioGroup.setOnCheckedChangeListener(new AFettlerFragment.RadioGroupListener());

        btnInsert.setOnClickListener(clickEvent());
        btnUpdate.setOnClickListener(clickEvent());
        btnDelete.setOnClickListener(clickEvent());
        btnClear.setOnClickListener(clickEvent());
        btnSelect.setOnClickListener(clickEvent());

        return view;
    }

    // clickEvent()
    private View.OnClickListener clickEvent() {
        // TODO Auto-generated method stub
        return new View.OnClickListener() {

            // clickEvent()方法体，参数是控件的基类View，必须加上final
            @Override
            public void onClick(final View view) {
                // TODO Auto-generated method stub
                // 用view来判断按下的哪个按钮
                if (view == btnClear) {
                    // ClearEdit()方法用于复位控件
                    ClearEdit();
                } else {
                    // 如果不是btnClear，那就是增删改查的按钮，必须开启新的线程进行操作
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            // 通过Message类来传递结果值，先实例化
                            Message msg = Message.obtain();
                            // 下面分别是增删改查方法
                            if (view == btnInsert) {
                                // 设定msg的类型，用what属性，便于后面的代码区分返回的结果是什么类型
                                // 这里的1是指操作是否成功，String
                                // 这里的2是指查询的结果，String，用json的形式表示
                                msg.what = 1;
                                msg.obj = Insert();
                            } else if (view == btnUpdate) {
                                msg.what = 1;
                                msg.obj = Update();
                            } else if (view == btnDelete) {
                                msg.what = 1;
                                msg.obj = Delete();
                            } else if (view == btnSelect) {
                                String jsonResult = Select();
                                msg.what = 2;
                                msg.obj = jsonResult;
                            } else {

                            }
                            // 执行完以后，把msg传到handler，并且触发handler的响应方法
                            handler.sendMessage(msg);
                        }
                    });
                    // 进程开始，这行代码不要忘记
                    thread.start();
                }
            }
        };
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
                    Toast.makeText(mContent, rst, Toast.LENGTH_SHORT).show();
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
                    Type type = new TypeToken<List<Fettler>>() {
                    }.getType();
                    // 使用gson的fromJson()方法，参数1：json结果，参数2：想要转哪一个类型
                    List<Fettler> fettlers = gson.fromJson(jsonResult, type);

                    // 由于要使用GridView表示，绑定数据时只能使用Map<K,T>的类型，并且多个记录时，要用List<Map<K,T>>
                    // 先实例化
                    List<Map<String, String>> mFettler = new ArrayList<Map<String, String>>();
                    // 实例化一个title，是GridView的列头
                    Map<String, String> title = new HashMap<String, String>();

                    title.put("id", "id");
                    title.put("username", "用户名");
                    title.put("password", "密码");
                    title.put("realName", "真实姓名");
                    title.put("phoneNumber", "手机号码");
                    title.put("area", "地区");
                    title.put("idNumber", "身份证号");
                    title.put("sex", "性别");
                    title.put("locationX", "坐标X");
                    title.put("locationY", "坐标Y");
                    title.put("jobNumber", "工号");
                    // 首先把表头追加到List<Map<String, String>>
                    mFettler.add(title);

                    // for循环从json转过来的List<Plan>
                    for (Fettler fettler : fettlers){
                        // 实例化用于接收plan的HashMap<K,T>类
                        HashMap<String, String> hFettlers = new HashMap<String, String>();
                        // 使用put()方法把数值加入到HashMap<K,T>，参数1：键，参数2：值

                        hFettlers.put("id", fettler.id+"");
                        Log.e("id",fettler.id+"");

                        hFettlers.put("password", fettler.password);
                        Log.e("password",fettler.password);

                        hFettlers.put("username", fettler.username);
                        Log.e("username",fettler.username);

                        hFettlers.put("realName", fettler.realName);
                        Log.e("realName",fettler.realName);

                        hFettlers.put("phoneNumber", fettler.phoneNumber);
                        Log.e("phoneNumber",fettler.phoneNumber);

                        hFettlers.put("area", fettler.area);
                        Log.e("area",fettler.area);

                        hFettlers.put("idNumber", fettler.idNumber);
                        Log.e("idNumber",fettler.idNumber+"");

                        hFettlers.put("sex", fettler.sex+"");
                        Log.e("sex",fettler.sex+"");

                        hFettlers.put("locationX", fettler.locationX+"");
                        Log.e("locationX",fettler.locationX+"");

                        hFettlers.put("locationY", fettler.locationY+"");
                        Log.e("locationY",fettler.locationY+"");

                        hFettlers.put("jobNumber", fettler.jobNumber);
                        Log.e("jobNumber",fettler.jobNumber);
                        // 把HashMap加入到List<Map>中
                        mFettler.add(hFettlers);
                    }

                    // SimpleAdapter是GridView的适配器，参数1：上下文内容，参数2：List<Map<K,T>>对象，参数3：GridView的布局文件，指每一个item的布局，需要在res/layout中创建xml，
                    // 参数4：String数组，指每一列要绑定到Map中的值，数组中的值就是上文“hmPlans.put("id",
                    // plan.id);”的键"id"
                    // 参数5：列头的显示文件，存放在res/values/strings.xml
                    SimpleAdapter sa = new SimpleAdapter(mContent, mFettler, R.layout.faf_item,
                            new String[] { "id", "username", "password" ,"realName","phoneNumber","area","idNumber",
                                    "sex","locationX","locationY","jobNumber"}, new int[] { R.id.header1, R.id.header2, R.id.header3
                            , R.id.header4, R.id.header5, R.id.header6 , R.id.header7, R.id.header8, R.id.header9 , R.id.header10, R.id.header11});
                    // 把SimpleAdapter绑定到GridView
                    Log.e("mFettler",mFettler.toString());
                    queryAdminGridView.setAdapter(sa);
                    // 气泡提示
                    Toast.makeText(mContent, "读取成功！", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(mContent, "操作失败！", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    // 用于接收EditText的输入值，并赋值到字符串
    public void GetMsg() {
        id = adminIdEdit.getText().toString().trim();
        username = adminUsernameEdit.getText().toString().trim();
        password = adminPasswordEdit.getText().toString().trim();
        realName = adminRealNameEdit.getText().toString().trim();
        phoneNumber = adminPhoneNumberEdit.getText().toString().trim();
        area = adminAreaEdit.getText().toString().trim();
        idNumber = adminIdNumberEdit.getText().toString().trim();
        locationX= adminLocationXEdit.getText().toString().trim();
        locationY= adminLocationYEdit.getText().toString().trim();
        jobNumber= adminJobNumberEdit.getText().toString().trim();
    }

    // Insert()方法，通过判断受影响行数，返回“添加成功”或“操作失败”
    public String Insert() {
        String sql = "INSERT INTO [Fettler]([username],[password],[realName],[phoneNumber]," +
                "[area],[idNumber],[sex],[locationX],[locationY],[jobNumber]) VALUES (?,?,?,?,?,?,?,?,?,?)";
        GetMsg();
        List<Object> params = new ArrayList<Object>();
        params.add(username);
        params.add(password);
        params.add(realName);
        params.add(phoneNumber);
        params.add(area);
        params.add(idNumber);
        params.add(sexRadioButton);
        params.add(locationX);
        params.add(locationY);
        params.add(jobNumber);
        try {
            int count = sh.ExecuteNonQuery(sql, params);
            if (count == 1) {
                return "添加成功！";
            } else {
                return "2操作失败！";
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            //System.out.println(e.getMessage());
            Log.e("e.getMessage()",e.getMessage());
            return "3操作失败！";
        }
    }

    public String Update() {
        String sql = "UPDATE [Fettler] SET [username]=?,[password]=?,[realName]=?,[phoneNumber]=?," +
                "[area]=?,[idNumber]=?,[sex]=?,[locationX]=?,[locationY]=?,[jobNumber]=? where [ID]=?";
        GetMsg();
        // params用于存放变量参数，即sql中的“？”
        List<Object> params = new ArrayList<Object>();
        params.add(username);
        params.add(password);
        params.add(realName);
        params.add(phoneNumber);
        params.add(area);
        params.add(idNumber);
        params.add(sexRadioButton);
        params.add(locationX);
        params.add(locationY);
        params.add(jobNumber);
        params.add(id);
        try {
            int count = sh.ExecuteNonQuery(sql, params);
            if (count == 1) {
                return "更新成功！";
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

    public String Delete() {
        String sql = "DELETE FROM [Fettler] where [ID]=?";
        GetMsg();
        List<Object> params = new ArrayList<Object>();
        params.add(id);
        try {
            int count = sh.ExecuteNonQuery(sql, params);
            if (count == 1) {
                return "删除成功！";
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
        String sql = "SELECT [username] AS username,[password] AS password" +
                ",[realName] AS realName,[phoneNumber] AS phoneNumber" +
                ",[area] AS area,[idNumber] AS idNumber" +
                ",[sex] AS sex,[locationX] AS locationX" +
                ",[locationY] AS locationY,[id] AS id ,[jobNumber] AS jobNumber FROM [Fettler]";
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

    // 界面复位，清空显示
    public void ClearEdit() {
        //清空文本输入框
        adminIdEdit.setText("");
        adminUsernameEdit.setText("");
        adminPasswordEdit.setText("");
        adminRealNameEdit.setText("");
        adminPhoneNumberEdit.setText("");
        adminIdNumberEdit.setText("");
        adminAreaEdit.setText("");
        adminLocationXEdit.setText("");
        adminLocationYEdit.setText("");
        adminJobNumberEdit.setText("");
        //etId获得焦点
        adminIdEdit.setFocusable(true);
        adminIdEdit.setFocusableInTouchMode(true);
        adminIdEdit.requestFocus();
        adminIdEdit.requestFocusFromTouch();
        //清空GridView的绑定值
        queryAdminGridView.setAdapter(null);
    }

    @Override
    protected void loadData() {
        //Toast.makeText(mContent,"Fragment头条加载数据",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected View initView() {
        TextView mView = new TextView(mContent);
        return mView;
    }

    class RadioGroupListener implements RadioGroup.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId==femaleRadioButton.getId()){
                sexRadioButton=1;//1女
            }else if (checkedId==maleRadioButton.getId()){
                sexRadioButton=0;//0男
            }
        }
    }
}
