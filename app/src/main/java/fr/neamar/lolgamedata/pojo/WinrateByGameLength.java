package fr.neamar.lolgamedata.pojo;

import android.util.SparseArray;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.Iterator;

public class WinrateByGameLength extends SparseArray<Double> implements Serializable {
    public WinrateByGameLength(JSONObject winrateByGameLength) {
        Iterator<String> keys = winrateByGameLength.keys();

        while(keys.hasNext()) {
            String key = keys.next();
            this.append(Integer.parseInt(key), winrateByGameLength.optDouble(key, 50));
        }
    }
}
