package com.kevin;

import jakarta.enterprise.context.ApplicationScoped;

import org.apache.camel.builder.RouteBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class BruvaxBot extends RouteBuilder{

    @ConfigProperty(name = "token")
    String telegramToken;

    @ConfigProperty(name = "chatid")
    String chatId;

    @Override
    public void configure() throws Exception {                        
        from("timer:tick?repeatCount=1")        
        .bean(DataScraper.class, "getYear")
        .log("Current year from getBruvaxYear is ${body.currentYear}")
        .log("Previous year from getBruvaxYear is ${body.previousYear}")
        .choice().when().simple("${body.currentYear} < ${body.previousYear}")
        .setBody().simple("The current vaccination registration age has changed and is now > ${body.currentYear} years old!")
        .log("Sending to Telegram")
        .choice().when(x -> Integer.parseInt(chatId) != 0)
        .to("telegram:bots?authorizationToken=" + telegramToken + "&chatId=" + chatId)
        .log("message sent");         
    }
}
