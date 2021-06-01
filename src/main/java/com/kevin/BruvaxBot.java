package com.kevin;

import javax.enterprise.context.ApplicationScoped;

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
        .choice().when().simple("${body.currentYear} > ${body.previousYear}")
        .setBody().simple("The current vaccination registration year has changed and is now ${body.currentYear}!")
        .log("Sending to Telegram with token " + telegramToken + " and chatId " + chatId)
        .choice().when(x -> Integer.parseInt(chatId) != 0)
        .to("telegram:bots?authorizationToken=" + telegramToken + "&chatId=" + chatId)
        .log("message sent");         
    }
}
