package br.com.neonpay.neonpayacademy.utils;

import android.content.Context;
import android.content.SharedPreferences;

// Classe para auxiliar no uso do SharedPreferences
public class SharedPrefsHelper {

    // Função para retornar o id do usuario, caso não exista retorna -1
    public static int getUsuarioId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        return prefs.getInt("id_usuario", -1);
    }

    // Função para retornar o token, caso não exista retorna null
    public static String getToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        return prefs.getString("TOKEN", null);
    }

}
