package dubois.kevin;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import javax.enterprise.context.ApplicationScoped;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import java.io.IOException;
import java.net.MalformedURLException;

@Path("/")
@RegisterRestClient
@ApplicationScoped
public class DataScraper {

  private static final long TOO_MANY_REQUESTS_PAUSE_MILLIS = 30000L;
  private static final int HTTP_STATUS_TOO_MANY_REQUEST = 429;
  private static final String USER_AGENT = "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)";
  private static final String REFERRER = "http://www.google.com";
  
  @ConfigProperty(name = "searchstring")
  private String SEARCHSTRING;

  @ConfigProperty(name = "bruvaxyear")
  private String bruvaxYearProperty;

  @ConfigProperty(name = "bruvaxurl")
  private String url;

  @GET
  @Path("/getBruvaxYear")
  public BruvaxYear getYear() throws Exception, MalformedURLException, IOException {
    String newYear = getYear(fetchPage(url));
    
    BruvaxYear bruvaxYear = new BruvaxYear(newYear, bruvaxYearProperty);        

    return bruvaxYear;
  }

  private Document fetchPage(String url) throws IOException, InterruptedException {
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

  private String getYear(Document doc) {
    Elements listElements = doc.select("li");

    String result = "";
    for (Element el : listElements) {
      // find the string that mentions age, check what year,
      // and then break because we don't want to pick up the second result
      if (el.text().startsWith(SEARCHSTRING)) {
        int len = SEARCHSTRING.length();
        result = el.text().substring(len, len + 4);
        break;
      }
    }

    return result;
  }
}