package fr.neamar.lolgamedata.pojo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Summoner implements Serializable {
    public String id;
    public String name;
    public int level;

    Summoner(JSONObject summoner) throws JSONException {
        id = summoner.getString("id");
        name = summoner.getString("name");
        level = summoner.getInt("level");
    }
}
