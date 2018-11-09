package fr.neamar.lolgamedata.pojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {
    public Summoner summoner;
    public ChampionInGame champion;
    public Spell spellD;
    public Spell spellF;

    public Rank mainRank;
    public ArrayList<Rank> allRanks = new ArrayList<>();
    public Boolean teamwardUser;
    public int averageTimeBetweenGames;

    public ArrayList<Champion> mainChampions = new ArrayList<>();

    public String region;

    public Player(JSONObject player, String region) throws JSONException {
        this.summoner = new Summoner(player.getJSONObject("summoner"));
        this.champion = new ChampionInGame(player.getJSONObject("champion"));
        this.spellD = new Spell(player.getJSONObject("spell_d"));
        this.spellF = new Spell(player.getJSONObject("spell_f"));

        this.teamwardUser = player.getBoolean("teamward_user");

        this.mainRank = new Rank(player.getJSONObject("current_season_rank"), player.getString("last_season_rank"));

        JSONArray allRanksJson = player.getJSONArray("all_ranks");
        for (int i = 0; i < allRanksJson.length(); i++) {
            allRanks.add(new Rank(allRanksJson.getJSONObject(i), player.getString("last_season_rank")));
        }

        JSONObject recentGames = player.getJSONObject("recent_games");
        this.averageTimeBetweenGames = recentGames.getInt("average_time_between_games");

        this.region = region;

        JSONArray mainChampionsJson = player.getJSONArray("main_champions");
        for (int i = 0; i < mainChampionsJson.length(); i++) {
            mainChampions.add(new Champion(mainChampionsJson.getJSONObject(i)));
        }
    }
}
