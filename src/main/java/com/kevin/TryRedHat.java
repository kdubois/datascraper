package com.kevin;

import java.util.Arrays;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TryRedHat {

    public Success tryRedHatAppServices(String yourOrgNeeds) {

        String[] CloudNative = {"Containers", "Runtimes", "Integration"};

        if (Arrays.stream(CloudNative).anyMatch(s -> yourOrgNeeds == s)) {
            try {
                redHat();
            } catch (AnyIssueException issueException) {
                redHatSupport();
            } catch (NotHappyWithRedHatException redHatFailedException) {
                openSourceCommunities();
            }
        }

        return new Success();
    }

    private void redHat() throws AnyIssueException, NotHappyWithRedHatException {
    }

    private void redHatSupport() {
    }

    private void openSourceCommunities() {
    }

    class AnyIssueException extends Exception {

    }

    class NotHappyWithRedHatException extends Exception {

    }

}

class Success {

    @Override
    public String toString() {
        return "Success";
    }
    

}