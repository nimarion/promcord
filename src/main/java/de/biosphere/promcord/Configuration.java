package de.biosphere.promcord;

import java.lang.reflect.Field;

import org.slf4j.LoggerFactory;

import io.github.cdimascio.dotenv.Dotenv;

public class Configuration {

    public static final String HTTP_PORT;
    public static final String DISCORD_TOKEN;

    static {
        final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        HTTP_PORT = getenv("HTTP_PORT", dotenv);
        DISCORD_TOKEN = getenv("DISCORD_TOKEN", dotenv);

        try {
            checkNull();
            LoggerFactory.getLogger(Configuration.class).info("Configuration loaded!");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static String getenv(final String name, final Dotenv dotenv) {
        if (System.getenv(name) != null) {
            return System.getenv(name);
        } else if (dotenv.get(name) != null) {
            return dotenv.get(name);
        }
        return null;
    }

    private static void checkNull() throws IllegalAccessException {
        for (Field f : Configuration.class.getDeclaredFields()) {
            LoggerFactory.getLogger(Configuration.class).debug(f.getName() + " environment variable "
                    + (f.get(Configuration.class) == null ? "is null" : "has been loaded"));
        }
    }

}