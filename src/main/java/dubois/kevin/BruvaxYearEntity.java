package dubois.kevin;

import org.eclipse.microprofile.config.inject.ConfigProperty;

public class BruvaxYearEntity{
   
    @ConfigProperty(name = "bruvaxYear")
    public static String year;
}
