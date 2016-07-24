package at.michael1011.telegrambot.commands;

import at.michael1011.telegrambot.tasks.GetUpdate;
import com.pi4j.system.SystemInfo;

import java.io.IOException;

public class Ram {

    public Ram(int id) {
        try {
            double free = SystemInfo.getMemoryFree()/1000000;
            double used = SystemInfo.getMemoryUsed()/1000000;
            double total = SystemInfo.getMemoryTotal()/1000000;

            String usage = String.valueOf((used*100)/(total*100));

            GetUpdate.sendText(id, "Free RAM = "+free+" MB%0AUsed RAM = "+used+" MB%0AUsage = "+
                    usage.substring(2, 4)+"%");

        } catch(IOException | InterruptedException e) {
            GetUpdate.sendText(id, e.getCause().toString());
            e.printStackTrace();
        }
    }

}
