package MyBot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class Bot extends TelegramLongPollingBot {

    private static final String username = "ProjectGord_bot";
    private static final String token = "5532637846:AAE8aLSpB95bSqm7lxWhMoZYtYTEd328uNU";
    private final Responser responser = new Responser();

    @Override
    public void onUpdateReceived(Update update) {
        log.info("BOT DATA: " + update.getMessage().getText());

        if(update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText().trim();

            String chatId = update.getMessage().getChatId().toString();

            List<String> responses = new ArrayList<>();

            log.info(message);

            try {
                responses = responser.exec(message);
            } catch (IOException e) {
                e.printStackTrace();
            }

            SendMessage sm = new SendMessage();
            sm.setChatId(chatId);

            if (!responses.isEmpty()) {
                for (String str : responses) {
                    sm.setText(str);

                    try {
                        execute(sm);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }

            } else {
                sm.setText("Ничего не найдено по данному запросу");

                try {
                    execute(sm);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
