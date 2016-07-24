package at.michael1011.telegrambot.tasks;

import at.michael1011.telegrambot.Main;
import org.joda.time.DateTime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

public class InputReader {

    private static Timer timer;

    public InputReader() {
        timer = new Timer();

        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

                try {
                    String line = in.readLine();

                    if(line.equalsIgnoreCase("close") || line.equalsIgnoreCase("exit")) {
                        in.close();

                        exit(true);

                    } else {
                        DateTime date = new DateTime();

                        System.out.println("["+date.toString(Main.formatter)+"] "+"Didn't find command: "+line);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };

        timer.schedule(task, 500, 500);
    }

    public static void exit(Boolean exit) {
        timer.cancel();
        GetUpdate.cancelTask();

        DateTime date = new DateTime();

        System.out.println("["+date.toString(Main.formatter)+"] ");
        System.out.println("["+date.toString(Main.formatter)+"] "+"Exiting. Bye bye!");

        if(exit) {
            System.exit(0);
        }

    }

}
