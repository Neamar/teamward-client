package fr.neamar.lolgamedata.pojo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Rank implements Serializable {
    public String tier;
    public String division;
    public String queue;
    public String oldTier;
    public int wins = -1;
    public int losses = -1;

    Rank(JSONObject rank, String oldTier) throws JSONException {
        tier = rank.getString("tier");
        division = rank.getString("division");
        queue = rank.getString("queue");

        this.oldTier = oldTier;
    }
}
