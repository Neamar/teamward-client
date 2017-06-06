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

    public Counters(Account account, String role, JSONObject jsonCounterRequest) throws JSONException {
        JSONArray jsonCounters = jsonCounterRequest.getJSONArray("counters");

        Set<ChampionCounter> playedChampions = new HashSet<>();
        Set<ChampionCounter> availableChampions = new HashSet<>();

        for (int i = 0; i < jsonCounters.length(); i++) {
            Counter c = new Counter(account, role, jsonCounters.getJSONObject(i));

            counters.add(c);

            playedChampions.addAll(c.counters);
            availableChampions.add(c.champion);
        }

        for (Counter c : counters) {
            // First, list all champions
            c.noData.addAll(new HashSet<>(playedChampions));
            // And remove champions not in first set
            c.noData.removeAll(c.counters);
            // Also remove champions that aren't common in this position
            c.noData.retainAll(availableChampions);

            // And remove self
            c.noData.remove(c.champion);
        }
    }

    public Counters() {

    }
}
