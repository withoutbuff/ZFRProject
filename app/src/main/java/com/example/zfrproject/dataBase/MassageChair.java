package com.example.zfrproject.dataBase;

import org.litepal.crud.DataSupport;

public class MassageChair extends DataSupport {
    public int MC_id;
    public int MCL_id;
    public int number;
    public String version;
    public String MC_area;
    public int MC_state;
    public double MC_locationX;
    public double MC_locationY;
    public int floor;

//    public MassageChairLocation massageChairLocation;

    public int getMC_Id(){

        return MC_id;
    }
    public void setMC_Id(int MC_id){

        this.MC_id=floor;
    }
    public int getFloor(){

        return floor;
    }
    public void setFloor(int floor){

        this.floor=floor;
    }
    public int getMCL_Id(){

        return MCL_id;
    }
    public void setMCL_Id(int MCL_id){

        this.MCL_id=MCL_id;
    }

    public int getNumber(){

        return number;
    }
    public void setNumber(int number){

        this.number=number;
    }

    public String getVersion(){

        return version;
    }
    public void setVersion(String version){

        this.version=version;
    }

    public String getMC_Area(){

        return MC_area;
    }
    public void setMC_Area(String MC_area){

        this.MC_area=MC_area;
    }

    public int getMC_State(){

        return MC_state;
    }
    public void setMC_State(int MC_state){

        this.MC_state=MC_state;
    }

    public double getMC_LocationX(){

        return MC_locationX;
    }
    public void setMC_LocationX(double MC_locationX){

        this.MC_locationX=MC_locationX;
    }
    public double getMC_LocationY(){

        return MC_locationY;
    }
    public void setMC_LocationY(double MC_locationY){

        this.MC_locationY=MC_locationY;
    }
}
