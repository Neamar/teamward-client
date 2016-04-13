package fr.neamar.lolgamedata;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import fr.neamar.lolgamedata.adapter.AccountAdapter;
import fr.neamar.lolgamedata.pojo.Account;

public class HomeActivity extends SnackBarActivity {
    public static final String TAG = "HomeActivity";

    public int GAME_DETAILS = 0;

    public SharedPreferences prefs;

    public RecyclerView recyclerView;

    public AccountManager accountManager;

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

        accountManager = new AccountManager(this);
        ArrayList<Account> accounts = accountManager.getAccounts();
        JSONObject j = new JSONObject();
        try {
            j.put("accounts_count", accounts.size());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ((LolApplication) getApplication()).getMixpanel().track("View account list", j);

        if (accounts.size() > 0) {
            ((LolApplication) getApplication()).getMixpanel().getPeople().set("$username", accounts.get(0).summonerName);
            ((LolApplication) getApplication()).getMixpanel().getPeople().set("$name", accounts.get(0).summonerName);
            ((LolApplication) getApplication()).getMixpanel().getPeople().set("region", accounts.get(0).region);
        }

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

    private void initView() {
        ArrayList<Account> accounts = accountManager.getAccounts();

        AccountAdapter adapter = new AccountAdapter(accounts, this, findViewById(android.R.id.empty), recyclerView);
        recyclerView.setAdapter(adapter);
    }


    private void addAccount() {
        final Dialog d = new Dialog(this);
        d.setTitle(R.string.add_account_title);
        d.setContentView(R.layout.dialog_add_account);

        d.findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
                String name = ((TextView) d.findViewById(R.id.summonerText)).getText().toString();
                String region = ((Spinner) d.findViewById(R.id.summonerRegion)).getSelectedItem().toString().replaceAll(" .+$", "");

                Log.i(TAG, "Adding new account: " + name + " (" + region + ")");

                saveAccount(name, region);
            }
        });
        d.show();
    }

    private Account saveAccount(String name, String region) {
        final Account newAccount = new Account(name, region, "");

        final ProgressDialog dialog = ProgressDialog.show(this, "",
                String.format(getString(R.string.loading_summoner_data), name), true);
        dialog.show();

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        try {
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, LolApplication.API_URL + "/summoner/data?summoner=" + URLEncoder.encode(name, "UTF-8") + "&region=" + region,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            newAccount.summonerImage = response.optString("profileIcon", "");

                            accountManager.addAccount(newAccount);

                            AccountAdapter adapter = (AccountAdapter) recyclerView.getAdapter();
                            adapter.updateAccounts(accountManager.getAccounts());

                            dialog.dismiss();

                            displaySnack(String.format("Added account %s", newAccount.summonerName));

                            JSONObject j = newAccount.toJsonObject();
                            ((LolApplication) getApplication()).getMixpanel().track("Account added", j);
                            ((LolApplication) getApplication()).getMixpanel().getPeople().set("accounts_length", accountManager.getAccounts().size());
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                    Log.e(TAG, error.toString());

                    try {
                        String responseBody = new String(error.networkResponse.data, "utf-8");
                        Log.i(TAG, responseBody);

                        displaySnack(responseBody.isEmpty() ? "Unable to load player data" : responseBody);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                    }
                }
            });

            jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(jsonRequest);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return newAccount;
    }

    public void openGameDetails(Account account) {
        Intent i = new Intent(this, GameActivity.class);
        i.putExtra("account", account);
        startActivityForResult(i, GAME_DETAILS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GAME_DETAILS && resultCode == GameActivity.NO_GAME_FOUND) {
            displaySnack(data.getStringExtra("error"));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
