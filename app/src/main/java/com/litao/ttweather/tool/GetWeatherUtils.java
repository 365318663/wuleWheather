package com.litao.ttweather.tool;


import com.litao.ttweather.adapter.WeatherAdapter;
import com.litao.ttweather.entity.WeatherItem;

import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.Node;

import org.htmlparser.Parser;
import org.htmlparser.util.NodeList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class GetWeatherUtils {
    // 从城市名称到城市代码的映射map，如 山东济宁->101120701
    private static HashMap<String, String> cityMap = new HashMap<String, String>();

    // 初始化cityMap
    public static void initCityMap(BufferedReader reader) throws IOException {
        String tempString = null;
        while ((tempString = reader.readLine()) != null) {
            String[] strs = tempString.split("\t");
            if (cityMap.get(strs[0]) == null) {
                cityMap.put(strs[0], strs[1]);
            } else {
               // System.out.println("城市键重复  " + strs[1]);
            }
        }
        reader.close();

        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e1) {
                throw e1;
            }
        }
    }


    // 输入城市名，返回城市代码
    public String cityToCode(String cityName) {
        String code = cityMap.get(cityName);
        if (code == null) {
            return null;
        }
        return code;
    }

    // 输入城市代码，返回该城市的七日天气列表
    public ArrayList<WeatherItem> getSevenDayWeather(String cityCode) throws Exception {
        String urlString = "http://www.weather.com.cn/weather/" + cityCode + ".shtml";
        ArrayList<WeatherItem> sevenDayWeather = new ArrayList<WeatherItem>();
        Parser parser = new Parser((HttpURLConnection) (new URL(urlString)).openConnection());
        HasAttributeFilter t_clearfix = new HasAttributeFilter("class", "t clearfix");
        NodeIterator iterator = parser.extractAllNodesThatMatch(t_clearfix).elements();
        Node ul = iterator.nextNode();
        NodeList lis = ul.getChildren();
        TagNameFilter li = new TagNameFilter("li");
        NodeList liList = lis.extractAllNodesThatMatch(li);
        for (int i = 0; i < liList.size(); i++) {
            NodeList children = liList.elementAt(i).getChildren();
            TagNameFilter p = new TagNameFilter("p");
            NodeList ps = children.extractAllNodesThatMatch(p);
            TagNameFilter h1 = new TagNameFilter("h1");
            TagNameFilter span = new TagNameFilter("span");
            NodeList h = children.extractAllNodesThatMatch(h1);

            String day = h.elementAt(0).toPlainTextString().trim();

            String weather = ps.elementAt(0).toPlainTextString().trim();
            String temperature = ps.elementAt(1).toPlainTextString().trim();
            String wind_value = ps.elementAt(2).toPlainTextString().trim();
            //DayWheaher dw = new DayWheaher(day, weather, temperature, wind);
            //获取风向
            NodeList c = ps.elementAt(2).getChildren();
            NodeList em = c.extractAllNodesThatMatch(span);
            // InputTag inputTag = (InputTag)em.elementAt(0);
            String string =em.elementAt(0).toHtml();
            String[]strings=string.split("title=\"");
            strings=strings[1].split("\"");

            String wind = strings[0];
            WeatherItem item= new WeatherItem();
            item.setDate(day);
            item.setTemperature(temperature);
            item.setWind(wind);
            item.setWindValue(wind_value);
            //System.out.println(wind);
            item.setWeather(weather);
            sevenDayWeather.add(item);
        }
        return sevenDayWeather;
    }

    // 输入城市代码，返回该城市的七日-15日天气列表
    public ArrayList<WeatherItem> getSevenToFifteenDayWeather(String cityCode) throws Exception {
        String urlString = "http://www.weather.com.cn/weather15d/" + cityCode + ".shtml";
        ArrayList<WeatherItem> fifteenDayWeather = new ArrayList<WeatherItem>();
        Parser parser = new Parser((HttpURLConnection) (new URL(urlString)).openConnection());
        HasAttributeFilter t_clearfix = new HasAttributeFilter("class", "t clearfix");
        NodeIterator iterator = parser.extractAllNodesThatMatch(t_clearfix).elements();
        Node ul = iterator.nextNode();
        NodeList lis = ul.getChildren();
        TagNameFilter li = new TagNameFilter("li");
        NodeList liList = lis.extractAllNodesThatMatch(li);
        for (int i = 0; i < liList.size(); i++) {
            NodeList children = liList.elementAt(i).getChildren();
            TagNameFilter span = new TagNameFilter("span");
            NodeList h = children.extractAllNodesThatMatch(span);

            String day = h.elementAt(0).toPlainTextString().trim();

            String weather= h.elementAt(1).toPlainTextString().trim();
            String temperature = h.elementAt(2).toPlainTextString().trim();
            String wind = h.elementAt(3).toPlainTextString().trim();
            String wind_value = h.elementAt(4).toPlainTextString().trim();
            //DayWheaher dw = new DayWheaher(day, weather, temperature, wind);
            //获取风向



            WeatherItem item= new WeatherItem();
            item.setDate(day);
            item.setTemperature(temperature);
            item.setWind(wind);
            item.setWindValue(wind_value);
            //System.out.println(wind);
            item.setWeather(weather);
            fifteenDayWeather.add(item);
        }
        return fifteenDayWeather;
    }

    public ArrayList<WeatherItem> getTodayWeather(String cityCode) throws Exception {
        String urlString = "http://www.weather.com.cn/weather15d/" + cityCode + ".shtml";
        ArrayList<WeatherItem> sevenDayWeather = new ArrayList<WeatherItem>();
        Parser parser = new Parser((HttpURLConnection) (new URL(urlString)).openConnection());
        HasAttributeFilter t_clearfix = new HasAttributeFilter("class", "t clearfix");
        NodeIterator iterator = parser.extractAllNodesThatMatch(t_clearfix).elements();
        Node ul = iterator.nextNode();
        NodeList lis = ul.getChildren();
        TagNameFilter li = new TagNameFilter("li");
        NodeList liList = lis.extractAllNodesThatMatch(li);
        for (int i = 0; i < liList.size(); i++) {
            NodeList children = liList.elementAt(i).getChildren();
            TagNameFilter p = new TagNameFilter("p");
            NodeList ps = children.extractAllNodesThatMatch(p);
            TagNameFilter h1 = new TagNameFilter("h1");
            NodeList h = children.extractAllNodesThatMatch(h1);

            String day = h.elementAt(0).toPlainTextString().trim();
            String weather = ps.elementAt(0).toPlainTextString().trim();
            String temperature = ps.elementAt(1).toPlainTextString().trim();
            String wind = ps.elementAt(2).toPlainTextString().trim();
            //DayWheaher dw = new DayWheaher(day, weather, temperature, wind);
            WeatherItem item= new WeatherItem();
            item.setDate(day);
            item.setTemperature(temperature);
            item.setWind(wind);
            item.setWeather(weather);
            sevenDayWeather.add(item);
        }
        return sevenDayWeather;
    }
 }


