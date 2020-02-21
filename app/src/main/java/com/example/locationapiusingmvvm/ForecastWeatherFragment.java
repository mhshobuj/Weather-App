package com.example.locationapiusingmvvm;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.locationapiusingmvvm.adapters.ForecastWeatherAdapter;
import com.example.locationapiusingmvvm.forcast.ForeCastList;
import com.example.locationapiusingmvvm.forcast.ForecastWeatherResponseBody;
import com.example.locationapiusingmvvm.viewmodels.ForecastWeatherViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForecastWeatherFragment extends Fragment {
    private RecyclerView forcastWeatherRV;
    private LocationViewModel locationViewModel;
    private ForecastWeatherAdapter forecastWeatherAdapter;
    private Location userLocation;
    private ForecastWeatherViewModel forecastWeatherViewModel;
    private String units = WeatherUtils.METRIC;

    public ForecastWeatherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forecast_weather, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        forcastWeatherRV = view.findViewById(R.id.forcastWeatherRV);
        locationViewModel = new ViewModelProvider(requireActivity()).get(LocationViewModel.class);
        forecastWeatherViewModel = new ViewModelProvider(getActivity()).get(ForecastWeatherViewModel.class);
        locationViewModel.getUnits().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                units = s;
                getForecastWeatherData(userLocation);
            }
        });


        locationViewModel.getLocationMutableLiveData().observe(getActivity(), new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                 userLocation = location;
                 getForecastWeatherData(location);
                 //getData(location);
            }
        });

        locationViewModel.getSearchCity().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String city) {
                forecastWeatherViewModel.getForecastDataUsingCity(city, units, getString(R.string.weather_Api_key))
                        .observe(getActivity(), new Observer<ForecastWeatherResponseBody>() {
                            @Override
                            public void onChanged(ForecastWeatherResponseBody forecastWeatherResponseBody) {
                                updateUI(forecastWeatherResponseBody);
                            }
                        });
            }
        });
    }

    private void getForecastWeatherData(Location location) {
        forecastWeatherViewModel.getForecastDataUsingLocation(location,units, getString(R.string.weather_Api_key))
                .observe(getActivity(), new Observer<ForecastWeatherResponseBody>() {
                    @Override
                    public void onChanged(ForecastWeatherResponseBody forecastWeatherResponseBody) {
                        updateUI(forecastWeatherResponseBody);
                    }
                });
    }

    public void updateUI(ForecastWeatherResponseBody forecastWeatherResponseBody){
        List<ForeCastList> forecastLists = forecastWeatherResponseBody.getList();

    }

    private void getData(Location location) {
        String apiKey = getString(R.string.weather_Api_key);
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
                            List<ForeCastList> forecastLists = responseBody.getList();
                            Log.e("forecast", "onResponse: "+forecastLists.size());
                            ForecastWeatherAdapter adapter = new ForecastWeatherAdapter(getContext(), forecastLists);
                            LinearLayoutManager llm = new LinearLayoutManager(getContext());
                            forcastWeatherRV.setLayoutManager(llm);
                            forcastWeatherRV.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<ForecastWeatherResponseBody> call, Throwable t) {
                        Log.e("forecast", "onFailure: "+t.getLocalizedMessage());
                    }
                });
    }
}
