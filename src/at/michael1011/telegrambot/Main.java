package at.michael1011.telegrambot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.SimpleLogger;

import java.io.*;
import java.util.Properties;

public class Main {

    private static String configName = "config.properties";

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";

    private static Properties prop;

    public static void main(String[] args) {
        Logger log = LoggerFactory.getILoggerFactory().getLogger(Main.class.getName());

        System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO");

        prop = new Properties();

        File file = new File(configName);

        String telegramTokenKey = "TelegramToken";
        String telegramTokenVal = "paste your token here";

        if(file.exists()) {
            prop = getProperties();

            if(!prop.getProperty(telegramTokenKey, telegramTokenVal).equals(telegramTokenVal)) {

            } else {
                showInstructions();
            }

        } else {
            log.debug("File not found. Ignore this if this is the first start of the app.");

            File f = new File(configName);

            try {
                //noinspection ResultOfMethodCallIgnored
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            prop.setProperty(telegramTokenKey, telegramTokenVal);

            writeFile();

            showInstructions();
        }

    }

    private static void showInstructions() {
        System.out.println();
        System.out.println("You have to "+ANSI_RED+"create a new Telegram bot yourself"+ANSI_RESET+
                " and paste the token in the "+configName+" file.");

        System.out.println("Then you can run this file again.");
        System.out.println();
    }

    private static Properties getProperties() {
        Properties prop = new Properties();

        try {
            InputStream input = new FileInputStream(configName);

            prop.load(input);

            input.close();

        } catch(IOException e) {
            e.printStackTrace();
        }

        return prop;

    }

    private static void writeFile() {
        try {
            OutputStream output = new FileOutputStream(configName);

            prop.store(output, null);

            output.close();

        } catch(IOException e) {
            e.printStackTrace();
        }

    }

}
