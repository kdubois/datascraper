package com.kevin;

public class BruvaxYear {
    public String currentYear;
    public String previousYear;

    public BruvaxYear(String currentYear, String previousYear) {
        this.currentYear = currentYear;
        this.previousYear = previousYear;
    }

    public String getCurrentYear(){
        return currentYear;
    }

    public String getPreviousYear(){
        return previousYear;
    }
}
