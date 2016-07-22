package at.michael1011.telegrambot.commands;

import at.michael1011.telegrambot.tasks.GetUpdate;
import com.pi4j.system.SystemInfo;

import java.io.IOException;

public class Temperature {

    public Temperature(int id) {
        try {
            GetUpdate.sendText(id, String.valueOf(SystemInfo.getCpuTemperature())+" °C");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            GetUpdate.sendText(id, e.getCause().toString());
        }

    }

}
