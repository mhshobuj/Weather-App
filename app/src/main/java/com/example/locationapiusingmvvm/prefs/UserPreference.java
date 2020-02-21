package com.example.locationapiusingmvvm.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.locationapiusingmvvm.WeatherUtils;

public class UserPreference {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public UserPreference(Context context){
        sharedPreferences = context.getSharedPreferences(WeatherUtils.TEMP_CON_FILE_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setTempConversionStatus(boolean status){
        editor.putBoolean(WeatherUtils.TEMP_CON_KEY, status);
        editor.commit();
    }

    public boolean isUserSetTempConversionStatus(){
        return sharedPreferences.getBoolean(WeatherUtils.TEMP_CON_KEY,false);
    }
}
