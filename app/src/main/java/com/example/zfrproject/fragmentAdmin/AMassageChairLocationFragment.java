package com.example.zfrproject.fragmentAdmin;


import android.annotation.SuppressLint;
import android.location.Location;
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
import com.example.zfrproject.dataBase.MassageChair;
import com.example.zfrproject.dataBase.MassageChairLocation;
import com.example.zfrproject.dataBase.QueryChairPoint;
import com.example.zfrproject.dataBase.SqlHelper;
import com.example.zfrproject.deal.dealEditToString;
import com.example.zfrproject.fragmentUser.BaseFragment;
import com.example.zfrproject.user.ImproveInfoActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.litepal.crud.DataSupport;
import org.litepal.crud.Many2ManyAnalyzer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AMassageChairLocationFragment extends BaseFragment{

    // 定义控件
    private EditText MCL_IdEdit;
    private EditText MCL_stateEdit;
    private EditText QuantityEdit;
    private EditText MCL_AreaEdit;
    private EditText MCL_locationEdit;
    private EditText MCL_LocationXEdit;
    private EditText MCL_LocationYEdit;

    private Button btnInsert;
    private Button btnUpdate;
    private Button btnDelete;
    private Button btnClear;
    private Button btnSelect;
    private GridView queryAdminGridView;
    // 定义变量
    private String MCL_Id = "";
    private String Quantity = "";
    private String MCL_state = "";
    private String MCL_Area = "";
    private String MCL_location = "";
    private String MCL_LocationX = "";
    private String MCL_LocationY = "";
    // SQL帮助类，参数用于设置连接字符串，参数1：主机ip，参数2：数据库名，参数3：用户名，参数4：用户密码
    private SqlHelper sh = new SqlHelper("192.168.2.102", "MassageChair", "sa", "123456");

    int MCL_IdC=100;
    int MCL_stateC=0;
    int QuantityC=0;
    int MCL_LocationXC=0;
    int MCL_LocationYC=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        mContent = getContext();
        View view=inflater.inflate(R.layout.fragment_a_massage_chair_location,container,false);
        MCL_IdEdit=(EditText) view.findViewById(R.id.mcL_id);
        QuantityEdit=(EditText) view.findViewById(R.id.quantity);
        MCL_stateEdit=(EditText) view.findViewById(R.id.MCL_state);
        MCL_AreaEdit=(EditText) view.findViewById(R.id.MCL_area);
        MCL_locationEdit=(EditText) view.findViewById(R.id.location);
        MCL_LocationXEdit=(EditText) view.findViewById(R.id.locationX);
        MCL_LocationYEdit=(EditText) view.findViewById(R.id.locationY);

        btnInsert = (Button) view.findViewById(R.id.btnInsert);
        btnUpdate = (Button) view.findViewById(R.id.btnUpdate);
        btnDelete = (Button) view.findViewById(R.id.btnDelete);
        btnClear = (Button) view.findViewById(R.id.btnClear);
        btnSelect = (Button) view.findViewById(R.id.btnSelect);
        queryAdminGridView = (GridView) view.findViewById(R.id.queryAdminGridView);

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
                    //Log.e("查询结果jsonResult",jsonResult);
                    //Log.e("查询结果msg.obj.toString()",msg.obj.toString());

                    // Gson类，用于json的转类型操作
                    Gson gson = new Gson();
                    // 定义查询到的结果类型，每一行记录映射为对象，本程序查询的是生产计划，所以一行记录表示一个品种的生产计划，用Plan类表示，用List收集全部Plan类
                    Type type = new TypeToken<List<MassageChairLocation>>() {
                    }.getType();
                    // 使用gson的fromJson()方法，参数1：json结果，参数2：想要转哪一个类型
                    List<MassageChairLocation> massageChairLocations = gson.fromJson(jsonResult, type);

                    // 由于要使用GridView表示，绑定数据时只能使用Map<K,T>的类型，并且多个记录时，要用List<Map<K,T>>
                    // 先实例化
                    List<Map<String, String>> mMassageChairLocation = new ArrayList<Map<String, String>>();
                    // 实例化一个title，是GridView的列头
                    Map<String, String> title = new HashMap<String, String>();

                    title.put("MCL_Id", "MCL_Id");
                    title.put("MCL_state", "状态");
                    title.put("quantity", "数量");
                    title.put("MCL_Area", "地区");
                    title.put("MCL_location", "地点");
                    title.put("MCL_locationX", "坐标X");
                    title.put("MCL_locationY", "坐标Y");
                    // 首先把表头追加到List<Map<String, String>>
                    mMassageChairLocation.add(title);

                    // for循环从json转过来的List<Plan>
                    for (MassageChairLocation massageChairLocation : massageChairLocations){
                        // 实例化用于接收plan的HashMap<K,T>类
                        HashMap<String, String> hMassageChairLocations = new HashMap<String, String>();
                        // 使用put()方法把数值加入到HashMap<K,T>，参数1：键，参数2：值

                        hMassageChairLocations.put("MCL_Id", massageChairLocation.MCL_id+"");
                        //Log.e("MCL_Id",massageChairLocation.MCL_id+"");

                        hMassageChairLocations.put("MCL_state", massageChairLocation.MCL_state+"");
                        //Log.e("MCL_state",massageChairLocation.MCL_state+"");

                        hMassageChairLocations.put("quantity", massageChairLocation.quantity+"");
                        //Log.e("quantity",massageChairLocation.quantity+"");

                        hMassageChairLocations.put("MCL_Area", massageChairLocation.MCL_area);
                        //Log.e("MCL_Area",massageChairLocation.MCL_area);

                        hMassageChairLocations.put("MCL_location", massageChairLocation.MCL_location+"");
                        //Log.e("MCL_location",massageChairLocation.MCL_location+"");

                        hMassageChairLocations.put("MCL_locationX", massageChairLocation.MCL_locationX+"");
                        //Log.e("MCL_locationX",massageChairLocation.MCL_locationX+"");

                        hMassageChairLocations.put("MCL_locationY", massageChairLocation.MCL_locationY+"");
                        //Log.e("MCL_locationY",massageChairLocation.MCL_locationY+"");
                        // 把HashMap加入到List<Map>中
                        mMassageChairLocation.add(hMassageChairLocations);
                    }

                    // SimpleAdapter是GridView的适配器，参数1：上下文内容，参数2：List<Map<K,T>>对象，参数3：GridView的布局文件，指每一个item的布局，需要在res/layout中创建xml，
                    // 参数4：String数组，指每一列要绑定到Map中的值，数组中的值就是上文“hmPlans.put("id",
                    // plan.id);”的键"id"
                    // 参数5：列头的显示文件，存放在res/values/strings.xml
                    SimpleAdapter sa = new SimpleAdapter(mContent, mMassageChairLocation, R.layout.famcl_item,
                            new String[] { "MCL_Id","MCL_state","quantity","MCL_Area","MCL_location",
                                    "MCL_locationX","MCL_locationY"}, new int[] { R.id.header1, R.id.header2, R.id.header3
                            , R.id.header4, R.id.header5, R.id.header6 , R.id.header7});
                    // 把SimpleAdapter绑定到GridView
                    //Log.e("mMassageChairLocation",mMassageChairLocation.toString());
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
        MCL_Id = MCL_IdEdit.getText().toString().trim();
        MCL_state = MCL_stateEdit.getText().toString().trim();
        Quantity = QuantityEdit.getText().toString().trim();
        MCL_Area = MCL_AreaEdit.getText().toString().trim();
        MCL_location = MCL_locationEdit.getText().toString().trim();
        MCL_LocationX= MCL_LocationXEdit.getText().toString().trim();
        MCL_LocationY= MCL_LocationYEdit.getText().toString().trim();
    }

