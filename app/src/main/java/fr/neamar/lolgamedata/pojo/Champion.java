package fr.neamar.lolgamedata.pojo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Champion implements Serializable {
    public static final String UNKNOWN_ROLE = "?";

    public int id;

    public String name;
    public String imageUrl;
    public String splashUrl;
    public String ggUrl;
    public int mastery;

    // Can be -1 on error
    public int championRank;
    public int ad;
    public int ap;
    public long winRate;

    public String role;

    public Champion(JSONObject champion) throws JSONException {
        id = champion.getInt("id");
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
