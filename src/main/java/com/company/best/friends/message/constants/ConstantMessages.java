package com.company.best.friends.message.constants;

public class ConstantMessages {
    public static String START_MESSAGE = "Greetings User ";
    public static String DEFAULT_MESSAGE = "Unknown Command, type \"/help\" for command list";
    public static String GET_TIME_MESSAGE = "Server time: ";
    public static String GET_WEATHER_MESSAGE = "To get weather, enter \"/getCurrentWeather <your city name>\"" +
            "or just send geolocation";
    public static String SUCCESSFUL_MAIL_REQUEST = "Successfully subscribed to daily weather mailing";
    public static String UNSUCCESSFUL_MAIL_REQUEST = "Unsuccessfully subscribed to daily weather mailing. Please check " +
            "all the command parameters please: Example: \"/mailWeatherEveryDay 18:00 Lviv\"";
    public static String MAIL_MESSAGE = "To subscribe enter command, time, in format<hh:mm>," +
            " and city name" +
            "all the command parameters please: Example: \"/mailWeatherEveryDay 18:00 Lviv\"";
}
