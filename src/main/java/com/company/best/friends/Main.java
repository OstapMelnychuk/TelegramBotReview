package com.company.best.friends;

import com.company.best.friends.bot.Bot;
import com.company.best.friends.message.constants.AllCommandsList;
import org.apache.log4j.BasicConfigurator;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class Main {
    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi botAPI = new TelegramBotsApi();
        AllCommandsList.setCommandsAndDescriptions();
        BasicConfigurator.configure();
        try {
            botAPI.registerBot(new Bot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
