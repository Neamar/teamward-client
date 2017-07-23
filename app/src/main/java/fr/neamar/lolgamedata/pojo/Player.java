package fr.neamar.lolgamedata.pojo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Player implements Serializable {
    public Summoner summoner;
    public Champion champion;
    public Spell spellD;
    public Spell spellF;

    public Rank rank;

    public Boolean teamwardUser;
    public int averageTimeBetweenGames;

    public String region;

    public Player(JSONObject player, String region) throws JSONException {
        this.summoner = new Summoner(player.getJSONObject("summoner"));
        this.champion = new Champion(player.getJSONObject("champion"));
        this.spellD = new Spell(player.getJSONObject("spell_d"));
        this.spellF = new Spell(player.getJSONObject("spell_f"));

        this.teamwardUser = player.getBoolean("teamward_user");

        this.rank = new Rank(player.getJSONObject("current_season_rank"), player.getString("last_season_rank"));

        JSONObject recentGames = player.getJSONObject("recent_games");
        this.averageTimeBetweenGames = recentGames.getInt("average_time_between_games");

        this.region = region;
    }
}
