package com.kevin;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.quarkus.arc.Unremovable;
import io.quarkus.runtime.annotations.RegisterForReflection;

@Unremovable
@ApplicationScoped
@RegisterForReflection
public class DataScraper {

  private static final Logger LOG = Logger.getLogger(DataScraper.class);

  @Inject
  ScraperService scraperService;
  
  @ConfigProperty(name = "bruvax.search.string")
  String searchString;

  @ConfigProperty(name = "bruvax.search.yearlen")
  String yearLen;

  @ConfigProperty(name = "bruvax.year")
  String previousYear;

  @ConfigProperty(name = "bruvax.url")
  String url;

  public BruvaxYear getYear() throws Exception {
    String currentYear;

    try {
      currentYear = extractYear(scraperService.fetchPage(url));
    } catch (Exception e) {
      LOG.error("Could not get the bruvax year.  Maybe the website is down or has changed :( .  The error was: " + e.getMessage() );
      currentYear = "0";
    }        
    return new BruvaxYear(currentYear, previousYear);
  }

  private String extractYear(Document doc) {
    Elements listElements = doc.select("li");

    String result = "";
    for (Element el : listElements) {
        // iterate through html link elements and search for a match to our search string
        if (el.text().startsWith(searchString)) {
          int len = searchString.length();  
          // extract the next 4 characters (the year)      
          result = el.text().substring(len, len + Integer.valueOf(yearLen));
          break;
        }
    }

    if (result == "") {
      throw new RuntimeException("Search string " + searchString + "not found.");
    }

    return result;
  }
}