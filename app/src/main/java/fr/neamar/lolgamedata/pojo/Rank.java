package fr.neamar.lolgamedata.pojo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by neamar on 25/03/16.
 */
public class Rank {
    public String tier;
    public String division;

    public Rank(JSONObject rank) throws JSONException {
        tier = rank.getString("tier");
        division = rank.getString("division");

    }
}
