package com.example.locationapiusingmvvm;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentLocation extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private WeatherPagerAdapter pagerAdapter;
    private String[] tabTitles = {
          "Current Weather",
          "Forecast Weather"
    };
    private LocationViewModel locationViewModer;

    public CurrentLocation() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current_location, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabLayout = view.findViewById(R.id.weather_tablayout);
        viewPager2 = view.findViewById(R.id.weatherViewPager);
        pagerAdapter = new WeatherPagerAdapter(this);
        viewPager2.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                   tab.setText(tabTitles[position]);
            }
        }).attach();

    }
}
