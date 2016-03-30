package fr.neamar.lolgamedata;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import fr.neamar.lolgamedata.adapter.AccountAdapter;
import fr.neamar.lolgamedata.pojo.Account;

public class HomeActivity extends AppCompatActivity {
    public static final String TAG = "HomeActivity";
    public static final String ACCOUNTS_KEY = "accounts";
    public static final String DEFAULT_VALUE = "[]";

    public SharedPreferences prefs;

    public RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        prefs = getPreferences(MODE_PRIVATE);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAccount();
            }
        });

        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the HomeActivity/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void initView() {
        ArrayList<Account> accounts = getAccounts();

        AccountAdapter adapter = new AccountAdapter(accounts);
        recyclerView.setAdapter(adapter);
    }

    public ArrayList<Account> getAccounts() {
        String accountsString = prefs.getString(ACCOUNTS_KEY, DEFAULT_VALUE);

        try {
            JSONArray accountsJson = new JSONArray(accountsString);
            ArrayList<Account> accounts = new ArrayList<>();

            for (int i = 0; i < accountsJson.length(); i += 1) {
                Account account = new Account(accountsJson.getJSONObject(i));
                accounts.add(account);
            }

            return accounts;
        } catch (JSONException e) {
            Toast.makeText(HomeActivity.this, "Accounts got corrupted, resetting app.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            prefs.edit().clear().apply();
            return new ArrayList<>();
        }
    }

    public void writeAccounts(ArrayList<Account> accounts) {
        JSONArray accountsJson = new JSONArray();

        for(int i = 0; i < accounts.size(); i++) {
            accountsJson.put(accounts.get(i).toJsonObject());
        }

        prefs.edit().putString(ACCOUNTS_KEY, accountsJson.toString()).apply();
    }

    public void addAccount() {
        final Dialog d = new Dialog(this);
        d.setTitle(R.string.add_account_title);
        d.setContentView(R.layout.dialog_add_account);

        d.findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
                ArrayList<Account> accounts = getAccounts();
                String name = ((TextView) d.findViewById(R.id.summonerText)).getText().toString();
                String region = "euw";
                Account newAccount = new Account(name, region, "http://ddragon.leagueoflegends.com/cdn/6.6.1/img/profileicon/26.png");
                Log.i(TAG, "Added new account:" + name);
                accounts.add(newAccount);

                AccountAdapter adapter = (AccountAdapter) recyclerView.getAdapter();
                adapter.updateAccounts(accounts);

                writeAccounts(accounts);
            }
        });
        d.show();
    }
}
