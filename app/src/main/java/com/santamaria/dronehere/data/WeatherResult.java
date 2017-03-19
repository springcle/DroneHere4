package com.santamaria.dronehere.data;

/**
 * Created by ksj_notebook on 2016-06-02.
 */
public class WeatherResult {
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
