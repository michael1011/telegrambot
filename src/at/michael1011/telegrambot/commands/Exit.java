package at.michael1011.telegrambot.commands;

import at.michael1011.telegrambot.tasks.GetUpdate;
import at.michael1011.telegrambot.tasks.InputReader;

public class Exit {

    public Exit(int id) {
        GetUpdate.sendText(id, "Closing Telegram server");

        InputReader.exit();
    }

}