//    public void GetMsg1() {
//        MCL_Id = dealEditToString.dealStringNull(MCL_IdEdit);
//        MCL_state = dealEditToString.dealStringNull(MCL_stateEdit);
//        Quantity = dealEditToString.dealStringNull(QuantityEdit);
//        MCL_Area = dealEditToString.dealStringNull(MCL_AreaEdit);
//        MCL_location = dealEditToString.dealStringNull(MCL_locationEdit);
//        MCL_LocationX= dealEditToString.dealStringNull(MCL_LocationXEdit);
//        MCL_LocationY= dealEditToString.dealStringNull(MCL_LocationYEdit);
//    }
    // Insert()方法，通过判断受影响行数，返回“添加成功”或“操作失败”

    public String Insert() {

        if(MCL_state==null|MCL_state==""){
            MCL_stateC=0;
        }else {MCL_stateC=Integer.parseInt(MCL_state);}

        if(Quantity==null|Quantity==""){
            QuantityC=0;
        }else {QuantityC=Integer.parseInt(Quantity);}

        if(MCL_LocationX==null|MCL_LocationX==""){
            MCL_LocationXC=0;
        }else {MCL_LocationXC=Integer.parseInt(MCL_LocationX);}

        if(MCL_LocationY==null|MCL_LocationY==""){
            MCL_LocationYC=0;
        }else {MCL_LocationYC=Integer.parseInt(MCL_LocationY);}
        MassageChairLocation massageChairLocation=new MassageChairLocation();
        massageChairLocation.setMCL_State(MCL_stateC);
        massageChairLocation.setQuantity(QuantityC);
        massageChairLocation.setMCL_Area(MCL_Area);
        massageChairLocation.setMCL_location(MCL_location);
        massageChairLocation.setMCL_LocationX(MCL_LocationXC);
        massageChairLocation.setMCL_LocationY(MCL_LocationYC);
        massageChairLocation.save();
        GetMsg();
        String sql = "INSERT INTO [MassageChairLocation](" +
                "[MCL_state]" +
                ",[quantity]" +
                ",[MCL_area]" +
                ",[MCL_location]" +
                ",[MCL_locationX]" +
                ",[MCL_locationY]) VALUES (?,?,?,?,?,?)";

        List<Object> params = new ArrayList<Object>();
        params.add(MCL_state);
        params.add(Quantity);
        params.add(MCL_Area);
        params.add(MCL_location);
        params.add(MCL_LocationX);
        params.add(MCL_LocationY);
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
            //Log.e("e.getMessage()",e.getMessage());
            return "3操作失败！";
        }
    }

    public String Update() {

        if(MCL_state==null|MCL_state==""){
            MCL_stateC=0;
        }else {MCL_stateC=Integer.parseInt(MCL_state);}

        if(Quantity==null|Quantity==""){
            QuantityC=0;
        }else {QuantityC=Integer.parseInt(Quantity);}

        if(MCL_LocationX==null|MCL_LocationX==""){
            MCL_LocationXC=0;
        }else {MCL_LocationXC=Integer.parseInt(MCL_LocationX);}

        if(MCL_LocationY==null|MCL_LocationY==""){
            MCL_LocationYC=0;
        }else {MCL_LocationYC=Integer.parseInt(MCL_LocationY);}

        MassageChairLocation massageChairLocation=new MassageChairLocation();
        massageChairLocation.setMCL_State(MCL_stateC);
        massageChairLocation.setQuantity(QuantityC);
        massageChairLocation.setMCL_Area(MCL_Area);
        massageChairLocation.setMCL_location(MCL_location);
        massageChairLocation.setMCL_LocationX(MCL_LocationXC);
        massageChairLocation.setMCL_LocationY(MCL_LocationYC);
        massageChairLocation.updateAll("MCL_Id=?",MCL_Id);

        GetMsg();
        String sql = "UPDATE [MassageChairLocation] SET [MCL_state]=?,[quantity]=?," +
                "[MCL_Area]=?,[MCL_Location]=?,[MCL_LocationX]=?,[MCL_LocationY]=? where [MCL_Id]=?";

        // params用于存放变量参数，即sql中的“？”
        List<Object> params = new ArrayList<Object>();
        params.add(MCL_state);
        params.add(Quantity);
        params.add(MCL_Area);
        params.add(MCL_location);
        params.add(MCL_LocationX);
        params.add(MCL_LocationY);
        params.add(MCL_Id);
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
            //Log.e("e.getMessage()",e.getMessage());
            return "操作失败！";
        }
    }

    public String Delete() {
        GetMsg();
        DataSupport.deleteAll(MassageChairLocation.class,"MCL_Id=?",MCL_Id);

        String sql = "DELETE FROM [MassageChairLocation] where [MCL_Id]=?";

        List<Object> params = new ArrayList<Object>();
        params.add(MCL_Id);
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
            //Log.e("e.getMessage()",e.getMessage());
            return "操作失败！";
        }
    }

    // Select()方法，查询生产计划
    public String Select() {

        String sql = "SELECT [MCL_Id] AS MCL_id" +
                ",[MCL_state] AS MCL_state" +
                ",[quantity] AS quantity" +
                ",[MCL_Area] AS MCL_area" +
                ",[MCL_location] AS MCL_location" +
                ",[MCL_LocationX] AS MCL_locationX" +
                ",[MCL_LocationY] AS MCL_locationY FROM [MassageChairLocation]";
        String jsonResult = null;
        try {
            // sh.ExecuteQuery()，参数1：查询语句，参数2：查询用到的变量，用于本案例不需要参数，所以用空白的new
            // ArrayList<Object>()
            jsonResult = sh.ExecuteQuery(sql, new ArrayList<Object>());
        } catch (Exception e) {
            // TODO: handle exception
            //System.out.println(e.getMessage());
            //Log.e("e.getMessage()",e.getMessage());
            return null;
        }
        return jsonResult;
    }

    // 界面复位，清空显示
    public void ClearEdit() {
        //清空文本输入框
        MCL_IdEdit.setText("");
        MCL_stateEdit.setText("");
        QuantityEdit.setText("");
        MCL_AreaEdit.setText("");
        MCL_locationEdit.setText("");
        MCL_LocationXEdit.setText("");
        MCL_LocationYEdit.setText("");
        //etId获得焦点
        MCL_IdEdit.setFocusable(true);
        MCL_IdEdit.setFocusableInTouchMode(true);
        MCL_IdEdit.requestFocus();
        MCL_IdEdit.requestFocusFromTouch();
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

}
