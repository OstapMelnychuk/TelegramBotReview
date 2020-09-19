package com.company.best.friends.message.constants;

import java.util.HashMap;
import java.util.Map;

public class AllCommandsList {
    public static HashMap<String, String> commandsAndDescriptions = new HashMap<>();

    public static String prepareAllCommands() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : commandsAndDescriptions.entrySet()) {
            stringBuilder.append(entry.getKey()).append(" -> ").append(entry.getValue()).append("\n");
        }
        return stringBuilder.toString();
    }

    public static void setCommandsAndDescriptions() {
        commandsAndDescriptions.put("/help", "get all commands;");
        commandsAndDescriptions.put("/getTime", "get bot server time;");
        commandsAndDescriptions.put("/getCurrentWeather", "Enter command and city to get your city current weather," +
                " or just send location;");
        commandsAndDescriptions.put("/mailWeatherEveryDay", "To subscribe enter command, time, in format<hh:mm>," +
                " and city name" +
                "all the command parameters please: Example: \"/mailWeatherEveryDay 18:00 Lviv\"");
    }
}
