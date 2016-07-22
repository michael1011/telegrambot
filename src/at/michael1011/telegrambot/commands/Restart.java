package at.michael1011.telegrambot.commands;

import at.michael1011.telegrambot.tasks.GetUpdate;

import java.io.IOException;

public class Restart {

    public Restart(int id) {
        new Exit(id);

        try {
            Process p = Runtime.getRuntime().exec("./AutoRestart.sh");

            GetUpdate.sendText(id, "restarted server");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
