package at.michael1011.telegrambot.commands;

import at.michael1011.telegrambot.tasks.GetUpdate;
import com.pi4j.system.SystemInfo;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

public class Cpu {

    public Cpu(int id) {
        try {
            GetUpdate.sendText(id, "Cpu load: "+getCpuLoad()+"%0ACpu temp: "+String.valueOf(SystemInfo.getCpuTemperature())+" °C");
        } catch (IOException | InterruptedException e) {
            GetUpdate.sendText(id, e.getCause().toString());
            e.printStackTrace();
        }
    }

    private double getCpuLoad() {
        OperatingSystemMXBean op = ManagementFactory.getOperatingSystemMXBean();

        return op.getSystemLoadAverage();
    }

}
