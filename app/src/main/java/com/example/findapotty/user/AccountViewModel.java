package com.example.findapotty.user;

import android.content.Context;
import android.content.SharedPreferences;

public class AccountViewModel {

    private SharedPreferences sharedPreferences;
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String ONLOGIN = "onlogin";
    private static final String PREFS_NAME = "accountState";

    public AccountViewModel(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveCredential(String username, String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USERNAME, username);
        editor.putString(PASSWORD, password);
        editor.apply();
    }

    public void setLoginState(boolean onLogin) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(ONLOGIN, onLogin);
        editor.apply();
    }

    public void clearCredential() {
        saveCredential(null, null);
        setLoginState(false);
    }

    public String[] getCredential() {
        return new String[] {
                sharedPreferences.getString(USERNAME, ""),
                sharedPreferences.getString(PASSWORD, "")
        };
    }

    public boolean getLoginState() {
        return sharedPreferences.getBoolean(ONLOGIN, false);
    }
}
