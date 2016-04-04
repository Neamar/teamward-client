package fr.neamar.lolgamedata.pojo;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by neamar on 25/03/16.
 */
public class Rank implements Serializable {
    public String tier;
    public String division;

    public Rank(JSONObject rank) throws JSONException {
        Log.e("WTF", rank.toString());
        tier = rank.getString("tier");
        division = rank.getString("division");

    }
}
