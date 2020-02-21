package com.example.locationapiusingmvvm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locationapiusingmvvm.R;
import com.example.locationapiusingmvvm.forcast.ForeCastList;

import java.util.List;

public class ForecastWeatherAdapter extends RecyclerView.Adapter<ForecastWeatherAdapter.ForecastWeatherViewHolder> {
    private Context context;
    private List<ForeCastList> foreCastLists;

    public ForecastWeatherAdapter(Context context, List<ForeCastList> foreCastLists) {
        this.context = context;
        this.foreCastLists = foreCastLists;
    }

    @NonNull
    @Override
    public ForecastWeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.forecast_weather_row, parent, false);
        return new ForecastWeatherViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastWeatherViewHolder holder, int position) {
        holder.forecastWeatherCloudnessTV.setText(foreCastLists.get(position).getClouds());
        holder.forecastWeatherDateTV.setText(foreCastLists.get(position).getDt());
        holder.forecastWeatherTempTV.setText(String.valueOf(foreCastLists.get(position).getTemp()));
        holder.forecastWeatherSunriseTV.setText(foreCastLists.get(position).getSunrise());
        holder.forecastWeatherSunsetTV.setText(foreCastLists.get(position).getSunset());
    }

    @Override
    public int getItemCount() {
        return foreCastLists.size();
    }

    class ForecastWeatherViewHolder extends RecyclerView.ViewHolder {
        TextView forecastWeatherDateTV, forecastWeatherTempTV,
                forecastWeatherSunriseTV, forecastWeatherCloudnessTV,
                forecastWeatherSunsetTV;
        public ForecastWeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            forecastWeatherDateTV = itemView.findViewById(R.id.forecastWeatherDate);
            forecastWeatherTempTV = itemView.findViewById(R.id.forecastWeatherTemp);
            forecastWeatherSunriseTV = itemView.findViewById(R.id.forecastWeatherSunrise);
            forecastWeatherCloudnessTV = itemView.findViewById(R.id.forecastWeatherCloudiness);
            forecastWeatherSunsetTV = itemView.findViewById(R.id.forecastWeatherSunset);
        }
    }
}
