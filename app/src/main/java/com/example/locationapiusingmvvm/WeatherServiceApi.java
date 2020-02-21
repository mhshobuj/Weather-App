package com.example.locationapiusingmvvm;

import com.example.locationapiusingmvvm.current.CurrentWeatherResponseBody;
import com.example.locationapiusingmvvm.forcast.ForecastWeatherResponseBody;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface WeatherServiceApi {

    @GET()
    Call<CurrentWeatherResponseBody> getCurrentWeatherData(@Url String endUrl);

    @GET()
    Call<ForecastWeatherResponseBody> getForecastWeatherData(@Url String endUrl);


}
