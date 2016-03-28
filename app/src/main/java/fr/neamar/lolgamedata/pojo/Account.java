package fr.neamar.lolgamedata.pojo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by neamar on 28/03/16.
 */
public class Account implements Serializable {
    public String summonerName;
    public String region;
    public String summonerImage;

    public Account(JSONObject account) throws JSONException {
        this.summonerName = account.getString("name");
        this.summonerImage = account.getString("image");
        this.region = account.getString("region");
    }
}
