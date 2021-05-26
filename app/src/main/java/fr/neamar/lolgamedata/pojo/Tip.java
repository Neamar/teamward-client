package fr.neamar.lolgamedata.pojo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Tip implements Serializable {
    public final String title;
    public final String description;
    public final String image;
    public final String link;

    public Tip(JSONObject jsonObject) throws JSONException {
        title = jsonObject.getString("title");
        description = jsonObject.getString("description");
        image = jsonObject.getString("image");
        link = jsonObject.getString("link");
    }
}
