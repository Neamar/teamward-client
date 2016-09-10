package fr.neamar.lolgamedata.pojo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by neamar on 10/09/16.
 */
public class ChampionCounter {
    public String name = null;
    public String image;
    public int mastery = -1;
    public int winRate = -1;

    public ChampionCounter(JSONObject champion, boolean isCounter) throws JSONException {
        image = champion.getString("image");
        name = champion.getString("name");
        if(isCounter) {
            mastery = champion.getInt("mastery");
            winRate = champion.getInt("winRate");
        }

    }
}
