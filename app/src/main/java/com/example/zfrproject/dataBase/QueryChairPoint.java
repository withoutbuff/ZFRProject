package com.example.zfrproject.dataBase;



import android.util.Log;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;



public class QueryChairPoint {
    //用户查询
    //地点
    public static double[] userQueryMCLPointX(double[] points){
        List<MassageChairLocation> massageChairLocationXs=DataSupport.select("MCL_state","MCL_locationX")
                .where("MCL_state = ?","0")
                .find(MassageChairLocation.class);

        //循环遍历List对象
        for (int i=0;i<massageChairLocationXs.size();i++){
            MassageChairLocation massageChairLocationX=massageChairLocationXs.get(i);
            points[i]=massageChairLocationX.getMCL_LocationX();
            Log.e("查询X",""+i+massageChairLocationX.getMCL_LocationX());
        }
        return points;
    }
    public static double[] userQueryMCLPointY(double[] points){
        List<MassageChairLocation> massageChairLocationYs=DataSupport.select("MCL_state","MCL_locationY")
                .where("MCL_state = ?","0")
                .find(MassageChairLocation.class);
        for (int i=0;i<massageChairLocationYs.size();i++){
            MassageChairLocation massageChairLocationY=massageChairLocationYs.get(i);
            points[i]=massageChairLocationY.getMCL_LocationY();
            Log.e("查询Y",""+i+massageChairLocationY.getMCL_LocationY());
        }
        return points;
    }
    //放置点id
    public static int[] userQueryMCLId(int[] MCL_ids){
        //查询到的id设为标题
        List<MassageChairLocation> massageChairLocationMCL_ids=DataSupport.select("MCL_state","MCL_id")
                .where("MCL_state = ?","0")
                .find(MassageChairLocation.class);
        //循环遍历查询结果
        for (int i=0;i<massageChairLocationMCL_ids.size();i++){
            //把对象List的内容赋值给MassageChairLocation对象
            MassageChairLocation massageChairLocationId=massageChairLocationMCL_ids.get(i);
            MCL_ids[i]=massageChairLocationId.getMCL_Id();
            //Log.e("查询Y",""+i+massageChairLocationY.getMCL_LocationY());
        }
        return MCL_ids;
    }
//放置点位置名
public static String[] userQueryMCLLocations(String[] MCL_locations){
    //查询到的Location设为内容
    List<MassageChairLocation> massageChairLocationMCL_locations=DataSupport.select("MCL_state","MCL_location")
            .where("MCL_state = ?","0")
            .find(MassageChairLocation.class);
    //循环遍历查询结果
    for (int i=0;i<massageChairLocationMCL_locations.size();i++){
        //把对象List的内容赋值给MassageChairLocation对象
        MassageChairLocation massageChairLocationLocation=massageChairLocationMCL_locations.get(i);
        MCL_locations[i]=massageChairLocationLocation.getMCL_location();
        //Log.e("查询Y",""+i+massageChairLocationY.getMCL_LocationY());
    }
    return MCL_locations;
}

//椅子
public static double[] userQueryMCPointX(double[] points){
    List<MassageChair> massageChairXs=DataSupport.select("MC_state","MC_locationX")
            .where("MC_state = ?","未使用")
            .find(MassageChair.class);

    //循环遍历List对象
    for (int i=0;i<massageChairXs.size();i++){
        MassageChair massageChairX=massageChairXs.get(i);
        points[i]=massageChairX.getMC_LocationX();
        Log.e("查询X",""+i+massageChairX.getMC_LocationX());
    }
    return points;
}
    public static double[] userQueryMCPointY(double[] points){
        List<MassageChair> massageChairYs=DataSupport.select("MC_state","MC_locationY")
                .where("MC_state = ?","未使用")
                .find(MassageChair.class);
        for (int i=0;i<massageChairYs.size();i++){
            MassageChair massageChairY=massageChairYs.get(i);
            points[i]=massageChairY.getMC_LocationY();
            Log.e("查询Y",""+i+massageChairY.getMC_LocationY());
        }
        return points;
    }
//椅子floor
    public static double[] userQueryMCFloor(double[] points){
        List<MassageChair> massageChairFloors=DataSupport.select("MC_state","floor")
                .where("MC_state = ?","未使用")
                .find(MassageChair.class);
        for (int i=0;i<massageChairFloors.size();i++){
            MassageChair massageChairFloor=massageChairFloors.get(i);
            points[i]=massageChairFloor.getFloor();
            Log.e("查询Y",""+i+massageChairFloor.getFloor());
        }
        return points;
    }

//管理员查询
    public static double[] adminQueryMCPointX(double[] points){
        List<MassageChairLocation> massageChairLocationXs=DataSupport.select("MCL_state","MCL_locationX")
                .find(MassageChairLocation.class);

        //循环遍历List对象
        for (int i=0;i<massageChairLocationXs.size();i++){
            MassageChairLocation massageChairLocationX=massageChairLocationXs.get(i);
            points[i]=massageChairLocationX.getMCL_LocationX();
            Log.e("查询X",""+i+massageChairLocationX.getMCL_LocationX());
        }
        return points;
    }
    public static double[] adminQueryMCPointY(double[] points){
        List<MassageChairLocation> massageChairLocationYs=DataSupport.select("MCL_state","MCL_locationY")
                .find(MassageChairLocation.class);
        for (int i=0;i<massageChairLocationYs.size();i++){
            MassageChairLocation massageChairLocationY=massageChairLocationYs.get(i);
            points[i]=massageChairLocationY.getMCL_LocationY();
            Log.e("查询Y",""+i+massageChairLocationY.getMCL_LocationY());
        }
        return points;
    }



