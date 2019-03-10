package com.example.zfrproject.deal;

import android.util.Log;
import android.widget.EditText;

public class dealEditToString {
    public dealEditToString(){}
    //判断是否为空，空返回“”，不空返回原值
    public static String dealStringNull(EditText stringEdit){
        String stringSQLite="";
        if(stringEdit.getText().toString().length()!=0&(stringEdit.getText().toString()!=null&stringEdit.getText().toString().trim()!="")){
            stringSQLite=stringEdit.getText().toString().trim();
        }
        return stringSQLite;
    }
    public static boolean surePassword(EditText passwordEdit,EditText passwordAgainEdit){
        boolean surePassword=false;
        if(passwordEdit.getText().toString().trim().equals(passwordAgainEdit.getText().toString().trim())){
            surePassword=true;
        }
        return surePassword;
    }

    public static int dealIntNull(EditText intEdit){
        int intCache;

        if(intEdit.getText().toString().length()!=0&(intEdit.getText().toString()!=null&intEdit.getText().toString().trim()!="")){
            String stringCache=intEdit.getText().toString();
            intCache=Integer.parseInt(stringCache);
        }
        else intCache=0;
        return intCache;
    }
}
