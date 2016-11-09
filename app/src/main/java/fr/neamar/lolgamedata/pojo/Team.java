package fr.neamar.lolgamedata.pojo;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.neamar.lolgamedata.R;

/**
 * Created by neamar on 25/03/16.
 */
public class Team implements Serializable {
    public int teamId;
    public boolean isPlayerOwnTeam;
    public ArrayList<Player> players;
    public final List<List<Integer>> premades = new ArrayList<>();

    public Team(JSONObject team, String region) throws JSONException {
        teamId = team.getInt("team_id");

        JSONArray playersJson = team.getJSONArray("players");
        players = new ArrayList<>();
        for (int i = 0; i < playersJson.length(); i++) {
            try {
                players.add(new Player(playersJson.getJSONObject(i), region));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        isPlayerOwnTeam = team.getBoolean("own_team");

        JSONArray premadesJson = team.getJSONArray("premades");
        for (int i = 0; i < premadesJson.length(); i++) {
            List<Integer> subPremade = new ArrayList<>();
            JSONArray subPremadeJson = premadesJson.getJSONArray(i);
            for (int j = 0; j < subPremadeJson.length(); j++) {
                subPremade.add(subPremadeJson.getInt(j));
            }
            premades.add(subPremade);
        }
    }

    public String getName(Context context) {
        if (teamId == 100) {
            return context.getString(R.string.blue_team);
        } else if (teamId == 200) {
            return context.getString(R.string.red_team);
        }

        return context.getString(R.string.unknown_team);
    }
}
