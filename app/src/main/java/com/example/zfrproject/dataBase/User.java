package com.example.zfrproject.dataBase;

import org.litepal.crud.DataSupport;

public class User extends DataSupport{
    public int id;
    public int balance;
    public String phoneNumber;
    public String idNumber;
    public String username;
    public String area;
    public String email;
    public String password;
    public String realName;
    public int locationX;
    public int locationY;
    public String sex;

    public int getId(){

        return id;
    }
    public void setId(int id){

        this.id=id;
    }

    public int getBalance(){

        return balance;
    }
    public void setBalance(int balance){

        this.balance=balance;
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

    public String getEmail(){

        return email;
    }
    public void setEmail(String email){

        this.email=email;
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
    public String getSex(){
        return sex;
    }
    public void setSex(String sex){
        this.sex=sex;
    }
}
