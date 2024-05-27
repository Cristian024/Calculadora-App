package com.example.myapplication.controllers

import android.content.Context

class ThemeController (context: Context){
    private val SHARED_THEME = "THEME";
    private var sharedPreferences = SharedPreferencesController(context);

    fun getTheme(): Boolean{
        return sharedPreferences.getBoolean(SHARED_THEME, "MODE");
    }

    fun setTheme(value: Boolean){
        sharedPreferences.updateBoolean(SHARED_THEME, "MODE", value);
    }
}