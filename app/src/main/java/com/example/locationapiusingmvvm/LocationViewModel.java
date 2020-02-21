package com.example.locationapiusingmvvm;

import android.location.Location;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LocationViewModel extends ViewModel {
        private MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>();
        private MutableLiveData<String> searchCity = new MutableLiveData<>();
        private MutableLiveData<String> units = new MutableLiveData<>();

    public void setLocationMutableLiveData(Location location) {
       locationMutableLiveData.setValue(location);
    }

    public MutableLiveData<Location> getLocationMutableLiveData() {
        return locationMutableLiveData;
    }

    public MutableLiveData<String> getSearchCity() {
        return searchCity;
    }

    public MutableLiveData<String> getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units.postValue(units);
    }

    public void setSearchCity(String city) {
        searchCity.postValue(city);


    }
}
