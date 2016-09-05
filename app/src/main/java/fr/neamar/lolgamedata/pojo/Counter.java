package fr.neamar.lolgamedata.pojo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Counter implements Serializable {
    public String championImage;
    public String championName;

    public String counterImage = null;
    public String counterName;
    public int counterMastery;

    public int winRate = 0;

    public Counter(JSONObject counter) throws JSONException {
        championImage = counter.getJSONObject("champion").getString("image");
        championName = counter.getJSONObject("champion").getString("name");

        if (counter.has("counter") && counter.get("counter") != JSONObject.NULL) {
            counterImage = counter.getJSONObject("counter").getString("image");
            counterName = counter.getJSONObject("counter").getString("name");
            counterMastery = counter.getJSONObject("counter").getInt("mastery");
            winRate = counter.getInt("winRate");
        }
    }
}
