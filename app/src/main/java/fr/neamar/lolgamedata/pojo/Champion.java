package fr.neamar.lolgamedata.pojo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Champion implements Serializable {

    public int id;

    public String name;
    public String imageUrl;
    public String ggUrl;
    public int mastery;
    public int points;

    // Can be -1 on error
    public int championRank;
    public int ad;
    public int ap;

    Champion(JSONObject champion) throws JSONException {
        id = champion.getInt("id");
        name = champion.getString("name");
        imageUrl = champion.getString("image");
        ggUrl = champion.getString("gg");
        mastery = champion.getInt("mastery");
        points = champion.getInt("points");
        ap = champion.getInt("ap");
        ad = champion.getInt("ad");
        championRank = champion.getInt("champion_rank");
    }

    public boolean isAp() {
        return ap * .75 > ad;
    }

    public boolean isAd() {
        return ad * .75 > ap;
    }
}
