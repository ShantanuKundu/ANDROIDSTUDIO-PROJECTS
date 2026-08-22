package com.example.assignment1_b;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPrefsHelper {
    private static final String PREF_NAME = "registered_users";

    public static boolean isUsernameTaken(Context context, String username) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.contains(username);
    }

    public static void saveUser(Context context, String username, String password) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(username, password);
        editor.apply();
    }

    public static boolean validateUser(Context context, String username, String password) {
        // Built-in admin account
        if (username.equals("admin") && password.equals("admin123")) {
            return true;
        }
        
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String savedPassword = prefs.getString(username, null);
        return savedPassword != null && savedPassword.equals(password);
    }
}