package at.michael1011.telegrambot.commands;

import at.michael1011.telegrambot.tasks.GetUpdate;
import com.pi4j.system.SystemInfo;

public class Java {

    // todo: test this
    public Java(int id) {
        GetUpdate.sendText(id,
                "Java Vendor: "+SystemInfo.getJavaVendor()+"%0A"+
                        "Java Version: "+SystemInfo.getJavaVersion()+"%0A"+
                        "Java VM: "+SystemInfo.getJavaVirtualMachine()+"%0A"+
                        "Java Runtime: "+SystemInfo.getJavaRuntime());
    }

}
