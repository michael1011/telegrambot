package at.michael1011.telegrambot.tasks;

import at.michael1011.telegrambot.Main;
import at.michael1011.telegrambot.runnables.GetUpdateRun;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class GetUpdate {

    public static final String updateUrl = "https://api.telegram.org/bot"+Main.token+"/getUpdates";

    public static final String usedIDsFile = "usedIDs.properties";
    public static Properties prop;

    private static Timer timer;

    public static Boolean finished = true;

    public static final Logger log = LoggerFactory.getILoggerFactory().getLogger(GetUpdate.class.getName());

    public GetUpdate() {
        prop = new Properties();

        File file = new File(usedIDsFile);

        if(!file.exists()) {
            try {
                //noinspection ResultOfMethodCallIgnored
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            prop = Main.getProperties(usedIDsFile);
        }

        timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if(finished) {
                    finished = false;

                    GetUpdateRun.run();
                }
            }

        };

        timer.schedule(task, 1000, 1000);

    }

    static void cancelTask() {
        timer.cancel();
    }

    public static JSONObject getJsonObject(String urlString) {
        try {
            URL url = new URL(urlString);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(con.getInputStream());

            StringBuilder sb = new StringBuilder();

            int cp;

            while((cp = in.read()) != -1) {
                sb.append((char) cp);
            }

            in.close();

            return new JSONObject(sb.toString());

        } catch(IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void sendText(int id, String text) {
        try {
            String parameters = ("bot"+Main.token+"/sendMessage?chat_id=IDR&parse_mode=HTML&text=").
                    replace("IDR" , String.valueOf(id))+
                    text.replaceAll("\\s", "%20");

            URL post = new URL("https://api.telegram.org/"+parameters);

            HttpURLConnection postCon = (HttpURLConnection) post.openConnection();

            postCon.setRequestMethod("POST");
            postCon.setDoOutput(true);


            BufferedReader in = new BufferedReader(new InputStreamReader(postCon.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();

            log.debug(response.toString());

        } catch (IOException e) {
            sendText(id, e.getCause().toString());
            e.printStackTrace();
        }

    }

}
