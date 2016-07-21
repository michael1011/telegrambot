package at.michael1011.telegrambot.tasks;

import at.michael1011.telegrambot.Main;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class GetUpdate {

    private static final String updateUrl = "https://api.telegram.org/bot"+Main.token+"/getUpdates";

    private static String sendMessageUrl = "https://api.telegram.org/bot"+Main.token+"/sendMessage?chat_id=IDR&text=";

    private static Timer timer;

    public GetUpdate() {
        timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    URL url = new URL(updateUrl);

                    HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(con.getInputStream());

                    StringBuilder sb = new StringBuilder();

                    int cp;

                    while((cp = in.read()) != -1) {
                        sb.append((char) cp);
                    }

                    in.close();

                    JSONObject js = new JSONObject(sb.toString());

                    if(js.getBoolean("ok")) {
                        JSONArray array = js.getJSONArray("result");

                        for(int i = 0; i < array.length(); i++) {
                            // todo: add functions here (switch) and add file to put used update_id
                        }

                    }

                } catch(IOException e) {
                    e.printStackTrace();
                }

            }
        };

        timer.schedule(task, 1000, 1000);
    }

    static void cancelTask() {
        timer.cancel();
    }

    public static void sendText(int id, String text) throws IOException {
        URL post = new URL((sendMessageUrl+text).replace("IDR", String.valueOf(id)));

        HttpsURLConnection postCon = (HttpsURLConnection) post.openConnection();

        postCon.setRequestMethod("POST");
        postCon.setDoOutput(true);

        DataOutputStream out = new DataOutputStream(postCon.getOutputStream());

        out.flush();
        out.close();
    }

}
