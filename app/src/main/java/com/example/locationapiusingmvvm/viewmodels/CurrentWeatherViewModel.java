package com.example.locationapiusingmvvm.viewmodels;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.locationapiusingmvvm.RetrofitClintModel;
import com.example.locationapiusingmvvm.WeatherServiceApi;
import com.example.locationapiusingmvvm.current.CurrentWeatherResponseBody;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurrentWeatherViewModel extends ViewModel {
    private MutableLiveData<CurrentWeatherResponseBody> currentWeatherResponseBody =
            new MutableLiveData<>();




    public MutableLiveData<CurrentWeatherResponseBody> getCurrentDataUsingLocation(Location location, String apiKey, String units){
        WeatherServiceApi serviceApi = RetrofitClintModel.getRetrofitClint()
                .create(WeatherServiceApi.class);
        String endurl = String.format("weather?lat=%f&lon=%f&units=%s&appid=%s",
                location.getLatitude(), location.getLongitude(), units, apiKey);
        serviceApi.getCurrentWeatherData(endurl)
                .enqueue(new Callback<CurrentWeatherResponseBody>() {
                    @Override
                    public void onResponse(Call<CurrentWeatherResponseBody> call, Response<CurrentWeatherResponseBody> response) {
                        if (response.isSuccessful()){
                            currentWeatherResponseBody.postValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<CurrentWeatherResponseBody> call, Throwable t) {
                        Log.e("currentWeather", "onFailure: "+t.getLocalizedMessage());
                    }
                });

        return currentWeatherResponseBody;
    }

    public MutableLiveData<CurrentWeatherResponseBody> getCurrentDataUsingCity(String city, String apiKey, String units){
        WeatherServiceApi serviceApi = RetrofitClintModel.getRetrofitClint()
                .create(WeatherServiceApi.class);
        String endurl = String.format("weather?q=%s&units=%s&appid=380199723cebdb85ef2e16cc30cee5b6",
                city, units, apiKey);
        serviceApi.getCurrentWeatherData(endurl)
                .enqueue(new Callback<CurrentWeatherResponseBody>() {
                    @Override
                    public void onResponse(Call<CurrentWeatherResponseBody> call, Response<CurrentWeatherResponseBody> response) {
                        if (response.isSuccessful()){
                            currentWeatherResponseBody.postValue(response.body());
                            Log.e("citycheck", "onResponse: "+currentWeatherResponseBody.getValue().getName());
                        }
                    }

                    @Override
                    public void onFailure(Call<CurrentWeatherResponseBody> call, Throwable t) {
                        Log.e("currentWeather", "onFailure: "+t.getLocalizedMessage());
                    }
                });

        return currentWeatherResponseBody;
    }


}
