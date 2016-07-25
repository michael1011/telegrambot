package at.michael1011.telegrambot;

import at.michael1011.telegrambot.runnables.ClearIDs;
import at.michael1011.telegrambot.tasks.GetUpdate;
import at.michael1011.telegrambot.tasks.InputReader;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.SimpleLogger;

import java.io.*;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

    public final static String version = "1.0.1";

    private static String configName = "config.properties";

    public static String token;

    public static DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");

    public static Properties prop;

    public static String autoRestartFileKey = "autoRestartFile";
    public static String autoRestartFileVal = "Put the name of your restart file here. Without this file the restart command will not work";

    public static String userNameKey = "userName";
    public static String userNameVal = "If you want that only your Telegram account can send commands put your username here (without the @ char). I recommend it.";

    public static String rootPasswordKey = "rootPassword";
    public static String rootPasswordVal = "If you don't insert your root password here you have to send your password via Telegram";

    private static Timer timer;

    public static void main(String[] args) {
        Logger log = LoggerFactory.getILoggerFactory().getLogger(Main.class.getName());

        System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO");

        prop = new Properties();

        File file = new File(configName);

        String telegramTokenKey = "TelegramToken";
        String telegramTokenVal = "paste your token here";

        if(file.exists()) {
            prop = getProperties(configName);

            token = prop.getProperty(telegramTokenKey, telegramTokenVal);

            if(!token.equals(telegramTokenVal)) {
                log.debug("token found");

                new GetUpdate();
                new InputReader();

                timer = new Timer();

                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        ClearIDs.run();
                    }
                };

                timer.schedule(task, 10000, 3600000);

                DateTime date = new DateTime();

                System.out.println("["+date.toString(Main.formatter)+"] "+"Server started");
                System.out.println("["+date.toString(Main.formatter)+"] ");

            } else {
                log.debug("token not changed");

                showInstructions();
            }

        } else {
            log.debug("config file not found");

            File f = new File(configName);

            try {
                //noinspection ResultOfMethodCallIgnored
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            prop.setProperty(telegramTokenKey, telegramTokenVal);
            prop.setProperty(autoRestartFileKey, autoRestartFileVal);
            prop.setProperty(userNameKey, userNameVal);
            prop.setProperty(rootPasswordKey, rootPasswordVal);

            writeFile(configName, prop);

            showInstructions();
        }

    }

    public static void cancelTask() {
        timer.cancel();
    }

    private static void showInstructions() {
        DateTime date = new DateTime();

        System.out.println("["+date.toString(Main.formatter)+"] ");
        System.out.println("["+date.toString(Main.formatter)+"] "+"You have to create a new Telegram bot yourself and paste the token in the "+configName+" file.");
        System.out.println("["+date.toString(Main.formatter)+"] "+"Then you can run this file again.");
        System.out.println("["+date.toString(Main.formatter)+"] ");
    }

    public static Properties getProperties(String fileName) {
        Properties prop = new Properties();

        try {
            InputStream input = new FileInputStream(fileName);

            prop.load(input);

            input.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return prop;

    }

    public static void writeFile(String fileName, Properties prop) {
        try {
            OutputStream output = new FileOutputStream(fileName);

            prop.store(output, null);

            output.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}