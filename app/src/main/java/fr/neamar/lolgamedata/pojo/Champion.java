package fr.neamar.lolgamedata.pojo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by neamar on 25/03/16.
 */
public class Champion implements Serializable {
    public static String UNKNOWN_ROLE = "?";

    public String name;
    public String imageUrl;
    public int mastery;
    public int championRank;
    public int ad;
    public int ap;
    public long winRate;

    public String role;

    public Champion(JSONObject champion) throws JSONException {
        name = champion.getString("name");
        imageUrl = champion.getString("image");
        mastery = champion.getInt("mastery");
        ap = champion.getInt("ap");
        ad = champion.getInt("ad");
        championRank = champion.getInt("champion_rank");
        role = champion.getString("role");

        JSONObject matchup = champion.getJSONObject("matchup");
        winRate = matchup.has("win_rate") ? matchup.getLong("win_rate") : -1;
    }
}
