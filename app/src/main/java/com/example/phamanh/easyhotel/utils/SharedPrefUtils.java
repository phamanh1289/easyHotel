package com.example.phamanh.easyhotel.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPrefUtils {

    public static String getString(Context context, String key) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return preferences.getString(key, "");
    }

    public static void setString(Context context, String key,
                                 String content) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context)
                .edit();
        edit.putString(key, content);
        edit.apply();
    }

    public static void setBoolean(Context context, String key,
                                  boolean content) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context)
                .edit();
        edit.putBoolean(key, content);
        edit.apply();
    }

    public static void removeKey(Context context, String key) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context)
                .edit();
        edit.remove(key);
        edit.apply();
    }

    public static int getInt(Context context, String key) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return preferences.getInt(key, 0);
    }

    public static boolean getBoolean(Context context, String key) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return preferences.getBoolean(key, false);
    }

    public static void setInt(Context context, String key,
                              int content) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context)
                .edit();
        edit.putInt(key, content);
        edit.apply();
    }

    public static void saveLogin(Context context, String mail, String pass) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context)
                .edit();
        edit.putString(Constant.TYPE_LOGIN, Constant.LOGIN_NORMAL);
        edit.putString(Constant.DEVICE_TOKEN, mail);
        edit.putString(Constant.PASSWORD, pass);
        edit.apply();
    }

    public static void saveLoginSocial(Context context, String deviceToken) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context)
                .edit();
        edit.putString(Constant.TYPE_LOGIN, Constant.LOGIN_SOCIAL);
        edit.putString(Constant.DEVICE_TOKEN, deviceToken);
        edit.apply();
    }

    public static void removeLogout(Context context) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context)
                .edit();
        edit.putString(Constant.TYPE_LOGIN, "");
        edit.putString(Constant.DEVICE_TOKEN, "");
        edit.apply();
    }
}
