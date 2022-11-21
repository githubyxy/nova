package com.yxy.nova.util.wechat.weather;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;

/**
 * @author yuxiaoyu
 * @date 2021/1/22 下午3:52
 * @Description
 */
public class TianQiCityID {
    public static HashMap<String , String> map = new HashMap<String,String>();

    public static void main(String[] args) {

        TianQiCityID ci = new TianQiCityID();
        ci.queryCityID();
        String hangzhou=map.get("杭州");
        String chongqing =map.get("重庆");
        System.out.println("杭州的编码是："+hangzhou+"\n"+"重庆的编码是："+chongqing);


    }

    private void queryCityID(){
//获取包含城市及其编码信息的json字符串；
        TianQiCity jc= new TianQiCity();
        String city=jc.getCitys();
        JSONArray array = JSON.parseArray(city);
        for(int i=0;i<array.size();i++){
            JSONObject jsonObject = array.getJSONObject(i);

            String cityName = jsonObject.getString("cityZh");
            String cityId = jsonObject.getString("id");
            map.put(cityName, cityId);
            //System.out.println((count++)+" : "+x+" -- "+y);

        }

    }

    public HashMap<String,String> getCityIDMap(){
        HashMap<String , String> maps = new HashMap<String,String>();
        queryCityID();
        maps.putAll(map);
        return maps;
    }
}
