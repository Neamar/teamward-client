package fr.neamar.lolgamedata.pojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Counter implements Serializable {
    public String role;
    public ChampionCounter champion;
    public final ArrayList<ChampionCounter> counters = new ArrayList<>();
    public int goodCountersThreshold = 0;

    public Counter(String role, JSONObject counter) throws JSONException {
        this.role = role;
        champion = new ChampionCounter(counter.getJSONObject("champion"), false);

        JSONArray jsonCounters = counter.getJSONArray("counters");
        for(int i = 0; i < jsonCounters.length(); i++) {
            ChampionCounter championCounter = new ChampionCounter(jsonCounters.getJSONObject(i), true);

            if(championCounter.winRate >= 50) {
                goodCountersThreshold++;
            }
            counters.add(championCounter);
        }
    }
}
