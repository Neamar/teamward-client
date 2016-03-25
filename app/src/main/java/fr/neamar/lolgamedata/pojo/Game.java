package fr.neamar.lolgamedata.pojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by neamar on 25/03/16.
 */
public class Game implements Serializable {
    public int mapId;
    public ArrayList<Player> players;

    public Game(JSONObject game) throws JSONException {
        // mapId = game.getInt("map_id");
        JSONArray playersJson = game.getJSONArray("participants");
        players = new ArrayList<>();
        for (int i = 0; i < playersJson.length(); i++) {
            try {
                players.add(new Player(playersJson.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }
}
