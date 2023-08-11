package com.kevin;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import jakarta.inject.Inject;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.kubernetes.client.WithKubernetesTestServer;


@QuarkusTest
@WithKubernetesTestServer
public class DataScraperTest {

    @InjectMock 
    BruvaxBot bruvaxBot;

    @InjectMock 
    ScraperService scraperService;

    @Inject
    DataScraper dataScraper;
    
    public void setup(String inputString) throws Exception {    
        Element el = new Element("li");
        el.text(inputString);
        Elements els = new Elements(el);
        Document document = Mockito.mock(Document.class);
        
        when(document.select("li")).thenReturn(els);        
        when(scraperService.fetchPage(any())).thenReturn(document);   
    }

    @Test
    public void testGetYear () throws Exception {
        setup("Toutes les personnes à partir de 16 ans");
        String previousYear = "18";
        String currentYear = "16";
        
        BruvaxYear bruvaxYear = dataScraper.getYear();

        Assertions.assertEquals(currentYear, bruvaxYear.currentYear);   
        Assertions.assertEquals(previousYear, bruvaxYear.previousYear);   
    }

    @Test
    public void testGetYearFail () throws Exception {
        setup("badString");
        String previousYear = "18";
        String currentYear = "0";

        BruvaxYear bruvaxYear = dataScraper.getYear();

        Assertions.assertEquals(currentYear, bruvaxYear.currentYear);   
        Assertions.assertEquals(previousYear, bruvaxYear.previousYear);   
    }

}
