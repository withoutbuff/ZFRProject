package com.example.zfrproject.dataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String CREATE_USER="create table User("
            +"id integer primary key autoincrement,"//integer打错了，打成inreger了。
            +"username text,"//登陆用户名
            +"balance integer,"//余额 sex   area  location  phonenumber  email  realname  idnumber
            +"password text,"//密码
            +"realName text," //真实姓名
            +"sex text," //性别
            +"location text,"//位置
            +"phoneNumber integer,"//手机号码
            +"area text,"//地区
            +"email text,"//邮箱
            +"idNumber integer)";//身份证号
    public static final String CREATE_ADMIN="create table Admin("
            +"id integer primary key autoincrement,"
            +"username text,"//登陆用户名
            +"password text,"//密码
            +"realName text," //真实姓名
            +"sex text," //性别
            +"location text,"//位置
            +"phoneNumber integer,"//手机号码
            +"area text,"//地区
            +"idNumber integer)";//身份证号
    public static final String CREATE_FETTLER="create table Fettler("//维修人员表
            +"id integer primary key autoincrement,"
            +"username text,"//登陆用户名
            +"password text,"//密码
            +"realName text," //真实姓名
            +"sex text," //性别
            +"location text,"//位置
            +"phoneNumber integer,"//手机号码
            +"area text,"//地区
            +"jobNumber integer,"//工号
            +"idNumber integer)";//身份证号
    public static final String CREATE_MASSAGE_CHAIR="create table MassageChair("//按摩椅信息表
            +"id integer primary key autoincrement,"
            +"version text,"//型号
            +"state text,"//状态
            +"location text,"//位置
            +"number integer,"//编号
            +"area text)";//地区
    public static final String CREATE_MASSAGE_CHAIR_LOCATION="create table MassageChairLocation("//按摩椅存放点信息表
            +"id integer primary key autoincrement,"
            +"state text,"//状态
            +"location text,"//位置
            +"quantity integer,"//数量
            +"area text)";//地区

    private Context mContext;
    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
        mContext=context;
    }
    @Override
    public void  onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_USER);
        db.execSQL(CREATE_ADMIN);
        db.execSQL(CREATE_FETTLER);
        db.execSQL(CREATE_MASSAGE_CHAIR);
        db.execSQL(CREATE_MASSAGE_CHAIR_LOCATION);
        Toast.makeText(mContext,"Create Succeeded",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        db.execSQL("drop table if exists User");
        db.execSQL("drop table if exists Admin");
        db.execSQL("drop table if exists Fettler");
        db.execSQL("drop table if exists MassageChair");
        db.execSQL("drop table if exists MassageChairLocation");

    }
}
