package com.memorykeeper.memory_keeper.Service;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    private final DefaultMessageService messageService;
    private final String fromNumber;

    public SmsService(@Value("${sms.api.key}") String apiKey,
                      @Value("${sms.api.secret}") String apiSecret,
                      @Value("${sms.from.number}") String fromNumber) {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
        this.fromNumber = fromNumber;
    }

    public SingleMessageSentResponse sendSms(String to, String text) {
        Message message = new Message();
        message.setFrom(fromNumber);
        message.setTo(to);
        message.setText(text);

        return this.messageService.sendOne(new SingleMessageSendingRequest(message));
    }
}

