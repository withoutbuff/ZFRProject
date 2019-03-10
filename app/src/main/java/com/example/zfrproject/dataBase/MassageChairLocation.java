package com.example.zfrproject.dataBase;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class MassageChairLocation extends DataSupport{
    public int MCL_id;
    public int quantity;
    public String MCL_area;
    public int MCL_state;
    public double MCL_locationX;
    public double MCL_locationY;
    public String MCL_location;
//    public List<MassageChair> massageChairs=new ArrayList<MassageChair>();
//
//    public List<MassageChair> getMassageChairs() {
//        return DataSupport.where("MLC_id = ?", String.valueOf(MCL_id)).find(MassageChair.class);
//    }

    public int getMCL_Id(){

        return MCL_id;
    }
    public void setMCL_Id(int MCL_id){

        this.MCL_id=MCL_id;
    }

    public int getQuantity(){

        return quantity;
    }
    public void setQuantity(int quantity){

        this.quantity=quantity;
    }

    public String getMCL_Area(){

        return MCL_area;
    }
    public void setMCL_Area(String MCL_area){

        this.MCL_area=MCL_area;
    }

    public int getMCL_State(){

        return MCL_state;
    }
    public void setMCL_State(int MCL_state){

        this.MCL_state=MCL_state;
    }

    public double getMCL_LocationX(){

        return MCL_locationX;
    }
    public void setMCL_LocationX(double MCL_locationX){

        this.MCL_locationX=MCL_locationX;
    }
    public double getMCL_LocationY(){

        return MCL_locationY;
    }
    public void setMCL_LocationY(double MCL_locationY){

        this.MCL_locationY=MCL_locationY;
    }

    public String getMCL_location(){

        return MCL_location;
    }
    public void setMCL_location(String MCL_location){

        this.MCL_location=MCL_location;
    }

}

