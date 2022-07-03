package com.cagdasmarangoz.notes.database

import android.content.Context
import android.content.SharedPreferences

class DarkModeData(context: Context) {

    private var sharedPreferences: SharedPreferences= context.getSharedPreferences("file",Context.MODE_PRIVATE)


    fun SetDarkModeState(state:Boolean?){
        val editor:SharedPreferences.Editor =sharedPreferences.edit()
        editor.putBoolean("darkMode",state!!)
        editor.apply()
    }

    fun loadDarkModeState():Boolean?{
        val state:Boolean=sharedPreferences.getBoolean("darkMode",false)
        return (state)
    }

}