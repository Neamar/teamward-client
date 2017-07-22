package fr.neamar.lolgamedata.pojo;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class Game implements Serializable {
    public final long gameId;
    public final int mapId;
    public final Date startTime;
    public final String gameMode;
    public final String gameType;
    public final Account associatedAccount;
    public final ArrayList<Team> teams;

    public Game(JSONObject game, String region, Account associatedAccount, boolean useRelativeTeamColor) throws JSONException {
        gameId = game.getLong("game_id");
        mapId = game.getInt("map_id");
        gameMode = game.getString("game_mode");
        gameType = game.getString("game_type");
        startTime = new Date(game.optLong("game_start_time", new Date().getTime()));
        this.associatedAccount = associatedAccount;

        JSONArray teamsJson = game.getJSONArray("teams");
        teams = new ArrayList<>();
        for (int i = 0; i < teamsJson.length(); i++) {
            try {
                teams.add(new Team(teamsJson.getJSONObject(i), region, useRelativeTeamColor));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Collections.sort(teams, new Comparator<Team>() {
            @Override
            public int compare(Team o1, Team o2) {
                return o1.teamId - o2.teamId;
            }
        });
    }

    @NonNull
    public Team getPlayerOwnTeam() {
        for (Team team : teams) {
            if (team.isPlayerOwnTeam) {
                return team;
            }
        }

        throw new RuntimeException("Current player is part of no team?!");
    }

    @Nullable
    public Team getTeamForPlayer(Player player) {
        for (Team team : teams) {
            for (Player tplayer : team.players) {
                if (tplayer.summoner.name.equalsIgnoreCase(player.summoner.name)) {
                    return team;
                }
            }
        }

        return null;
    }

    @Nullable
    public Player getPlayerByAccount(Account account) {
        for (Team team : teams) {
            for(Player player: team.players) {
                if (player.summoner.name.equalsIgnoreCase(account.summonerName)) {
                    return player;
                }
            }
        }

        // This is very uncommon, but can happen after migrating to a new region / new server: the data won't be fully sinced yet, and the player won't be in the list
        return null;
    }

    public int getNotificationId() {
        return Long.toString(gameId).hashCode();
    }

    public void useRelativeTeamId() {

    }
}
