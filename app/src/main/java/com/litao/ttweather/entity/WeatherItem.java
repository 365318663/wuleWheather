package com.litao.ttweather.entity;

public class WeatherItem {
    private String date;
    private String weather;
    private String temperature;
    private String air_quality;
    private String wind;
    private String windValue;

    public String getWindValue() {
        return windValue;
    }

    public void setWindValue(String windValue) {
        this.windValue = windValue;
    }

    public String getWind() {
        return wind;

    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String data) {
        this.date = data;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getAir_quality() {
        return air_quality;
    }

    public void setAir_quality(String air_quality) {
        this.air_quality = air_quality;
    }
}
