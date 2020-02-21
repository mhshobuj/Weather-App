package com.example.locationapiusingmvvm;

import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.locationapiusingmvvm.current.CurrentWeatherResponseBody;
import com.example.locationapiusingmvvm.prefs.UserPreference;
import com.example.locationapiusingmvvm.viewmodels.CurrentWeatherViewModel;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentWeatherFragment extends Fragment {
    private TextView currentWeatherTempTV, currentWeatherCity_stateTV, currentWeatherCloudinessTV,currentWeatherPressureTV, currentWeatherHumidityTV,
            currentWeatherSunriseTV, currentWeatherSunsetTV, currentWeatherDateTV;
    private ImageView iconIV;
    private LocationViewModel locationViewModel;
    private Location userlocation;
    private CurrentWeatherViewModel currentWeatherViewModel;
    private String units = WeatherUtils.METRIC;
    private String tempSymbol = WeatherUtils.METRIC_SYMBOL;

    public CurrentWeatherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current_weather, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currentWeatherTempTV = view.findViewById(R.id.currentWeatherTemp);
        currentWeatherCloudinessTV = view.findViewById(R.id.currentWeatherCloudiness);
        currentWeatherPressureTV = view.findViewById(R.id.currentWeatherPressure);
        currentWeatherHumidityTV = view.findViewById(R.id.currentWeatherHumidity);
        currentWeatherSunriseTV = view.findViewById(R.id.currentWeatherSunrise);
        currentWeatherSunsetTV = view.findViewById(R.id.currentWeatherSunset);
        currentWeatherCity_stateTV = view.findViewById(R.id.currentWeatherCity_state);
        currentWeatherDateTV = view.findViewById(R.id.currentWeatherDate);
        iconIV = view.findViewById(R.id.weather_condition_icon);
        locationViewModel = new ViewModelProvider(requireActivity()).get(LocationViewModel.class);
        currentWeatherViewModel = new ViewModelProvider(getActivity()).get(CurrentWeatherViewModel.class);

        locationViewModel.getUnits().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                units = s;
                switch (units){
                    case  WeatherUtils.METRIC:
                        tempSymbol = WeatherUtils.METRIC_SYMBOL;
                        break;
                    case  WeatherUtils.IMPERIAL:
                        tempSymbol = WeatherUtils.IMPERIAL_SYMBOL;
                        break;
                }
                getCurrentWeatherData(userlocation);
            }
        });
        locationViewModel.getSearchCity().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String city) {
                currentWeatherViewModel.getCurrentDataUsingCity(city, getString(R.string.weather_Api_key), units)
                        .observe(getActivity(), new Observer<CurrentWeatherResponseBody>() {
                            @Override
                            public void onChanged(CurrentWeatherResponseBody currentWeatherResponseBody) {
                                updateUI(currentWeatherResponseBody);
                            }
                        });
            }
        });
        locationViewModel.getLocationMutableLiveData().observe(getActivity(), new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                userlocation = location;
                getCurrentWeatherData(location);
                //getData(location);
            }
        });
    }

    private void getCurrentWeatherData(Location location){
        String apiKey = getString(R.string.weather_Api_key);
        //latlngTV.setText(location.getLatitude()+","+location.getLongitude());
        currentWeatherViewModel.getCurrentDataUsingLocation(location, apiKey, units)
                .observe(getActivity(), new Observer<CurrentWeatherResponseBody>() {
                    @Override
                    public void onChanged(CurrentWeatherResponseBody currentWeatherResponseBody) {
                        updateUI(currentWeatherResponseBody);
                    }
                });
    }

    private void updateUI(CurrentWeatherResponseBody currentWeatherResponseBody){
        if (currentWeatherResponseBody != null){
            String city = currentWeatherResponseBody.getName();
            double temp = currentWeatherResponseBody.getMain().getTemp();
            String cloudiness = currentWeatherResponseBody.getWeather().get(0).getDescription();
            String icon = currentWeatherResponseBody.getWeather().get(0).getIcon();
            String fullIconUrl = RetrofitClintModel.IMAGE_URL_PREFIX+icon+".png";
            long pressure = currentWeatherResponseBody.getMain().getPressure();
            long humidity = currentWeatherResponseBody.getMain().getHumidity();
            long date = currentWeatherResponseBody.getDt();
            long sunRise = currentWeatherResponseBody.getSys().getSunrise();
            long sunSet = currentWeatherResponseBody.getSys().getSunset();
            String country_state = currentWeatherResponseBody.getSys().getCountry();
            Picasso.get().load(fullIconUrl).into(iconIV);

            currentWeatherCloudinessTV.setText(cloudiness);
            currentWeatherPressureTV.setText(""+pressure+" hpa");
            currentWeatherHumidityTV.setText(""+humidity+"%");
            currentWeatherSunriseTV.setText(getTimeString(sunRise));
            currentWeatherSunsetTV.setText(getTimeString(sunSet));
            currentWeatherDateTV.setText(getDateString(date));
            currentWeatherCity_stateTV.setText("Weather in "+city+", "+country_state);
            currentWeatherTempTV.setText(temp+"\u00B0 "+tempSymbol);

        }
    }

    private String getDateString(long dt){
        Date date = new Date(dt * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        return sdf.format(date);
    }

    private String getTimeString(long time){
        Date date = new Date(time * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        return sdf.format(date);
    }

}
