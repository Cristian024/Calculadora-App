package com.example.myapplication.controllers

import android.content.Context
import android.content.SharedPreferences

data class SharedPreferencesController (
    val context: Context
){
    private lateinit var sharedPreferences: SharedPreferences;
    private var contextShared = context;

    fun getBoolean(shared: String, key: String): Boolean{
        sharedPreferences = contextShared.getSharedPreferences(shared, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }

    fun updateBoolean(shared: String, key: String, value: Boolean){
        sharedPreferences = contextShared.getSharedPreferences(shared, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    fun getString(shared: String, key: String): String?{
        sharedPreferences = contextShared.getSharedPreferences(shared, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "") ?: "";
    }

    fun setString(shared: String, key: String, value: String){
        sharedPreferences = contextShared.getSharedPreferences(shared, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(key, value).apply();
    }
}