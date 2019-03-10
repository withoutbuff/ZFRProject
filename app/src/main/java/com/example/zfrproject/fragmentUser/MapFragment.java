package com.example.zfrproject.fragmentUser;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
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
import com.example.zfrproject.dataBase.MassageChairLocation;
import com.example.zfrproject.dataBase.SqlHelper;
import com.example.zfrproject.dataBase.User;
import com.example.zfrproject.user.UserMapChairActivity;
import com.example.zfrproject.dataBase.QueryChairPoint;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class MapFragment extends BaseFragment implements
        View.OnClickListener, AMap.OnMyLocationChangeListener,
        AMap.OnMarkerClickListener {
    private String DatabaseIp="192.168.2.102";
    private SqlHelper sh = new SqlHelper(DatabaseIp, "MassageChair", "sa", "123456");
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

    public int[] ids=new int[200];

    public String[] locations=new String[200];
    private SharedPreferences pref;
    String usernamepref;
    int balance;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        mContent = getContext();
        View view=inflater.inflate(R.layout.fragment_map,container,false);
        //获取地图控件引用

        mMapView = (MapView) view.findViewById(R.id.mapView);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);

        initMap();

        pref = PreferenceManager.getDefaultSharedPreferences(mContent);
        usernamepref = pref.getString("username", "");

        mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象
        //指南针用于向 App 端用户展示地图方向，默认不显示
        mUiSettings.setCompassEnabled(true);
        mUiSettings.setMyLocationButtonEnabled(true); //显示默认的定位按钮
        mUiSettings.setScaleControlsEnabled(true);//控制比例尺控件是否显示

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

        return view;
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
        aMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE).interval(999999999) );//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）默认执行此种模式。
        //aMap.setLocationSource(mLocationSource);//通过aMap对象设置定位数据源的监听
        //设置SDK 自带定位消息监听
        aMap.setOnMyLocationChangeListener(this);

        aMap.setOnMarkerClickListener(this);
        addMarkersToMap();// 往地图上添加marker

        aMap.moveCamera(CameraUpdateFactory.zoomTo(16));//设置缩放等级，100m=16

        aMap.showIndoorMap(true);//室内地图


    }



    // Select()方法，查询余额,再传递到
    public String Select() {
        Log.e("进入查询方法","进入");
//需要查询，x,y，满足MC状态，未使用,属于点击的MCL的，这个数从前面获取,已经获取intentMCL_id，放到哪，查询语句的条件里
        String sql = "SELECT [balance] AS balance" +
                " FROM [user] " +
                "WHERE [username]="+"'"+usernamepref+"'";
        Log.e("sql",sql);
        Log.e("usernamepref",usernamepref);
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

//查询余额
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
                Type type = new TypeToken<List<User>>() {
                }.getType();
                // 使用gson的fromJson()方法，参数1：json结果，参数2：想要转哪一个类型
                List<User> users = gson.fromJson(jsonResult, type);
                for(User user:users){
                    balance=user.balance;
                }
                Intent intent=new Intent(mContent, UserMapChairActivity.class);
                intent.putExtra("balance",balance);
                Log.e("balance",balance+"");
        }
    }
};


    // Select()方法，查询余额,再传递到
    public String Select1() {
        Log.e("进入查询方法","进入");
//需要查询，x,y，满足MC状态，未使用,属于点击的MCL的，这个数从前面获取,已经获取intentMCL_id，放到哪，查询语句的条件里
        String sql = "SELECT [balance] AS balance" +
                " FROM [user] " +
                "WHERE [username]="+"'"+usernamepref+"'";
        Log.e("sql",sql);
        Log.e("usernamepref",usernamepref);
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

    //查询余额
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
                    Type type = new TypeToken<List<User>>() {
                    }.getType();
                    // 使用gson的fromJson()方法，参数1：json结果，参数2：想要转哪一个类型
                    List<User> users = gson.fromJson(jsonResult, type);
                    for(User user:users){
                        balance=user.balance;
                    }
                    Intent intent=new Intent(mContent, UserMapChairActivity.class);
                    intent.putExtra("balance",balance);
                    Log.e("balance",balance+"");
            }
        }
    };


        /**
         * 在地图上添加marker
         */
    private void addMarkersToMap() {
        QueryChairPoint.userQueryMCLPointX(pointXs);
        QueryChairPoint.userQueryMCLPointY(pointYs);
        QueryChairPoint.userQueryMCLId(ids);
        QueryChairPoint.userQueryMCLLocations(locations);
        for(int i=0;i<pointXs.length;i++){
            LatLng latlng =new LatLng(pointXs[i],pointYs[i]);
            markerOption = new MarkerOptions().icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                    .position(latlng)//点坐标
                    .draggable(false)//不可拖拽
                    .title(""+ids[i])
                    .snippet(locations[i]);//内容
            aMap.addMarker(markerOption);
        }
    }


    /**
     * 对marker标注点点击响应事件
     */
    @Override
    public boolean onMarkerClick(final Marker marker) {
        if (aMap != null) {
            jumpPoint(marker);
        }
        //开启线程操作数据库,查询余额
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
        //在此处添加点

        Intent intent = new Intent(mContent, UserMapChairActivity.class);
        //传递LatLng对象，即点坐标
        LatLng position=marker.getPosition();
        intent.putExtra("positionpass",new Gson().toJson(position));
        //传点的标题，内容，即id，location
        intent.putExtra("id",marker.getTitle());
        Log.e("id",marker.getTitle());
        intent.putExtra("location",marker.getSnippet());
        Log.e("location",marker.getSnippet());
        //传递余额
        intent.putExtra("balance",balance);
        Log.e("balance1",balance+"");
        startActivity(intent);
        Toast.makeText(mContent, "您点击了"+marker.getSnippet(), Toast.LENGTH_LONG).show();
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
    protected void loadData() {
        //Toast.makeText(mContent,"Fragment头条加载数据",Toast.LENGTH_SHORT).show();
    }
    @Override
    protected View initView() {
        TextView mView = new TextView(mContent);
//        mView.setText("Fragment头条");
//        mView.setGravity(Gravity.CENTER);
//        mView.setTextSize(18);
//        mView.setTextColor(Color.BLACK);
        return mView;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initFruits();
    }
    @Override
    public void onClick(View v){

    }

}
