package com.example.activityintentdemo;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedManager {
    private static final String PREF_NAME = "UserPrefs";
    private SharedPreferences prefs;

    public SharedManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void registerUser(String username, String password) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(username, password);
        editor.apply();
    }

    public boolean isUserRegistered(String username) {
        return prefs.contains(username);
    }

    public boolean checkPassword(String username, String password) {
        String storedPassword = prefs.getString(username, null);
        return storedPassword != null && storedPassword.equals(password);
    }
}