package com.example.zfrproject.dataBase;

import org.litepal.LitePal;
//判断第二次启动不调用即可
public class createDatabase {
    private createDatabase (){}
    public static void createDataBase(){
//        LitePal.getDatabase();
//        User user=new User();
//        user.setId(1);
//        user.setUsername("共享按摩椅");
//        user.setPassword("123456");
//        user.setBalance(100);
//        user.save();
//        Admin admin=new Admin();
//        admin.setId(1);
//        admin.setUsername("admin");
//        admin.setPassword("admin");
//        admin.save();
//        Fettler fettler=new Fettler();
//        admin.setId(1);
//        fettler.setUsername("fettler");
//        fettler.setPassword("fettler");
//        fettler.save();

        MassageChairLocation massageChairLocation=new MassageChairLocation();
        massageChairLocation.setQuantity(2);
        massageChairLocation.setMCL_Id(1);
        massageChairLocation.setMCL_Area("海口");
        massageChairLocation.setMCL_State(0);//110.277788,19.979535
        massageChairLocation.setMCL_LocationX(19.979535);
        massageChairLocation.setMCL_LocationY(110.277788);
        massageChairLocation.setMCL_location("海口万达广场");
        massageChairLocation.save();
        MassageChairLocation massageChairLocation1=new MassageChairLocation();
        massageChairLocation1.setQuantity(2);
        massageChairLocation1.setMCL_Id(2);
        massageChairLocation1.setMCL_Area("海口");
        massageChairLocation1.setMCL_State(0);
        massageChairLocation1.setMCL_LocationX(20.0558817);
        massageChairLocation1.setMCL_LocationY(110.330644);
        massageChairLocation1.setMCL_location("海南大学7号楼");
        massageChairLocation1.save();
        MassageChairLocation massageChairLocation2=new MassageChairLocation();
        massageChairLocation2.setQuantity(2);
        massageChairLocation2.setMCL_Id(3);
        massageChairLocation2.setMCL_Area("海口");
        massageChairLocation2.setMCL_State(0);
        massageChairLocation2.setMCL_LocationX(20.05769);
        massageChairLocation2.setMCL_LocationY(110.329502);
        massageChairLocation2.setMCL_location("海南大学一田");
        massageChairLocation2.save();
        MassageChairLocation massageChairLocation3=new MassageChairLocation();
        massageChairLocation3.setQuantity(2);
        massageChairLocation3.setMCL_Id(4);
        massageChairLocation3.setMCL_Area("海口");
        massageChairLocation3.setMCL_State(1);
        massageChairLocation3.setMCL_LocationX(20.056057);
        massageChairLocation3.setMCL_LocationY(110.329126);
        massageChairLocation3.setMCL_location("海南大学五食堂");
        massageChairLocation3.save();
        MassageChairLocation massageChairLocation4=new MassageChairLocation();
        massageChairLocation4.setQuantity(2);
        massageChairLocation4.setMCL_Id(5);
        massageChairLocation4.setMCL_Area("海口");
        massageChairLocation4.setMCL_State(0);
        massageChairLocation4.setMCL_LocationX(20.061562);
        massageChairLocation4.setMCL_LocationY(110.339172);
        massageChairLocation4.setMCL_location("嘉汇广场");
        massageChairLocation4.save();
        MassageChairLocation massageChairLocation5=new MassageChairLocation();
        massageChairLocation5.setQuantity(2);
        massageChairLocation5.setMCL_Id(6);
        massageChairLocation5.setMCL_Area("海口");
        massageChairLocation5.setMCL_State(0);
        massageChairLocation5.setMCL_LocationX(20.065755);
        massageChairLocation5.setMCL_LocationY(110.321491);
        massageChairLocation5.setMCL_location("月亮湾花园");
        massageChairLocation5.save();

        //110.330644,20.0558817号楼110.330435,20.056234==110.330902,20.055886
        //110.329502,20.05769一田110.329303,20.057634==110.329598,20.057735
        //110.329126,20.056057五食堂110.328772,20.056249==110.328751,20.055962
        //20.061562  110.339172广场110.338899,20.061427==110.33943,20.061443
//110.321491,20.065755花园110.320445,20.065826==110.322146,20.06564
//        MassageChair massageChair=new MassageChair();
//        massageChair.setVersion("A1");
//        massageChair.setMC_Area("海口");
//        massageChair.setMC_LocationX(20.056739);
//        massageChair.setMC_LocationY(110.328739);
//        massageChair.setNumber(1);
//        massageChair.setMC_State("损坏");
//        massageChair.setMCL_Id(4);
//        massageChair.setFloor(1);
//        massageChair.save();
//        MassageChair massageChair1=new MassageChair();
//        massageChair1.setVersion("B1");
//        massageChair1.setMC_Area("海口");
//        massageChair1.setMC_LocationX(20.055639);
//        massageChair1.setMC_LocationY(110.329239);
//        massageChair1.setNumber(2);
//        massageChair1.setMC_State("未使用");
//        massageChair1.setMCL_Id(2);
//        massageChair1.setFloor(1);
//        massageChair1.save();
//        MassageChair massageChair2=new MassageChair();
//        massageChair2.setVersion("C1");
//        massageChair2.setMC_Area("海口");
//        massageChair2.setMC_LocationX(20.055739);
//        massageChair2.setMC_LocationY(110.326939);
//        massageChair2.setNumber(3);
//        massageChair2.setMC_State("未使用");
//        massageChair2.setMCL_Id(3);
//        massageChair2.setFloor(1);
//        massageChair2.save();
//        MassageChair massageChair3=new MassageChair();
//        massageChair3.setVersion("D1");
//        massageChair3.setMC_Area("海口");
//        massageChair3.setMC_LocationX(19.978526);
//        massageChair3.setMC_LocationY(110.277504);
//        massageChair3.setNumber(4);
//        massageChair3.setMCL_Id(1);
//        massageChair3.setFloor(1);
//        massageChair3.setMC_State("未使用");
////110.277504,19.978526
//        massageChair3.save();
//        MassageChair massageChair4=new MassageChair();
//        massageChair4.setVersion("E1");
//        massageChair4.setMC_Area("海口");
//        massageChair4.setMC_LocationX(19.980462);
//        massageChair4.setMC_LocationY(110.278534);
//        massageChair4.setNumber(5);
//        massageChair4.setMCL_Id(1);
//        massageChair4.setFloor(2);
//        massageChair4.setMC_State("未使用");
////110.278534,19.980462
//        massageChair4.save();
////20.056729,110.328729
//
//        Statement statement=new Statement();
//        statement.setAmount(100);
//        statement.setState("未使用");
//        statement.setId(1);
//        statement.save();
//        Statement statement1=new Statement();
//        statement1.setAmount(100);
//        statement1.setState("已使用");
//        statement1.setId(2);
//        statement1.save();
    }

}
