package com.example.zfrproject.dataBase;

import org.litepal.crud.DataSupport;

public class Statement extends DataSupport {
    public int amount;
    public int state;
    public int id;
    public String number;

    public int getId(){

        return id;
    }
    public void setId(int id){

        this.id=id;
    }
    public int getAmount(){

        return amount;
    }
    public void setAmount(int amount){

        this.amount=amount;
    }
    public int getState(){

        return state;
    }
    public void setState(int state){

        this.state=state;
    }
    public String getNumber(){

        return number;
    }
    public void setNumber(String number){

        this.number=number;
    }
}
