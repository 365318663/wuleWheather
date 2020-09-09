package com.litao.ttweather.entity;

public class DayWheaher {

      String day;// 日期
      String weather;// 天气
      String temperature;// 温度
      String wind;// 风

    DayWheaher(String d, String w, String t, String wi) {
                     day = d;
                     weather = w;
                     temperature = t;
                     wind = wi;
                 }

              public String toString() {
                     return new String(day + "\t" + weather + "\t" + temperature + "\t" + wind);
                 }
  }
