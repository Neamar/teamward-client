package fr.neamar.lolgamedata.pojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by neamar on 25/03/16.
 */
public class Team implements Serializable {
    public int teamId;
    public ArrayList<Player> players;
    public List<List<Integer>> premades = new ArrayList<>();

    public Team(JSONObject team) throws JSONException {
        teamId = team.getInt("team_id");

        JSONArray playersJson = team.getJSONArray("players");
        players = new ArrayList<>();
        for (int i = 0; i < playersJson.length(); i++) {
            try {
                players.add(new Player(playersJson.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        JSONArray premadesJson = team.getJSONArray("premades");
        for(int i = 0; i < premadesJson.length(); i++) {
            List<Integer> subPremade = new ArrayList<>();
            JSONArray subPremadeJson = premadesJson.getJSONArray(i);
            for(int j = 0; j < subPremadeJson.length(); j++) {
                subPremade.add(subPremadeJson.getInt(j));
            }
            premades.add(subPremade);
        }
    }
}
