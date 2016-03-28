package fr.neamar.lolgamedata;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import fr.neamar.lolgamedata.adapter.AccountAdapter;
import fr.neamar.lolgamedata.pojo.Account;

public class HomeActivity extends AppCompatActivity {
    public static final String ACCOUNTS_KEY = "accounts";

    public SharedPreferences prefs;

    public  RecyclerView recyclerView;

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
        String accountsString = prefs.getString(ACCOUNTS_KEY, "[{\"summonerName\":\"neamar\",\"region\":\"euw\",\"summonerImage\":\"http://ddragon.leagueoflegends.com/cdn/6.6.1/img/profileicon/588.png\"}]");
        JSONArray accountsJson;

        try {
            accountsJson = new JSONArray(accountsString);

            ArrayList<Account> accounts = new ArrayList<>();

            for(int i = 0; i < accountsJson.length(); i += 1) {
                Account account = new Account(accountsJson.getJSONObject(i));
                accounts.add(account);
            }

            AccountAdapter adapter = new AccountAdapter(accounts);
            recyclerView.setAdapter(adapter);
        } catch (JSONException e) {
            Toast.makeText(HomeActivity.this, "Accounts got corrupted, resetting app.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            prefs.edit().clear().apply();
        }


    }
}
