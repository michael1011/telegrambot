package at.michael1011.telegrambot.commands;

import java.io.IOException;

public class Reboot {

    public Reboot(byte[] password) {
        try {
            Process p = Runtime.getRuntime().exec("sudo reboot");

            p.getOutputStream().write(password);

            p.waitFor();

        } catch(IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

}
