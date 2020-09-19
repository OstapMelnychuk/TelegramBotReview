package com.company.best.friends.bot;

import com.company.best.friends.message.constants.AllCommandsList;
import com.company.best.friends.message.constants.ConstantMessages;
import com.company.best.friends.weather.UserWeatherSendProfile;
import com.company.best.friends.weather.Weather;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.*;

public class Bot extends TelegramLongPollingBot {
    private final Weather weather;
    private final Logger logger = Logger.getLogger(Bot.class);

    public Bot() {
        this.weather = new Weather();
    }

    public void onUpdateReceived(Update update) {
        User user = update.getMessage().getFrom();
        if (update.getMessage().hasText()) {
            switch (update.getMessage().getText()) {
                case "/start" -> {
                    sendMessage(update.getMessage().getChatId(),
                            ConstantMessages.START_MESSAGE + user.getUserName());
                    logger.info("New User " + user.getFirstName() + " " + user.getLastName() + " with username "
                            + user.getUserName() + " started to use this bot!");
                }
                case "/help" -> {
                    sendMessage(update.getMessage().getChatId(), AllCommandsList.prepareAllCommands());
                    logger.info("User " + user.getFirstName() + " " + user.getLastName() + " with username "
                            + user.getUserName() + " asked for help commands");
                }
                case "/getTime" -> {
                    sendMessage(update.getMessage().getChatId(),
                            ConstantMessages.GET_TIME_MESSAGE
                                    + new Date());
                    logger.info("User " + user.getFirstName() + " " + user.getLastName() + " with username "
                            + user.getUserName() + " asked for server time!");
                }
                case "/getCurrentWeather" -> {
                    sendMessage(update.getMessage().getChatId(),
                            ConstantMessages.GET_WEATHER_MESSAGE);
                    logger.info("User " + user.getFirstName() + " " + user.getLastName() + " with username "
                            + user.getUserName() + " asked for weather but didn`t mention the city!");
                }
                case "/mailWeatherEveryDay" -> {
                    sendMessage(update.getMessage().getChatId(),
                            ConstantMessages.MAIL_MESSAGE);
                    logger.info("User " + user.getFirstName() + " " + user.getLastName() + " with username "
                            + user.getUserName() + " asked for weather subscription but didn`t mention the city!");
                }

            }
            if (update.getMessage().getText().contains("/getCurrentWeather") && update.getMessage().getText().length() > 18
                    && !update.getMessage().hasLocation()) {
                String weatherStr = weather.getWeatherByCityName(update.getMessage().getText().split(" ")[1]);
                sendMessage(update.getMessage().getChatId(), weatherStr);
                if (weatherStr != null) {
                    logger.info("User " + user.getFirstName() + " " + user.getLastName() + " with username "
                            + user.getUserName() + " asked for weather and got it!");
                }

            }
            if (update.getMessage().getText().contains("/mailWeatherEveryDay") &&
                    update.getMessage().getText().length() > 20) {
                String[] request = update.getMessage().getText().split(" ");
                if (weather.getWeatherByCityName(request[2]) != null && weather.checkTimeFormat(request[1])) {
                    UserWeatherSendProfile profile = new UserWeatherSendProfile(update.getMessage().getChatId(),
                            request[2], request[1]);
                    runTask(profile);
                    sendMessage(update.getMessage().getChatId(), ConstantMessages.SUCCESSFUL_MAIL_REQUEST);
                    logger.info("User " + user.getFirstName() + " " + user.getLastName() + " with username "
                            + user.getUserName() + " asked for weather subscription and got it!\n Request: "
                            + Arrays.toString(request));
                } else {
                    sendMessage(update.getMessage().getChatId(), ConstantMessages.UNSUCCESSFUL_MAIL_REQUEST);
                    logger.info("User " + user.getFirstName() + " " + user.getLastName() + " with username "
                            + user.getUserName() + " asked for weather subscription but did error in request!\n"
                            + Arrays.toString(request));
                }
            }
        }

        if (update.getMessage().hasLocation()) {
            sendMessage(update.getMessage().getChatId(), weather.getWeatherByGeolocation(update.getMessage().getLocation().getLongitude(),
                    update.getMessage().getLocation().getLatitude()));
            logger.info("User " + user.getFirstName() + " " + user.getLastName() + " with username "
                    + user.getUserName() + " asked for weather with location and got it!");
        }
    }


    public String getBotUsername() {
        return "test_bot_integrity_unit_id_5_bot";
    }

    public String getBotToken() {
        return "1191508257:AAFOXJnhpk1HwLaMgstNj0Me8bIvytJq1gI";
    }

    private void sendMessage(Long chatId, String messageText) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(messageText);
        setButtons(sendMessage);
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public synchronized void setButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("/getTime"));
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("/help"));
        keyboardSecondRow.add(new KeyboardButton("/getCurrentWeather"));
        keyboardSecondRow.add(new KeyboardButton("/mailWeatherEveryDay"));

        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    public class MailingTask extends TimerTask {
        private final UserWeatherSendProfile profile;
        private Date date;

        public MailingTask(UserWeatherSendProfile profile, Date date) {
            this.profile = profile;
            this.date = date;
        }

        @Override
        public void run() {
            Weather weather = new Weather();
            logger.info("Sending scheduled message to chat: " + profile.getChatId());
            sendMessage(profile.getChatId(), weather.getWeatherByCityName(profile.getCity()));
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.DATE, 1);
            date = c.getTime();
            logger.info("Setting new task for chat: " + profile.getChatId() + " for time: " + profile.getTime() + " for"
                    + " city: " + profile.getCity() + " for date: " + date.toString());
        }
    }

    private void runTask(UserWeatherSendProfile profile) {
        Timer time = new Timer();
        Date date = new Date();
        date.setHours(Integer.parseInt(profile.getTime().split(":")[0]));
        date.setMinutes(Integer.parseInt(profile.getTime().split(":")[1]));
        date.setSeconds(0);
        Bot.MailingTask mailingTask = new MailingTask(profile, date);
        time.schedule(mailingTask, date);
    }

}

