package fr.neamar.lolgamedata.pojo;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.neamar.lolgamedata.R;

public class Team implements Serializable {
    public final List<List<String>> premades = new ArrayList<>();
    public final WinrateByGameLength winrateByGameLength;
    public int teamId;
    boolean isPlayerOwnTeam;
    public ArrayList<Player> players;

    public Team(JSONObject team, String region, boolean useRelativeTeamColor) throws JSONException {
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

        if(useRelativeTeamColor) {
            // Ensure our team is always blue team
            teamId = isPlayerOwnTeam ? 100 : 200;
        }

        if(team.has("premades")) {
            JSONArray premadesJson = team.getJSONArray("premades");
            for (int i = 0; i < premadesJson.length(); i++) {
                List<String> subPremade = new ArrayList<>();
                JSONArray subPremadeJson = premadesJson.getJSONArray(i);
                for (int j = 0; j < subPremadeJson.length(); j++) {
                    subPremade.add(subPremadeJson.getString(j));
                }
                premades.add(subPremade);
            }
        }

        // Happens on custom games where you're alone.
        winrateByGameLength = team.has("winrate_by_game_length") ? new WinrateByGameLength(team.getJSONObject("winrate_by_game_length")) : new WinrateByGameLength();
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
