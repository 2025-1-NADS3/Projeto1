package br.com.neonpay.neonpayacademy.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsHelper {

    public static int getUsuarioId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        return prefs.getInt("id_usuario", -1);
    }

    public static String getToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        return prefs.getString("TOKEN", null);
    }

}
