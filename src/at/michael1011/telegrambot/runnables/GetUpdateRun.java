package at.michael1011.telegrambot.runnables;

import at.michael1011.telegrambot.Main;
import at.michael1011.telegrambot.commands.*;
import at.michael1011.telegrambot.tasks.GetUpdate;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

import static at.michael1011.telegrambot.tasks.GetUpdate.*;

public class GetUpdateRun {

    private static String command = null;

    private static Boolean shutdown = false, reboot = false;

    public static void run() {
        JSONObject js = GetUpdate.getJsonObject(updateUrl);

        if(js != null) {
            if(js.getBoolean("ok")) {
                JSONArray array = js.getJSONArray("result");

                for(int i = 0; i < array.length(); i++) {
                    // todo: create help command

                    JSONObject object = array.getJSONObject(i);

                    String updateID = String.valueOf(object.getInt("update_id"));

                    if(prop.getProperty(updateID, null) == null) {
                        JSONObject message = object.getJSONObject("message");
                        JSONObject from = message.getJSONObject("from");

                        String configUserName = Main.prop.getProperty(Main.userNameKey);
                        String userName = from.getString("username");

                        String text = message.getString("text");
                        String textLower = text.toLowerCase();

                        DateTime date = new DateTime();

                        prop.setProperty(updateID, date.toString(Main.formatter));

                        Main.writeFile(usedIDsFile, prop);
                        Main.getProperties(usedIDsFile);

                        log.debug("added "+updateID);

                        if(configUserName.equals(userName) || configUserName.equals(Main.userNameVal)) {

                            System.out.println("["+date.toString(Main.formatter)+"] "+userName+" executed command: '"+text+"'");

                            int id = from.getInt("id");

                            if(command != null) {
                                new RBash(id, command, text.getBytes(StandardCharsets.UTF_8));

                                command = null;

                            } else if(shutdown) {
                                Main.writeFile(usedIDsFile, prop);

                                sendText(id, "Shutting down server");

                                new Shutdown(text.getBytes(StandardCharsets.UTF_8));

                                shutdown = false;

                            } else if(reboot) {
                                Main.writeFile(usedIDsFile, prop);

                                sendText(id, "Rebooting server");

                                new Reboot(text.getBytes(StandardCharsets.UTF_8));

                                reboot = false;

                            } else {
                                if(textLower.length() > 4) {
                                    if(textLower.substring(0, 4).equals("bash")) {
                                        new Bash(id, text.substring(5));

                                    } else if(textLower.substring(0, 5).equals("rbash")) {
                                        String password = Main.prop.getProperty(Main.rootPasswordKey);

                                        if(!Main.prop.getProperty(Main.rootPasswordKey).equals(Main.rootPasswordVal)) {
                                            new RBash(id, text.substring(6), password.getBytes(StandardCharsets.UTF_8));

                                        } else {
                                            command = text.substring(6);

                                            sendText(id, "Send your root password.");
                                        }

                                    } else {
                                        executeCommand(textLower, text, id, from);
                                    }

                                } else {
                                    executeCommand(textLower, text, id, from);
                                }

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

    private static void executeCommand(String textLower, String text, int id, JSONObject from) {
        if(text.startsWith("/")) {
            //noinspection ResultOfMethodCallIgnored
            text.replaceFirst("/", "");
        }

        if(textLower.startsWith("/")) {
            //noinspection ResultOfMethodCallIgnored
            text.replaceFirst("/", "");
        }

        switch(textLower) {
            case "hello":
            case "hi":
                new Hello(id, from.getString("first_name"));

                break;

            case "help":
                new Help(id);

                break;

            case "cpu":
                new Cpu(id);

                break;

            case "ram":
                new Ram(id);

                break;

            case "status":
                new Cpu(id);
                new Ram(id);

                break;

            case "java":
                new Java(id);

                break;

            case "version":
            case "v":
                new Version(id);

                break;

            case "exit":
            case "close":
                Main.writeFile(usedIDsFile, prop);

                new Exit(id);

                break;

            case "restart":
                Main.writeFile(usedIDsFile, prop);

                new Restart(id);

                break;

            case "shutdown":
                String password = Main.prop.getProperty(Main.rootPasswordKey);

                if(!password.equals(Main.rootPasswordVal)) {
                    Main.writeFile(usedIDsFile, prop);

                    sendText(id, "Shutting down server");

                    new Shutdown(password.getBytes(StandardCharsets.UTF_8));

                    shutdown = false;

                } else {
                    shutdown = true;

                    sendText(id, "Send your root password.");
                }

                break;

            case "reboot":
                String passwordR = Main.prop.getProperty(Main.rootPasswordKey);

                if(!passwordR.equals(Main.rootPasswordVal)) {
                    Main.writeFile(usedIDsFile, prop);

                    sendText(id, "Rebooting server");

                    new Reboot(passwordR.getBytes(StandardCharsets.UTF_8));

                    reboot = false;

                } else {
                    reboot = true;

                    sendText(id, "Send your root password.");
                }

                break;

            default:
                sendText(id, "Command <b>"+text+"</b> not found. %0AWrite <i>help</i> to get a list of all commands.'");

                break;
        }

    }

}
