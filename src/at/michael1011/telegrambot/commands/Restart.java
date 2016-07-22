package at.michael1011.telegrambot.commands;

import at.michael1011.telegrambot.Main;
import at.michael1011.telegrambot.tasks.GetUpdate;
import at.michael1011.telegrambot.tasks.InputReader;

import java.io.IOException;

public class Restart {

    public Restart(int id) {
        try {
            String val = Main.prop.getProperty(Main.autoRestartFileKey);

            if(!val.equals(Main.autoRestartFileVal)) {
                Runtime.getRuntime().exec("./"+val);

                GetUpdate.sendText(id, "restarted Telegram server");

            } else {
                GetUpdate.sendText(id, "Please put the name of your restart file in the config to use this feature");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        InputReader.exit(false);

    }

}
