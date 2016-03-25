package fr.neamar.lolgamedata.pojo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by neamar on 25/03/16.
 */
public class Spell implements Serializable {
    public String name;
    public String imageUrl;

    public Spell(JSONObject spell) throws JSONException {
        name = spell.getString("name");
        imageUrl = spell.getString("image");
    }
}
