package at.michael1011.telegrambot.runnables;

import at.michael1011.telegrambot.Main;
import at.michael1011.telegrambot.commands.*;
import at.michael1011.telegrambot.tasks.GetUpdate;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import static at.michael1011.telegrambot.tasks.GetUpdate.*;

public class GetUpdateRun {

    public static void run() {
        JSONObject js = GetUpdate.getJsonObject(updateUrl);

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

                            switch(text) {
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
                                    sendText(from.getInt("id"), "Command <b>"+text+"</b> not found. %0AWrite <i>help</i> to get a list of all commands.'");

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

        finished = true;

    }

}
