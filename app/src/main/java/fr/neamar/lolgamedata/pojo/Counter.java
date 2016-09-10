package fr.neamar.lolgamedata.pojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Counter implements Serializable {
    public ChampionCounter champion;
    public ArrayList<ChampionCounter> counters = new ArrayList<>();

    public Counter(JSONObject counter) throws JSONException {
        champion = new ChampionCounter(counter.getJSONObject("champion"), false);

        JSONArray jsonCounters = counter.getJSONArray("counters");
        for(int i = 0; i < jsonCounters.length(); i++) {
            counters.add(new ChampionCounter(jsonCounters.getJSONObject(i), true));
        }
    }
}
