package fr.neamar.lolgamedata;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.neamar.lolgamedata.adapter.AccountAdapter;
import fr.neamar.lolgamedata.pojo.Account;
import fr.neamar.lolgamedata.service.RegistrationIntentService;

public class HomeActivity extends SnackBarActivity {
    public static final String TAG = "HomeActivity";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final int ADD_NEW_ACCOUNT = 1;

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

        registerForPushNotification(accounts);
    }

    private void registerForPushNotification(ArrayList<Account> accounts) {
        if (checkPlayServices() && accounts.size() > 0) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            Log.e(TAG, "Starting Service");
            startService(intent);
        }
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
        } else if (id == R.id.action_about) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.action_about)
                    .setMessage(getString(R.string.about_text))
                    .setPositiveButton(R.string.rammus_ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        ArrayList<Account> accounts = accountManager.getAccounts();

        AccountAdapter adapter = new AccountAdapter(accounts, this, findViewById(android.R.id.empty), recyclerView);
        recyclerView.setAdapter(adapter);
    }


    private void addAccount() {
        Intent i = new Intent(HomeActivity.this, AddAccountActivity.class);
        startActivityForResult(i, ADD_NEW_ACCOUNT);
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
        else if(requestCode == ADD_NEW_ACCOUNT && resultCode == RESULT_OK) {
            Account newAccount = (Account) data.getSerializableExtra("account");
            accountManager.addAccount(newAccount);

            AccountAdapter adapter = (AccountAdapter) recyclerView.getAdapter();
            adapter.updateAccounts(accountManager.getAccounts());

            displaySnack(String.format("Added account %s", newAccount.summonerName));

            ArrayList<Account> newAccounts = accountManager.getAccounts();
            JSONObject j = newAccount.toJsonObject();
            ((LolApplication) getApplication()).getMixpanel().track("Account added", j);
            ((LolApplication) getApplication()).getMixpanel().getPeople().set("accounts_length", newAccounts.size());

            registerForPushNotification(newAccounts);
        }
        else if(requestCode == ADD_NEW_ACCOUNT && resultCode == AddAccountActivity.RESULT_ERROR) {
            displaySnack(data.getStringExtra("error"));
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported for GCM.");
            }
            return false;
        }
        return true;
    }
}
