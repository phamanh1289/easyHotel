package com.example.phamanh.easyhotel.utils;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;

import com.example.phamanh.easyhotel.activity.AuthActivity;
import com.example.phamanh.easyhotel.activity.IntroActivity;
import com.example.phamanh.easyhotel.activity.LoginActivity;
import com.example.phamanh.easyhotel.activity.MainActivity;


public class StartActivityUtils {

    public static void toAuth(Context context) {
        Intent intent = new Intent().setClass(context,
                AuthActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public static void toMain(Context context,Bundle bundleNotification) {
        Intent intent = new Intent().setClass(context,
                MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
        if (bundleNotification != null)
            intent.putExtras(bundleNotification);
        context.startActivity(intent);
    }

    public static void toIntro(Context context) {
        Intent intent = new Intent().setClass(context,
                IntroActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public static void toLogin(Context context) {
        Intent intent = new Intent().setClass(context,
                LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
}
