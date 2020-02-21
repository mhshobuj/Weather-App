package com.example.locationapiusingmvvm;

import android.util.Range;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClintModel {
    private static final String BASE_URL= "http://api.openweathermap.org/data/2.5/";
    public static final String IMAGE_URL_PREFIX= "https://openweathermap.org/img/wn/";
    private static Retrofit retrofit;

    public static Retrofit getRetrofitClint(){
          if (retrofit !=null){
              return retrofit;
          }

          retrofit = new Retrofit.Builder()
                  .baseUrl(BASE_URL)
                  .addConverterFactory(GsonConverterFactory.create())
                  .build();
          return retrofit;
    }

}
