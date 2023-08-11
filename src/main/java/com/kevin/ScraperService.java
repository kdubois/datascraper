package com.kevin;

import java.io.IOException;

import jakarta.enterprise.context.ApplicationScoped;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import io.quarkus.arc.Unremovable;
import io.quarkus.runtime.annotations.RegisterForReflection;

@Unremovable
@RegisterForReflection
@ApplicationScoped
public class ScraperService {

    private static final long TOO_MANY_REQUESTS_PAUSE_MILLIS = 30000L;
    private static final int HTTP_STATUS_TOO_MANY_REQUEST = 429;
    private static final String USER_AGENT = "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)";
    private static final String REFERRER = "http://www.google.com";

    public Document fetchPage(String url) throws IOException, InterruptedException {
        Document ret;
        
        try {
            ret = Jsoup.connect(url).userAgent(USER_AGENT).referrer(REFERRER).get();
        } catch (HttpStatusException exception) {
            if (exception.getStatusCode() == HTTP_STATUS_TOO_MANY_REQUEST) {
                Thread.sleep(TOO_MANY_REQUESTS_PAUSE_MILLIS);
                ret = Jsoup.connect(url).userAgent(USER_AGENT).referrer(REFERRER).get();
            } else {
                throw exception;
            }
        }

        return ret;
    }

}
