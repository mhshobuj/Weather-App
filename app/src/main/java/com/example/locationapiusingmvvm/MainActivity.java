package com.example.locationapiusingmvvm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.locationapiusingmvvm.prefs.UserPreference;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {

    private FusedLocationProviderClient providerClient;
    private LocationViewModel locationViewModel;
    private LocationRequest locationRequest;
    private UserPreference userPreference;
    private SearchView searchView;
    private String units = WeatherUtils.METRIC;

    private LocationCallback locationCallback = new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            for (Location location : locationResult.getLocations()){
                 locationViewModel.setLocationMutableLiveData(location);
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.weather_menu, menu);
        searchView = (SearchView) menu.findItem(R.id.item_search).getActionView();
        View tempView = menu.findItem(R.id.item_temp_conversion).getActionView();
        final SwitchCompat tempSwitch = tempView.findViewById(R.id.tempConSwitch);
        searchView.setQueryHint("Search city");
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String city) {
                Toast.makeText(MainActivity.this, city, Toast.LENGTH_SHORT).show();
                locationViewModel.setSearchCity(city);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        tempSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                 if (isChecked){
                     units = WeatherUtils.IMPERIAL;
                     userPreference.setTempConversionStatus(true);
                 }
                 else {
                     units = WeatherUtils.METRIC;
                 }
                locationViewModel.setUnits(units);
                if (userPreference.isUserSetTempConversionStatus()){
                    tempSwitch.setChecked(isChecked);
                }
            }
        });
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Weather App");
        providerClient = LocationServices.getFusedLocationProviderClient(this);
        userPreference = new UserPreference(this);
        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!LocationPermissionHelper.hasLocationPermission(this)){
            LocationPermissionHelper.requestLocationPermission(this);
            return;
        }
        getDeviceLastLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (!LocationPermissionHelper.hasLocationPermission(this)){
            Toast.makeText(this, "You denied required permission", Toast.LENGTH_SHORT).show();
            return;
        }
        getDeviceLastLocation();
    }

    private void getDeviceLastLocation() {
            providerClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location == null){
                        createLocationRequest();
                        return;
                    }
                    locationViewModel.setLocationMutableLiveData(location);

                    /*double lat = location.getLatitude();
                    double lng = location.getLongitude();

                    Log.e("currentLocation", "Latitude: "+lat);
                    Log.e("currentLocation", "Longitude: "+lng);*/
                }
            });
    }

    private void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setNumUpdates(1);//not need for ride share type apps

        LocationSettingsRequest locationSettingsRequest = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest).build();

        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> requestTask = settingsClient.checkLocationSettings(locationSettingsRequest);
        requestTask.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                requestDeviceLocationUpdate();
            }
        });
        requestTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException){
                   try {
                       ResolvableApiException apiException = (ResolvableApiException) e;
                       apiException.startResolutionForResult(MainActivity.this, 222);
                   }
                   catch (Exception e1){

                   }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 222 && resultCode == RESULT_OK){
            requestDeviceLocationUpdate();
        }
    }

    private void requestDeviceLocationUpdate() {
         providerClient.requestLocationUpdates(locationRequest,locationCallback, null);
    }

}
