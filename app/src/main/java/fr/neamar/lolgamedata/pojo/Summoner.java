package fr.neamar.lolgamedata.pojo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Summoner implements Serializable {
    public long id;
    public String name;
    public int level;

    public Summoner(JSONObject summoner) throws JSONException {
        id = summoner.getLong("id");
        name = summoner.getString("name");
        level = summoner.getInt("level");
    }
}
