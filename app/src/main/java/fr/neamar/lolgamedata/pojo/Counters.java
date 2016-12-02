package fr.neamar.lolgamedata.pojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Counters implements Serializable {
    public final ArrayList<Counter> counters = new ArrayList<>();

    public Counters(String role, JSONObject jsonCounterRequest) throws JSONException {
        JSONArray jsonCounters = jsonCounterRequest.getJSONArray("counters");

        Set<ChampionCounter> availableChampions = new HashSet<>();
        for (int i = 0; i < jsonCounters.length(); i++) {
            Counter c = new Counter(role, jsonCounters.getJSONObject(i));

            counters.add(c);

            availableChampions.addAll(c.counters);
        }

        for(Counter c: counters) {
            // First, list all champions
            c.noData.addAll(new HashSet<>(availableChampions));
            // And remove champions not in first set
            c.noData.removeAll(c.counters);
        }
    }

    public Counters() {

    }
}
