package fr.neamar.lolgamedata.pojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by neamar on 31/08/16.
 */
public class Counters implements Serializable {
    public final ArrayList<Counter> counters = new ArrayList<>();

    public Counters(String role, JSONObject jsonCounterRequest) throws JSONException {
        JSONArray jsonCounters = jsonCounterRequest.getJSONArray("counters");

        for (int i = 0; i < jsonCounters.length(); i++) {
            counters.add(new Counter(role, jsonCounters.getJSONObject(i)));
        }
    }

    public Counters() {

    }
}
