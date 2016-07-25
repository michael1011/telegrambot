package at.michael1011.telegrambot.commands;

import at.michael1011.telegrambot.tasks.GetUpdate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Bash {

    public Bash(int id, String command) {
        StringBuilder buffer = new StringBuilder();

        try {
            Process p = Runtime.getRuntime().exec(command);
            p.waitFor();

            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;

            while((line = reader.readLine()) != null) {
                buffer.append(line).append("%0A");
            }

        } catch(IOException | InterruptedException e) {
            GetUpdate.sendText(id, e.getCause().toString());
            e.printStackTrace();
        }

        String message = buffer.toString();

        if(!message.equals("")) {
            GetUpdate.sendText(id, message);
        }

    }

}
