package at.michael1011.telegrambot.tasks;

import at.michael1011.telegrambot.Main;
import at.michael1011.telegrambot.commands.Exit;
import at.michael1011.telegrambot.commands.Hello;
import at.michael1011.telegrambot.commands.Restart;
import at.michael1011.telegrambot.commands.Temperature;
import org.joda.time.DateTime;
import org.json.JSONArray;
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

    private static final String updateUrl = "https://api.telegram.org/bot"+Main.token+"/getUpdates";

    private static Timer timer;

    private static final String usedIDsFile = "usedIDs.properties";

    private static Properties prop;

    private static final Logger log = LoggerFactory.getILoggerFactory().getLogger(GetUpdate.class.getName());

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
                JSONObject js = getJsonObject(updateUrl);

                if(js != null) {
                    if(js.getBoolean("ok")) {
                        JSONArray array = js.getJSONArray("result");

                        Boolean added = false;

                        for(int i = 0; i < array.length(); i++) {
                            // todo: remove update_ids when they are older than 24 hours
                            // todo: create help command

                            JSONObject object = array.getJSONObject(i);

                            String updateID = String.valueOf(object.getInt("update_id"));

                            if(prop.getProperty(updateID, null) == null) {
                                JSONObject message = object.getJSONObject("message");
                                JSONObject from = message.getJSONObject("from");

                                String text = message.getString("text").toLowerCase();

                                System.out.println(from.getString("first_name")+" executed command: 'exit'");

                                switch (text) {
                                    case "hello":
                                    case "hi":
                                        new Hello(from.getInt("id"), from.getString("first_name"));

                                        break;

                                    case "temperature":
                                    case "temp":
                                        new Temperature(from.getInt("id"));

                                        break;

                                    case "exit":
                                    case "close":
                                        new Exit(from.getInt("id"));

                                        break;

                                    case "restart":
                                        new Restart(from.getInt("id"));
                                        
                                        break;
                                }

                                prop.setProperty(updateID, new DateTime().toString());

                                added = true;

                                log.debug("added "+updateID);
                            }

                        }

                        if(added) {
                            Main.writeFile(usedIDsFile, prop);
                            Main.getProperties(usedIDsFile);
                        }

                    }

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
            String parameters = ("bot"+Main.token+"/sendMessage?chat_id=IDR&text=").replace("IDR" , String.valueOf(id))+
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
            e.printStackTrace();
        }

    }

}
