package at.michael1011.telegrambot.commands;

import at.michael1011.telegrambot.Main;
import at.michael1011.telegrambot.tasks.GetUpdate;

import org.json.JSONObject;

public class Hello {

    private static final String getMeUrl = "https://api.telegram.org/bot"+Main.token+"/getMe";

    public Hello(int id, String userName) {
        JSONObject object = GetUpdate.getJsonObject(getMeUrl);

        if(object != null) {
            JSONObject result = object.getJSONObject("result");

            GetUpdate.sendText(id, "Hello "+userName+"! I am "+result.getString("first_name")+" aka "+
                    result.getString("username")+" your personal Raspberry Pi Bot. "
                    +"Write 'help' to get a list of all commands");

        } else {
            GetUpdate.sendText(id, "JSONObject not found: "+getMeUrl);
        }

    }

}
