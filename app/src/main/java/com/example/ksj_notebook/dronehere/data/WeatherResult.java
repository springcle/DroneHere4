package com.example.ksj_notebook.dronehere.data;

/**
 * Created by ksj_notebook on 2016-06-02.
 */
public class WeatherResult {
    //WeatherWindSun gweather;
    OpenWeatherWind wind;
    OpenWeatherSun sys;
    public OpenWeatherWind getWind() {
        return wind;
    }
    public OpenWeatherSun getSun(){
        return sys;
    }

    public void setGweather(OpenWeatherWind wind) {
        this.wind = wind;
    }
}
