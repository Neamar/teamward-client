package fr.neamar.lolgamedata.pojo;

import android.os.Bundle;

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

    public Account(String summonerName, String region, String summonerImage) {
        this.summonerName = summonerName;
        this.region = region;
        this.summonerImage = summonerImage;
    }

    public JSONObject toJsonObject() {
        JSONObject o = new JSONObject();
        try {
            o.putOpt("name", summonerName);
            o.putOpt("region", region);
            o.putOpt("image", summonerImage);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return o;
    }

    public Bundle toBundle() {
        Bundle b = new Bundle();
        b.putString("name", summonerImage);
        b.putString("region", region);
        b.putString("image", summonerImage);

        return b;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (!summonerName.equals(account.summonerName)) return false;
        return region.equals(account.region);

    }

    @Override
    public int hashCode() {
        int result = summonerName.hashCode();
        result = 31 * result + region.hashCode();
        return result;
    }
}
