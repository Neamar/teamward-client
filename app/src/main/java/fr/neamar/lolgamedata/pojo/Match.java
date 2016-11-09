package fr.neamar.lolgamedata.pojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by neamar on 08/11/16.
 */

public class Match {
    public int k;
    public int d;
    public int a;
    public int cs;

    public boolean victory;

    public String ward;

    public ArrayList<String> items = new ArrayList<>();

    public Match(JSONObject match) throws JSONException {
        victory = match.getBoolean("victory");
        k = match.getInt("k");
        d = match.getInt("d");
        a = match.getInt("a");
        cs = match.getInt("cs");


        if(match.has("ward")) {
            ward = match.getJSONObject("ward").getString("image_url");
        }

        JSONArray jsonItems = match.getJSONArray("items");
        for(int i = 0; i < jsonItems.length(); i++) {
            JSONObject jsonItem = jsonItems.getJSONObject(i);
            items.add(jsonItem.getString("image_url"));
        }
    }

    public static ArrayList<Match> getMatches(JSONObject json) {
        JSONArray jsonMatches = null;
        ArrayList<Match> matches = new ArrayList<>();

        try {
            jsonMatches = json.getJSONArray("matches");


            for (int i = 0; i < jsonMatches.length(); i++) {
                Match match = new Match(jsonMatches.getJSONObject(i));
                matches.add(match);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return matches;
    }
}
