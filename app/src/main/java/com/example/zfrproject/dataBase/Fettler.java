package com.example.zfrproject.dataBase;

import org.litepal.crud.DataSupport;

public class Fettler extends DataSupport {
    public int id;
    public String phoneNumber;
    public String idNumber;
    public String jobNumber;
    public String username;
    public String area;
    public String password;
    public String realName;
    public int locationX;
    public int locationY;
    public int sex;

    public int getId(){

        return id;
    }
    public void setId(int id){

        this.id=id;
    }

    public String getPhoneNumber(){

        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber){

        this.phoneNumber=phoneNumber;
    }

    public String getIdNumber(){

        return idNumber;
    }
    public void setIdNumber(String idNumber){

        this.idNumber=idNumber;
    }

    public String getJobNumber(){
        return jobNumber;
    }
    public void setJobNumber(String jobNumber){
        this.jobNumber=jobNumber;
    }

    public String getUsername(){

        return username;
    }
    public void setUsername(String username){

        this.username=username;
    }

    public String getArea(){

        return area;
    }
    public void setArea(String area){

        this.area=area;
    }

    public String getPassword(){

        return password;
    }
    public void setPassword(String password){

        this.password=password;
    }

    public String getRealName(){

        return realName;
    }
    public void setRealName(String realName){

        this.realName=realName;
    }

    public int getlocationX(){

        return locationX;
    }
    public void setlocationX(int locationX){

        this.locationX=locationX;
    }
    public int getlocationY(){

        return locationY;
    }
    public void setlocationY(int locationY){

        this.locationY=locationY;
    }
    public int getSex(){
        return sex;
    }
    public void setSex(int sex){
        this.sex=sex;
    }
}