package com.company.best.friends.weather;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.logging.impl.Log4JLogger;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class Weather {
    private static String APP_KEY = "4bf0a1f234130be257f7b508862f99b8";

    private final Logger logger;
    public Weather() {
        logger = Logger.getLogger(Weather.class);
    }

    private static Map<String, Object> jsonToMap(String str) {
        Map<String, Object> map = new Gson().fromJson(str,
                new TypeToken<HashMap<String, Object>>() {}.getType());
        return map;
    }

    public String getWeatherByCityName(String cityName) {
        String urlString = "http://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=" + APP_KEY +
                "&lang=ua&units=metric";
        return getWeatherJson(urlString);
    }

    public String getWeatherByGeolocation(float lon, float lat) {
        String urlString = "http://api.openweathermap.org/data/2.5/weather?lon=" + lon + "&lat=" + lat + "&appid=" + APP_KEY +
                "&units=metric";
        return getWeatherJson(urlString);
    }

    private String getWeatherJson(String urlString) {
        try {
            StringBuilder result = new StringBuilder();
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection
                    .getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
            bufferedReader.close();
            Map<String, Object> respMap = jsonToMap(result.toString());
            Map<String, Object> mainMap = jsonToMap(respMap.get("main").toString());
            return "Main Temperature: \n" + mapJson(mainMap) + "Wind:\n" + mapJson(jsonToMap(respMap.get("wind").toString()));
        } catch (IOException e) {
            logger.error("Error in getting weather.");
            e.printStackTrace();
        }
        return null;
    }

    public boolean checkTimeFormat (String time) {
       String[] arr = time.split(":");
       return arr.length == 2 && arr[0].length() == 2 && arr[1].length() == 2 && arr[0].matches("[0-9]+")
               && arr[1].matches("[0-9]+") && checkHoursMinutes(arr);
    }

    public boolean checkHoursMinutes(String[] arr) {
        return !(Double.parseDouble(arr[0]) > 23) && !(Double.parseDouble(arr[1]) > 59);
    }

    private String mapJson (Map<String, Object> map) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            stringBuilder.append(entry.getKey()).append("  ->  ").append(entry.getValue().toString()).append(";\n");
        }
        return stringBuilder.toString();
    }
}
