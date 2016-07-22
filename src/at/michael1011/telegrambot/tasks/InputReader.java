package at.michael1011.telegrambot.tasks;

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

                        exit();

                    } else {
                        System.out.println("Didn't find command: "+line);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };

        timer.schedule(task, 500, 500);

    }

    public static void exit() {
        timer.cancel();
        GetUpdate.cancelTask();

        System.out.println();
        System.out.println("Exiting. Bye bye!");
    }

}
