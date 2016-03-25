package fr.neamar.lolgamedata.pojo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by neamar on 25/03/16.
 */
public class Player implements Serializable {
    public Summoner summoner;
    public Champion champion;
    public Spell spellD;
    public Spell spellF;

    public int knownChampions;

    public Player(JSONObject player) throws JSONException {
        this.summoner = new Summoner(player.getJSONObject("summoner"));
        this.champion = new Champion(player.getJSONObject("champion"));
        this.spellD = new Spell(player.getJSONObject("spell_d"));
        this.spellF = new Spell(player.getJSONObject("spell_f"));

        this.knownChampions = player.getInt("known_champions");
    }
}