    //维修人员查询椅子
    public static double[] fettlerQueryMCLPointX(double[] points){
        List<MassageChair> massageChairXs=DataSupport.select("MC_state","MC_locationX")
                .where("MC_state = ?","损坏")
                .find(MassageChair.class);

        //循环遍历List对象
        for (int i=0;i<massageChairXs.size();i++){
            MassageChair massageChairLocationX=massageChairXs.get(i);
            points[i]=massageChairLocationX.getMC_LocationX();
            Log.e("查询X",""+i+massageChairLocationX.getMC_LocationX());
        }
        return points;
    }
    public static double[] fettlerQueryMCLPointY(double[] points){
        List<MassageChair> massageChairYs=DataSupport.select("MC_state","MC_locationY")
                .where("MC_state = ?","损坏")
                .find(MassageChair.class);
        for (int i=0;i<massageChairYs.size();i++){
            MassageChair massageChairLocationY=massageChairYs.get(i);
            points[i]=massageChairLocationY.getMC_LocationY();
            Log.e("查询Y",""+i+massageChairLocationY.getMC_LocationY());
        }
        return points;
    }

    //放置点id
    public static int[] fettlerQueryMCLId(int[] MCL_ids){
        //查询到的id设为标题
        List<MassageChairLocation> massageChairLocationMCL_ids=DataSupport.select("MCL_state","MCL_id")

                .find(MassageChairLocation.class);
        //循环遍历查询结果
        for (int i=0;i<massageChairLocationMCL_ids.size();i++){
            //把对象List的内容赋值给MassageChairLocation对象
            MassageChairLocation massageChairLocationId=massageChairLocationMCL_ids.get(i);
            MCL_ids[i]=massageChairLocationId.getMCL_Id();
            Log.e("查询Y",""+i+MCL_ids[i]);
        }
        return MCL_ids;
    }
    //放置点位置名
    public static String[] fettlerQueryMCLLocations(String[] MCL_locations){
        //查询到的Location设为内容
        List<MassageChairLocation> massageChairLocationMCL_locations=DataSupport.select("MCL_state","MCL_location")

                .find(MassageChairLocation.class);
        //循环遍历查询结果
        for (int i=0;i<massageChairLocationMCL_locations.size();i++){
            //把对象List的内容赋值给MassageChairLocation对象
            MassageChairLocation massageChairLocationLocation=massageChairLocationMCL_locations.get(i);
            MCL_locations[i]=massageChairLocationLocation.getMCL_location();
            Log.e("查询Y",""+i+MCL_locations[i]);
        }
        return MCL_locations;
    }

}
