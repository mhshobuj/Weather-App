package com.example.locationapiusingmvvm.viewmodels;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.locationapiusingmvvm.RetrofitClintModel;
import com.example.locationapiusingmvvm.WeatherServiceApi;
import com.example.locationapiusingmvvm.forcast.ForecastWeatherResponseBody;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForecastWeatherViewModel extends ViewModel {
    private MutableLiveData<ForecastWeatherResponseBody> responseMLD =
            new MutableLiveData<>();

    public MutableLiveData<ForecastWeatherResponseBody> getForecastDataUsingLocation(
            Location location, String units, String apiKey
    ){
        String endUrl = String.format("forecast/daily?lat=%f&lon=%f&cnt=7&units=%s&appid=%s",
                location.getLatitude(), location.getLongitude(), units, apiKey);
        WeatherServiceApi serviceApi = RetrofitClintModel.getRetrofitClint()
                .create(WeatherServiceApi.class);

        serviceApi.getForecastWeatherData(endUrl)
                .enqueue(new Callback<ForecastWeatherResponseBody>() {
                    @Override
                    public void onResponse(Call<ForecastWeatherResponseBody> call, Response<ForecastWeatherResponseBody> response) {
                        if (response.isSuccessful()){
                            ForecastWeatherResponseBody responseBody = response.body();
                            responseMLD.postValue(responseBody);

                        }
                    }

                    @Override
                    public void onFailure(Call<ForecastWeatherResponseBody> call, Throwable t) {
                        Log.e("forecast", "onFailure: "+t.getLocalizedMessage());
                    }
                });
        return responseMLD;
    }

    public MutableLiveData<ForecastWeatherResponseBody> getForecastDataUsingCity(
            String city, String units, String apiKey
    ){
        String endUrl = String.format("forecast/daily?q=%s&cnt=7&units=%s&appid=%s",
                city, units, apiKey);
        WeatherServiceApi serviceApi = RetrofitClintModel.getRetrofitClint()
                .create(WeatherServiceApi.class);

        serviceApi.getForecastWeatherData(endUrl)
                .enqueue(new Callback<ForecastWeatherResponseBody>() {
                    @Override
                    public void onResponse(Call<ForecastWeatherResponseBody> call, Response<ForecastWeatherResponseBody> response) {
                        if (response.isSuccessful()){
                            ForecastWeatherResponseBody responseBody = response.body();
                            responseMLD.postValue(responseBody);

                        }
                    }
                    @Override
                    public void onFailure(Call<ForecastWeatherResponseBody> call, Throwable t) {
                        Log.e("forecast", "onFailure: "+t.getLocalizedMessage());
                    }
                });
        return responseMLD;
    }
}
