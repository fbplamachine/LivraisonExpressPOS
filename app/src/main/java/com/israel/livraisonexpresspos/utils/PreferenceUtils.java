package com.israel.livraisonexpresspos.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtils {
    public static final String PREF_NAME = "livrexPrefs";
    public static final String SELECTED_CITY = "selectedCity";
    public static final String ACCESS_TOKEN = "accessToken";
    public static final String CURRENT_USER = "current_user";
    public static final String MODULES = "modules";
    public static final String MODULES_ = "modules_";
    public static final String CITIES = "cities";
    public static final String PRODUCTS = "products_";
    public static final String ALREADY_CREATED = "alreadyCreated";
    public static final String LAST_CONNECTION = "lastConnection";
    public static final String LAST_SYNC_DATE = "lastSyncDate";
    public static final String SEARCH_PATTERN_ = "searchPattern";
    public static final String GENERIC_PRODUCT = "genericProduct";
    public static final String INTERN_ORDER = "internOrder";
    public static final String QUARTERS = "quarters";

    public static SharedPreferences getPrefs(Context context){
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static String getString(Context context, String key){
        return getPrefs(context).getString(key, "");
    }

    public static int getInt(Context context, String key){
        return getPrefs(context).getInt(key, 0);
    }

    public static long getLong(Context context, String key){
        return getPrefs(context).getLong(key, 0);
    }

    public static boolean getBoolean(Context context, String key){
        return getPrefs(context).getBoolean(key, false);
    }

    public static boolean getTrueBoolean(Context context, String key){
        return getPrefs(context).getBoolean(key, true);
    }

    public static void setString (Context context, String key, String value){
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void setInt (Context context, String key, int value){
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static void setLong (Context context, String key, long value){
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static void setBoolean (Context context, String key, boolean value){
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void clear(Context context){
        getPrefs(context).edit().clear().apply();
    }
}