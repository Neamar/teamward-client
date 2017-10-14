package fr.neamar.lolgamedata.pojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Match {
    public int k;
    public int d;
    public int a;
    public int cs;
    public int duration;
    public String queue;
    public String role;

    public boolean victory;

    public String ward;
    public String matchUrl;

    public String championImage;
    public String opponentImage = null;

    public final ArrayList<String> items = new ArrayList<>();

    private Match(JSONObject match) throws JSONException {
        victory = match.getBoolean("victory");
        k = match.getInt("k");
        d = match.getInt("d");
        a = match.getInt("a");
        cs = match.getInt("cs");
        role = match.getString("role");
        queue = match.getString("queue");
        duration = match.getInt("duration");
        matchUrl = match.getString("match_url");
        championImage = match.getJSONObject("champion").getString("image");
        JSONObject opponent = match.getJSONObject("opponent");

        if(opponent.has("image")) {
            opponentImage = opponent.getString("image");
        }


        if (match.has("ward")) {
            ward = match.getJSONObject("ward").getString("image_url");
        }

        JSONArray jsonItems = match.getJSONArray("items");
        for (int i = 0; i < jsonItems.length(); i++) {
            JSONObject jsonItem = jsonItems.getJSONObject(i);
            items.add(jsonItem.getString("image_url"));
        }
    }

    public static ArrayList<Match> getMatches(JSONObject json) {
        JSONArray jsonMatches;
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
