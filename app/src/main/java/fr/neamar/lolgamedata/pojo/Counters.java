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
    public ArrayList<Counter> counters = new ArrayList<Counter>();

    public Counters(JSONObject jsonCounterRequest) throws JSONException {
        JSONArray jsonCounters = jsonCounterRequest.getJSONArray("counters");

        for (int i = 0; i < jsonCounters.length(); i++) {
            counters.add(new Counter(jsonCounters.getJSONObject(i)));
        }
    }

    public Counters() {

    }
}
