package com.immx.industrialsupport.notificationworker.services.notification.sender;

import com.immx.industrialsupport.notificationworker.configuration.TelegramProperties;
import com.immx.industrialsupport.notificationworker.dto.NotificationChannel;
import com.immx.industrialsupport.notificationworker.entities.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

/**
 * Отправитель уведомления в телеграм.
 */
@Component
public class TelegramNotificationSender implements INotificationSender {

    @Autowired
    private TelegramProperties properties;

    @Autowired
    private JsonMapper jsonMapper;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Override
    public NotificationChannel getChannel() {
        return NotificationChannel.TELEGRAM;
    }

    @Override
    public void send(Notification notification) {
        try {
            String text =
                    notification.getSubject() + System.lineSeparator() + System.lineSeparator() + "Тип получателя: "
                    + getRecipientTypeLabel(notification) + System.lineSeparator() + "Получатель: "
                    + notification.getRecipientValue() + System.lineSeparator() + System.lineSeparator()
                    + notification.getMessage();

            String requestBody = jsonMapper.writeValueAsString(Map.of(
                    "chat_id",
                    properties.testChatId(),
                    "text",
                    text));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.telegram.org/bot" + properties.botToken() + "/sendMessage"))
                    .header(
                            "Content-Type",
                            "application/json")
                    .timeout(Duration.ofSeconds(10))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new IllegalStateException(
                        "Telegram вернул HTTP " + response.statusCode() + ": " + response.body());
            }
        } catch(InterruptedException ex) {
            Thread.currentThread()
                    .interrupt();
            throw new IllegalStateException(
                    "Отправка Telegram уведомления прервана",
                    ex);
        } catch(Exception ex) {
            throw new IllegalStateException(
                    "Не удалось отправить Telegram уведомление",
                    ex);
        }
    }

    private String getRecipientTypeLabel(Notification notification) {
        return switch(notification.getRecipientType()) {
            case USER -> "пользователь";
            case ROLE -> "роль";
        };
    }
}
