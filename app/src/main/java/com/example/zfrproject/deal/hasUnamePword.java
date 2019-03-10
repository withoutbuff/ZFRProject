package com.example.zfrproject.deal;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

public class hasUnamePword {
    /**
     * 保存用户信息的管理类
     */
    private SharedPreferences pref;

    private static hasUnamePword instance;

    private hasUnamePword() { }

    public static hasUnamePword getInstance() {
        if (instance == null) {
            instance = new hasUnamePword();
        }
            return instance;
    }


        /**
         * userInfo中是否有数据
         */
    public boolean hasUserInfo(Context context) {
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        if (pref != null) {
            if ((!TextUtils.isEmpty(pref.getString("username", "")))
                    && (!TextUtils.isEmpty(pref.getString("password", "")))) {//有数据
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

}
