package fr.neamar.lolgamedata.pojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by neamar on 25/03/16.
 */
public class Game implements Serializable {
    public long gameId;
    public int mapId;
    public String gameMode;
    public String gameType;
    public Date startTime;

    public ArrayList<Team> teams;

    public Game(JSONObject game) throws JSONException {
        gameId = game.getLong("game_id");
        mapId = game.getInt("map_id");
        gameMode = game.getString("game_mode");
        gameType = game.getString("game_type");

        startTime = new Date(game.optLong("game_start_time", new Date().getTime()));

        JSONArray teamsJson = game.getJSONArray("teams");
        teams = new ArrayList<>();
        for (int i = 0; i < teamsJson.length(); i++) {
            try {
                teams.add(new Team(teamsJson.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
