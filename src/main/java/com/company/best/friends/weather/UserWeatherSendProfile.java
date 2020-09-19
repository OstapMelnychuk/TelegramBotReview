package com.company.best.friends.weather;

import java.util.ArrayList;

public class UserWeatherSendProfile {
    private Long chatId;
    private String city;
    private String time;
    public static ArrayList<UserWeatherSendProfile> userWeatherSendProfileList = new ArrayList<>();

    public UserWeatherSendProfile(Long chatId, String city, String time) {
        this.chatId = chatId;
        this.city = city;
        this.time = time;
        userWeatherSendProfileList.add(this);
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
