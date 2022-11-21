package com.yxy.nova.util.wechat.weather;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yxy.nova.util.SimpleHttpClient;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author yuxiaoyu
 * @date 2021/1/22 下午3:56
 * @Description
 */
public class TianQiWeatherHelper {

    private SimpleHttpClient simpleHttpClient;

    public TianQiWeatherHelper () {
    }

    /**根据城市名称查询城市天气*/
    @SuppressWarnings("unchecked")
    public String getWeatherReportByCityName(String cityName){
        TianQiCityID ci = new TianQiCityID();
        HashMap<String,String> mp=ci.getCityIDMap();
        String x=getWeatherReport(mp.get(cityName), "");
        return x;
    }

    /**根据ip查询城市天气*/
    @SuppressWarnings("unchecked")
    public String getWeatherReportByIP(String ip){
        String x=getWeatherReport("", ip);
        return x;
    }

    /**根据城市编码查询城市天气*/
    public  String getWeatherReport(String cityId, String ip){
        String url = "https://v0.yiketianqi.com/api";
        NameValuePair[] data ={
                new BasicNameValuePair("version","v61"),
                new BasicNameValuePair("appid","11978966"),
                new BasicNameValuePair("appsecret","m7uAdZqQ"),
                new BasicNameValuePair("ip",ip),
                new BasicNameValuePair("cityid",cityId)
        };
        try {
            String result = simpleHttpClient.get(url, data);
            return parseJSONWeather(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String parseJSONWeather(String jsonStr){
        String weather ="";
        JSONObject obj = JSON.parseObject(jsonStr);
        //System.out.println(obj1.get("temp1"));
        weather="地区："+(String)obj.get("city")+" \n"+"天气："+(String)obj.get("wea")+"\n" +
                "气温："+(String)obj.get("tem2")+"~"+(String)obj.get("tem1")+"\n"+
                "实时温度："+(String)obj.get("tem")+"\n"+
                "当前建议："+(String)obj.get("air_tips");

        return weather;
    }

    public SimpleHttpClient getSimpleHttpClient() {
        return simpleHttpClient;
    }

    public void setSimpleHttpClient(SimpleHttpClient simpleHttpClient) {
        this.simpleHttpClient = simpleHttpClient;
    }
}

