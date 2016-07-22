package at.michael1011.telegrambot.tasks;

import at.michael1011.telegrambot.Main;
import at.michael1011.telegrambot.commands.*;
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

                        for(int i = 0; i < array.length(); i++) {
                            // todo: remove update_ids when they are older than 24 hours
                            // todo: create help command

                            // todo: 'restart' for the Raspberry Pi

                            JSONObject object = array.getJSONObject(i);

                            String updateID = String.valueOf(object.getInt("update_id"));

                            if(prop.getProperty(updateID, null) == null) {
                                JSONObject message = object.getJSONObject("message");
                                JSONObject from = message.getJSONObject("from");

                                String configUserName = Main.prop.getProperty(Main.userNameKey);
                                String userName = from.getString("username");

                                String text = message.getString("text").toLowerCase();

                                DateTime date = new DateTime();

                                prop.setProperty(updateID, date.toString(Main.formatter));

                                Main.writeFile(usedIDsFile, prop);
                                Main.getProperties(usedIDsFile);

                                log.debug("added "+updateID);

                                if(configUserName.equals(userName) ||
                                        configUserName.equals(Main.userNameVal)) {

                                    System.out.println("["+date.toString(Main.formatter)+"] "+userName+" executed command: '"+text+"'");

                                    switch (text) {
                                        case "hello":
                                        case "hi":
                                            new Hello(from.getInt("id"), from.getString("first_name"));

                                            break;

                                        case "temperature":
                                        case "temp":
                                            new Temperature(from.getInt("id"));

                                            break;

                                        case "ram":
                                            new Ram(from.getInt("id"));

                                            break;

                                        case "exit -telegram":
                                        case "exit -te":
                                        case "close -telegram":
                                        case "close -te":
                                            Main.writeFile(usedIDsFile, prop);

                                            new Exit(from.getInt("id"));

                                            break;

                                        case "restart -telegram":
                                        case "restart -te":
                                            Main.writeFile(usedIDsFile, prop);

                                            new Restart(from.getInt("id"));

                                            break;

                                        default:
                                            // todo: send help

                                            break;
                                    }

                                } else {
                                    System.out.println("["+date.toString(Main.formatter)+"] "+userName+" wanted to execute: '"+text+"' (access denied)");

                                    sendText(from.getInt("id"), "Access denied");
                                }

                            }

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
