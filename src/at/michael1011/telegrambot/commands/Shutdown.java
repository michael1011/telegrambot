package at.michael1011.telegrambot.commands;

import java.io.IOException;

public class Shutdown {

    public Shutdown(byte[] password) {
        try {
            Process p = Runtime.getRuntime().exec("sudo shutdown -h now");

            p.getOutputStream().write(password);

            p.waitFor();

        } catch(IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

}
