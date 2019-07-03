package de.biosphere.wtp;

import io.sentry.Sentry;
import org.slf4j.LoggerFactory;

public class Launcher {

    public static void main(String... args){
        if (System.getenv("SENTRY_DSN") != null || System.getProperty("sentry.properties") != null) {
            Sentry.init();
        }
        try {
            new WheresTheParty();
        } catch (Exception exception){
            LoggerFactory.getLogger(Launcher.class).error("Encountered exception while initializing the bot!", exception);
        }
    }
}
