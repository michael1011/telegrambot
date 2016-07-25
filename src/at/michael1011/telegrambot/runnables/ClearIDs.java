package at.michael1011.telegrambot.runnables;

import at.michael1011.telegrambot.Main;
import at.michael1011.telegrambot.tasks.GetUpdate;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Map;

import static at.michael1011.telegrambot.tasks.GetUpdate.prop;
import static at.michael1011.telegrambot.tasks.GetUpdate.usedIDsFile;

public class ClearIDs {

    public static void run() {
        ArrayList<String> remove = new ArrayList<>();

        for(Map.Entry<Object, Object> entry : GetUpdate.prop.entrySet()) {
            DateTime date = Main.formatter.parseDateTime((String) entry.getValue());

            DateTime yesterday = DateTime.now().minusDays(1);

            if(date.isBefore(yesterday)) {
                remove.add((String) entry.getKey());
            }

        }

        for(String toRemove : remove) {
            GetUpdate.prop.remove(toRemove);
        }

        Main.writeFile(usedIDsFile, prop);

    }

}

