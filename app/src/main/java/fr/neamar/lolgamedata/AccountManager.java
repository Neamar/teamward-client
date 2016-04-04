package fr.neamar.lolgamedata;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import fr.neamar.lolgamedata.pojo.Account;

/**
 * Created by neamar on 03/04/16.
 */
public class AccountManager {
    public static final String ACCOUNTS_KEY = "accounts";
    public static final String DEFAULT_VALUE = "[]";

    private Context context;

    public AccountManager(Context context) {
        this.context = context;
    }

    private SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences("accounts", Context.MODE_PRIVATE);
    }

    private void writeAccounts(ArrayList<Account> accounts) {
        JSONArray accountsJson = new JSONArray();

        for (int i = 0; i < accounts.size(); i++) {
            accountsJson.put(accounts.get(i).toJsonObject());
        }

        getSharedPreferences().edit().putString(ACCOUNTS_KEY, accountsJson.toString()).apply();
    }

    public ArrayList<Account> getAccounts() {
        String accountsString = getSharedPreferences().getString(ACCOUNTS_KEY, DEFAULT_VALUE);

        try {
            JSONArray accountsJson = new JSONArray(accountsString);
            ArrayList<Account> accounts = new ArrayList<>();

            for (int i = 0; i < accountsJson.length(); i += 1) {
                Account account = new Account(accountsJson.getJSONObject(i));
                accounts.add(account);
            }

            return accounts;
        } catch (JSONException e) {
            Toast.makeText(context, "Accounts got corrupted, resetting app.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            getSharedPreferences().edit().clear().apply();
            return new ArrayList<>();
        }
    }

    public void addAccount(Account account) {
        ArrayList<Account> accounts = getAccounts();
        accounts.add(account);
        writeAccounts(accounts);
    }

    public void removeAccount(Account account) {
        ArrayList<Account> accounts = getAccounts();
        accounts.remove(account);
        writeAccounts(accounts);
    }
}
