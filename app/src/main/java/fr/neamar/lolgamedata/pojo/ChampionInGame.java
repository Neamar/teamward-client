package fr.neamar.lolgamedata.pojo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class ChampionInGame extends Champion implements Serializable {
    public static final String UNKNOWN_ROLE = "?";

    public long winRate;

    public String role;

    ChampionInGame(JSONObject champion) throws JSONException {
        super(champion);

        role = champion.getString("role");
        JSONObject matchup = champion.getJSONObject("matchup");
        winRate = matchup.has("win_rate") ? matchup.getLong("win_rate") : -1;
    }
}
