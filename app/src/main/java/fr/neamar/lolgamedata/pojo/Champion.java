package fr.neamar.lolgamedata.pojo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by neamar on 25/03/16.
 */
public class Champion implements Serializable {
    public String name;
    public String imageUrl;
    public int level;
    public int championRank;

    public Champion(JSONObject champion) throws JSONException {
        name = champion.getString("name");
        imageUrl = champion.getString("image");
        level = champion.getInt("level");
        championRank = champion.getInt("champion_rank");
    }
}
