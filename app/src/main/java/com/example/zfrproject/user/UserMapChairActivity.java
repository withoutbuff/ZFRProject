package com.example.zfrproject.user;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
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
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.example.zfrproject.R;
import com.example.zfrproject.dataBase.MassageChair;
import com.example.zfrproject.dataBase.SqlHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;

import org.litepal.crud.DataSupport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
//忘了扣费
public class UserMapChairActivity extends AppCompatActivity implements
        View.OnClickListener,
        AMap.OnMyLocationChangeListener,
        AMap.OnMarkerClickListener{
    private int time=0;
    private TextView title;
    List<MassageChair> massageChairs;
    //地图
    private MapView mMapView = null;
    private AMap aMap;
    //定位
    private MyLocationStyle myLocationStyle;
    private UiSettings mUiSettings;//定义一个UiSettings对象
    private LocationSource mLocationSource;
    //标记20.056729,110.328729
    private MarkerOptions markerOption;
    int i=0;
    public double[] pointXs=new double[200];//={20.056939,20.055639,20.055739,20.056739}

    public double[] pointYs=new double[200];;//={110.328939,110.329239,110.326939,110.328739};

    public int[] floors=new int[200];

    LatLng position=new LatLng(20,109);
    private Button scanButton;
    final private int  REQUEST_CODE_SCAN=0;
    private String result;
    private int chairNum;

    private String DatabaseIp="192.168.2.102";
    private SqlHelper sh = new SqlHelper(DatabaseIp, "MassageChair", "sa", "123456");

    String intentMCL_location;
    String intentMCL_id;

    private SharedPreferences pref;
    String usernamepref;
    int balance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_map_chair);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        usernamepref = pref.getString("username", "");

        //接受余额

        balance=getIntent().getIntExtra("balance",100);
        Log.e("Ubalance",balance+"");



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

//接受intentMCL_id,intentMCL_location
        intentMCL_id=getIntent().getStringExtra("id");
        Log.e("intentMCL_id",intentMCL_id);
        intentMCL_location=getIntent().getStringExtra("location");
        Log.e("intentMCL_location",intentMCL_location);
        //标题设置
        title=findViewById(R.id.title);
        title.setText(intentMCL_location+"按摩椅位置详情");
//动态权限申请
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(UserMapChairActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.CAMERA);
        }
        if (!permissionList.isEmpty()) {
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(UserMapChairActivity.this, permissions, 1);
        }

        //扫二维码
        scanButton=findViewById(R.id.scan);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(UserMapChairActivity.this, CaptureActivity.class);
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


        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.mapView);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);

        initMap();

        mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象
        //指南针用于向 App 端用户展示地图方向，默认不显示
        mUiSettings.setCompassEnabled(true);
        mUiSettings.setMyLocationButtonEnabled(true); //显示默认的定位按钮
        mUiSettings.setScaleControlsEnabled(true);//控制比例尺控件是否显示


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
                     massageChairs = gson.fromJson(jsonResult, type);

                    // 由于要使用GridView表示，绑定数据时只能使用Map<K,T>的类型，并且多个记录时，要用List<Map<K,T>>
                    // 先实例化
                    // for循环从json转过来的List<Plan>
                    //处理坐标数组
                    //查询处了问题"SELECT [MC_LOCATIONX] AS mc_locationx,[MC_LOCATIONY] AS mc_locationy FROM [MassageChair] " +
                    //                "where [MCL_id]="+intentMCL_id+"and [MC_state]=0";
                    //改mc_locationx
                    //E/查询结果msg.obj.toString(): [{"mc_locationx":"20.056234","mc_locationy":"110.330435"},{"mc_locationx":"20.055886","mc_locationy":"110.330902"}]
                    for (int i=0;i<massageChairs.size();i++){
                        pointXs[i]=massageChairs.get(i).getMC_LocationX();
                        Log.e("循pointXs",pointXs[i]+"");
                        Log.e("massageChairs.get(i)",massageChairs.get(i).getMC_LocationX()+"");
                        pointYs[i]=massageChairs.get(i).getMC_LocationY();
                        Log.e("循pointYs",pointYs[i]+"");
                        Log.e("massageChairs.get(i)",massageChairs.get(i).getMC_LocationY()+"");
                    }
                    //向地图添加按摩椅点
                    for(int i=0;i<pointXs.length;i++){
                        Log.e("addMarkersToMap:", " 开始循环");
                        LatLng latlng =new LatLng(pointXs[i],pointYs[i]);
                        Log.e("latlng:", ""+i);
                        Log.e("latlng:", ""+pointXs[i]);
                        Log.e("latlng:", ""+pointYs[i]);
                        markerOption = new MarkerOptions().icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                                .position(latlng)//点坐标
                                .draggable(false);//不可拖拽
                        aMap.addMarker(markerOption).setZIndex(2);
                    }

            }
        }
    };
    // Select()方法，查询生产计划
    public String Select() {
        Log.e("进入查询方法","进入");
//需要查询，x,y，满足MC状态，未使用,属于点击的MCL的，这个数从前面获取,已经获取intentMCL_id，放到哪，查询语句的条件里
        String sql = "SELECT [MC_LOCATIONX] AS MC_locationX,[MC_LOCATIONY] AS MC_locationY FROM [MassageChair] " +
                "where [MCL_id]="+intentMCL_id+"and [MC_state]="+"'"+0+"'";
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

    /**
     * 初始化AMap对象
     */
    private void initMap() {
//        if (aMap == null) {
//            aMap = mMapView.getMap();
//            setUpMap();
//        }
        // Fragment嵌套高德地图，当再次进入Fragment的时候，会出现奇怪的现象。嵌套的地图会出现无法定位的现象。这个问题出现的原因在于，fragment在被移除时，不会执行onDestroy（）方法，而是执行onDestroyView（）方法。fragment中的数据已经在第一次操作时完成了初始化了，所以以下代码中，aMap不为null。故无法正常开启定位功能。
        if (aMap == null) {
            aMap = mMapView.getMap();
            setUpMap();
        }else{
            aMap.clear();
            aMap.setLocationSource(mLocationSource);
            aMap.setMyLocationEnabled(true);
            aMap = mMapView.getMap();
            setUpMap();
        }
    }
    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {

        // 如果要设置定位的默认状态，可以在此处进行设置
        myLocationStyle = new MyLocationStyle();
        aMap.setMyLocationStyle(myLocationStyle);

        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 定位、且将视角移动到地图中心点，定位点依照设备方向旋转，  并且会跟随设备移动。
        aMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER) );//连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。
        //aMap.setLocationSource(mLocationSource);//通过aMap对象设置定位数据源的监听
        //设置SDK 自带定位消息监听
        aMap.setOnMyLocationChangeListener(this);

        aMap.setOnMarkerClickListener(this);
        //addMarkersToMap();// 往地图上添加marker

        aMap.moveCamera(CameraUpdateFactory.zoomTo(18));//设置缩放等级，100m=16,18可显示室内地图

        //接受参数并且移动到这个点,移动地图中心
        String positionPass=getIntent().getStringExtra("positionpass");
        position=new Gson().fromJson(positionPass,LatLng.class);
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(position));

        aMap.showIndoorMap(true);//室内地图

    }



    /**
     * 在地图上添加marker
     */
