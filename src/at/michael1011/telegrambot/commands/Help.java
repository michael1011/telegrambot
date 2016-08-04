package at.michael1011.telegrambot.commands;

import at.michael1011.telegrambot.tasks.GetUpdate;

public class Help {

    public Help(int id) {
        GetUpdate.sendText(id, "List of all commands%0A%0A"+
                "/bash - Executes a command%0A"+
                "/rbash - Executes a command as root%0A"+
                "/cpu - Shows information about the cpu%0A"+
                "/exit - Closes this Telegram bot%0A"+
                "/hello - Welcome message%0A"+
                "/help - Sends this message%0A"+
                "/java - Shows information about the java runtime%0A"+
                "/ram - Shows information about the ram usage%0A"+
                "/reboot - Reboots the Raspberry Pi%0A"+
                "/restart - Restarts the Telegram bot (uses the sh file from the config)%0A"+
                "/shutdown - Shuts the Raspberry Pi down%0A"+
                "/status - Shows /ram and /cpu%0A"+
                "/version - Shows the version of the Telegram bot");
    }

}
