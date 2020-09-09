package com.litao.ttweather.entity;

public class CityWeatherItem {
    private String weather_details;
    private String temperature;
    private String temperature_range;
    private String province;
    private String city;
    private Boolean isGotWeathter=false;

    public Boolean isGotWeathter() {
        return isGotWeathter;
    }

    public void setIsGotWeathter(Boolean getWeathter) {
        isGotWeathter = getWeathter;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWeather_details() {
        return weather_details;
    }

    public void setWeather_details(String weather_details) {
        this.weather_details = weather_details;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getTemperature_range() {
        return temperature_range;
    }

    public void setTemperature_range(String temperature_range) {
        this.temperature_range = temperature_range;
    }
}
