package fr.neamar.lolgamedata.pojo;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Game implements Serializable {
    public long gameId;
    public int mapId;
    public Date startTime;
    public String gameMode;
    public String gameType;
    public ArrayList<Team> teams;

    public Game(JSONObject game, String region) throws JSONException {
        gameId = game.getLong("game_id");
        mapId = game.getInt("map_id");
        gameMode = game.getString("game_mode");
        gameType = game.getString("game_type");
        startTime = new Date(game.optLong("game_start_time", new Date().getTime()));

        JSONArray teamsJson = game.getJSONArray("teams");
        teams = new ArrayList<>();
        for (int i = 0; i < teamsJson.length(); i++) {
            try {
                teams.add(new Team(teamsJson.getJSONObject(i), region));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @NonNull
    public Team getPlayerOwnTeam() {
        for (Team team : teams) {
            if (team.isPlayerOwnTeam) {
                return team;
            }
        }

        throw new RuntimeException("Required player is part of no team?!");
    }

    @NonNull
    public Player getPlayerByAccount(Account account) {
        for (Team team : teams) {
            for(Player player: team.players)
                if(player.summoner.name.equals(account.summonerName)) {
                    return player;
            }
        }

        throw new RuntimeException("Required player is part of no team?!");
    }

    public int getNotificationId() {
        return Long.toString(gameId).hashCode();
    }
}
