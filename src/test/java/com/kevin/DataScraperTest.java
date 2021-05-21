package com.kevin;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import javax.inject.Inject;

import com.kevin.BruvaxBot;
import com.kevin.BruvaxYear;
import com.kevin.DataScraper;
import com.kevin.ScraperService;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
@QuarkusTest
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
        setup("Toutes les personnes nées en 1975");
        String previousYear = "1975";
        String currentYear = "1975";
        System.setProperty("bruvaxYear", previousYear);
        
        BruvaxYear bruvaxYear = dataScraper.getYear();

        Assertions.assertEquals(currentYear, bruvaxYear.currentYear);   
        Assertions.assertEquals(previousYear, bruvaxYear.previousYear);   
    }

    @Test
    public void testGetYearFail () throws Exception {
        setup("badString");
        String previousYear = "1975";
        String currentYear = "0";
        System.setProperty("bruvaxYear", previousYear);

        BruvaxYear bruvaxYear = dataScraper.getYear();

        Assertions.assertEquals(currentYear, bruvaxYear.currentYear);   
        Assertions.assertEquals(previousYear, bruvaxYear.previousYear);   
    }

}
