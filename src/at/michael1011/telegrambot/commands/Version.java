package at.michael1011.telegrambot.commands;

import at.michael1011.telegrambot.Main;
import at.michael1011.telegrambot.tasks.GetUpdate;

public class Version {

    public Version(int id) {
        GetUpdate.sendText(id, "Version: "+Main.version);
    }

}