//    private void addMarkersToMap() {
//
//    }

    /**
     * 对marker标注点点击响应事件
     */
    @Override
    public boolean onMarkerClick(final Marker marker) {
        if (aMap != null) {
            jumpPoint(marker);
        }
        Toast.makeText(this, "所有显示点均可使用", Toast.LENGTH_LONG).show();
        return true;
    }

    /**
     * marker点击时跳动一下
     */
    public void jumpPoint(final Marker marker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = aMap.getProjection();
        final LatLng markerLatlng = marker.getPosition();
        Point markerPoint = proj.toScreenLocation(markerLatlng);
        markerPoint.offset(0, -100);
        final LatLng startLatLng = proj.fromScreenLocation(markerPoint);
        final long duration = 1500;

        final Interpolator interpolator = new BounceInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * markerLatlng.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * markerLatlng.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                if (t < 1.0) {
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    @Override
    public void onMyLocationChange(Location location) {
        // 定位回调监听
        if(location != null) {
            Log.e("amap", "onMyLocationChange 定位成功， lat: " + location.getLatitude() + " lon: " + location.getLongitude());
            Bundle bundle = location.getExtras();
            if(bundle != null) {
                int errorCode = bundle.getInt(MyLocationStyle.ERROR_CODE);
                String errorInfo = bundle.getString(MyLocationStyle.ERROR_INFO);
                // 定位类型，可能为GPS WIFI等，具体可以参考官网的定位SDK介绍
                int locationType = bundle.getInt(MyLocationStyle.LOCATION_TYPE);

                /*
                errorCode
                errorInfo
                locationType
                */
                Log.e("amap", "定位信息， code: " + errorCode + " errorInfo: " + errorInfo + " locationType: " + locationType );
            } else {
                Log.e("amap", "定位信息， bundle is null ");

            }

        } else {
            Log.e("amap", "定位失败");
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }
    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v){

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

    @SuppressLint("HandlerLeak")
    Handler handler1=new Handler(){
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            // 获得执行的结果，String字符串，返回操作是否成功提示
            String rst = msg.obj.toString();
            // 使用气泡提示
            Toast.makeText(getApplicationContext(), rst, Toast.LENGTH_SHORT).show();
//                MassageChair massageChair = new MassageChair();
//                massageChair.setMC_State(0);
//                Log.e("", "" + chairNum);
//                massageChair.updateAll("number = ?", "" + chairNum);

            //开启线程操作数据库,恢复状态
            Thread thread2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    // 通过Message类来传递结果值，先实例化
                    Message msg = Message.obtain();
                    // 下面分别是增删改查方法

                    // 设定msg的类型，用what属性，便于后面的代码区分返回的结果是什么类型
                    // 这里的1是指操作是否成功，String
                    // 这里的2是指查询的结果，String，用json的形式表示
                    String jsonResult = Update1();
                    msg.obj = jsonResult;
                    // 执行完以后，把msg传到handler，并且触发handler的响应方法
                    handler3.sendMessage(msg);
                }
            });
            // 进程开始，这行代码不要忘记
            thread2.start();
        }
    };

    @SuppressLint("HandlerLeak")
    Handler handler3=new Handler(){
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            // 获得执行的结果，String字符串，返回操作是否成功提示
            String rst = msg.obj.toString();
            // 使用气泡提示
            Toast.makeText(getApplicationContext(), rst, Toast.LENGTH_SHORT).show();
        }
    };
    @SuppressLint("HandlerLeak")
    Handler handler2=new Handler(){
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            String jsonResult = msg.obj.toString();
            Log.e("查询结果jsonResult",jsonResult);
            Log.e("查询结果msg.obj.toString()",msg.obj.toString());
            Gson gson = new Gson();
            Type type = new TypeToken<List<MassageChair>>() {
            }.getType();
            List<MassageChair> massageChairs = gson.fromJson(jsonResult, type);
            outLoop: for (MassageChair massageChair:massageChairs){
                if(chairNum==massageChair.number){
                    //开始使用
                    //开启线程操作数据库,写入状态
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
                            msg.obj = jsonResult;
                            // 执行完以后，把msg传到handler，并且触发handler的响应方法
                            handler1.sendMessage(msg);
                        }
                    });
                    // 进程开始，这行代码不要忘记
                    thread.start();
                    break outLoop;
                }
            }
            //开始使用
            //开启线程操作数据库,扣费
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
                    String jsonResult = Update2();
                    msg.obj = jsonResult;
                    // 执行完以后，把msg传到handler，并且触发handler的响应方法
                    handler1.sendMessage(msg);
                }
            });
            // 进程开始，这行代码不要忘记
            thread.start();
        }
    };
    public String Update2() {
        time++;
        String sql = "UPDATE [User] SET [balance]=?" +
                "where [username]=?";
        //一个需要注意的地方，prepareStatement这个方法会把问号做处理，还有顺序一定要一样
        List<Object> params = new ArrayList<Object>();
        params.add(balance-time);
        params.add(usernamepref);
        try {
            int count = sh.ExecuteNonQuery(sql, params);
            if (count == 1) {
                return "开始使用！";
            } else {
                return "2更新失败！";
            }
        } catch (Exception e) {
            // TODO: handle exception
            //System.out.println(e.getMessage());
            return "3更新失败！";
        }
    }
    public String Update() {
        String sql = "UPDATE [MassageChair] SET [MC_state]=?" +
                "where [number]=?";
        //一个需要注意的地方，prepareStatement这个方法会把问号做处理，还有顺序一定要一样
        List<Object> params = new ArrayList<Object>();
        params.add(1);
        params.add(result);
        try {
            int count = sh.ExecuteNonQuery(sql, params);
            if (count == 1) {
                return "开始使用！";
            } else {
                return "2更新失败！";
            }
        } catch (Exception e) {
            // TODO: handle exception
            //System.out.println(e.getMessage());
            return "3更新失败！";
        }
    }
    public String Update1() {
        String sql = "UPDATE [MassageChair] SET [MC_state]=?" +
                "where [number]=?";
        //一个需要注意的地方，prepareStatement这个方法会把问号做处理，还有顺序一定要一样
        List<Object> params = new ArrayList<Object>();
        params.add(0);
        params.add(result);
        try {
            int count = sh.ExecuteNonQuery(sql, params);
            if (count == 1) {
                return "使用结束！";
            } else {
                return "2更新失败！";
            }
        } catch (Exception e) {
            // TODO: handle exception
            //System.out.println(e.getMessage());
            return "3更新失败！";
        }
    }
    // Select()方法，查询生产计划
    public String Select1() {
        Log.e("进入查询方法","进入");
        Log.e("Select1+result",result);
        String sql = "SELECT [number] AS number FROM [MassageChair] where [MC_state]="+"'"+0+"'"+"AND [MCL_id] = "+"'"+intentMCL_id+"'";
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {

                String content = data.getStringExtra(Constant.CODED_CONTENT);
                //chairNumEditText.setText(content);//("扫描结果为：" + content);
                result=content;//按摩椅id
                //扣费，并且改状态

                chairNum = Integer.parseInt(result);
//先查询编号是否可用(有必要?),然后再扣费,设置状态,最后回收
                //开启线程操作数据库,查询编号
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
                        String jsonResult = Select1();
                        msg.obj = jsonResult;
                        // 执行完以后，把msg传到handler，并且触发handler的响应方法
                        handler2.sendMessage(msg);
                    }
                });
                // 进程开始，这行代码不要忘记
                thread.start();
            }
        }
    }


}
