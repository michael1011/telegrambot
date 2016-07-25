package at.michael1011.telegrambot.commands;

import at.michael1011.telegrambot.tasks.GetUpdate;

public class Help {

    public Help(int id) {
        GetUpdate.sendText(id, "List of all commands%0A%0A"+
                "<b>bash *command*</b>: Executes *command*%0A"+
                "<b>rbash *command*</b>: Executes *command* as root%0A"+
                "<b>exit</b>: Closes this Telegram server%0A"+
                "<b>hello</b>: Welcome message%0A"+
                "<b>help</b>: Sends this message%0A"+
                "<b>java</b>: Shows information about the java runtime%0A"+
                "<b>ram</b>: Show information about the ram usage%0A"+
                "<b>reboot</b>: Reboots the Raspberry Pi%0A"+
                "<b>restart</b>: Restarts the Telegram server (uses the sh file from the config)%0A"+
                "<b>shutdown</b>: Shuts the Raspberry Pi down%0A"+
                "<b>version</b>: Shows the version of the Telegram server");
    }

}
