package fr.neamar.lolgamedata.pojo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by neamar on 25/03/16.
 */
public class Champion implements Serializable {
    public static final String UNKNOWN_ROLE = "?";

    public String name;
    public String imageUrl;
    public String splashUrl;
    public String ggUrl;
    public int mastery;
    public int championRank;
    public int ad;
    public int ap;
    public long winRate;

    public String role;

    public Champion(JSONObject champion) throws JSONException {
        name = champion.getString("name");
        imageUrl = champion.getString("image");
        splashUrl = champion.getString("splash");
        ggUrl = champion.getString("gg");
        mastery = champion.getInt("mastery");
        ap = champion.getInt("ap");
        ad = champion.getInt("ad");
        championRank = champion.getInt("champion_rank");
        role = champion.getString("role");

        JSONObject matchup = champion.getJSONObject("matchup");
        winRate = matchup.has("win_rate") ? matchup.getLong("win_rate") : -1;
    }

    public boolean isAp() {
        return ap * .75 > ad;
    }

    public boolean isAd() {
        return ad * .75 > ap;
    }

}
