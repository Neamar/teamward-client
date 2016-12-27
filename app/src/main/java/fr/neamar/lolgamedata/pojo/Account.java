package fr.neamar.lolgamedata.pojo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Account implements Serializable {
    public String summonerName;
    public String region;
    public String summonerImage;

    public Account(JSONObject account) throws JSONException {
        this.summonerName = account.getString("name");
        this.summonerImage = account.getString("image");
        this.region = account.getString("region").toUpperCase();
    }

    public Account(String summonerName, String region, String summonerImage) {
        this.summonerName = summonerName;
        this.region = region.toUpperCase();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        return summonerName.equalsIgnoreCase(account.summonerName) && region.equalsIgnoreCase(account.region);

    }

    @Override
    public int hashCode() {
        int result = summonerName.hashCode();
        result = 31 * result + region.hashCode();
        return result;
    }
}
