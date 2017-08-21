package com.naver.blog.hiddenplaces.db;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Admin on 2017-06-27.
 */

public class MockData {

    public Map<String ,List<String>> city;
    public Map<String ,List<String>> downtown;
    public Map<String, List<String>> place;

    public MockData() {

        city = new HashMap();
        downtown = new HashMap<>();
        place = new HashMap<>();

        List<String> data = new ArrayList<>();
        data.add("Seoul");
        data.add("Jeju Island");
        data.add("Busan");
        data.add("Daegu");
        data.add("Incheon");

        city.put("",data);

        data = new ArrayList<>();
        data.add("Gangnam");
        data.add("Hongdae");
        data.add("Myeongdong");
        data.add("Hye-hwa");
        downtown.put("Seoul",data);

        data = new ArrayList<>();
        data.add("Naksan Park");
        data.add("Ttukseom Resort");
        place.put("Gangnam",data);

        data = new ArrayList<>();
        data.add("Playground");
        data.add("Sang-sang ma-dong");
        data.add("NB Club");
        place.put("Hongdae", data);

    }
    public static void main(String[] args){
        MockData mockData = new MockData();
        List<String> cities = mockData.city.get("");
        for(String city : cities ){
            System.out.println(city);
        }
    }
}