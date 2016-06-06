package fr.neamar.lolgamedata;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.neamar.lolgamedata.pojo.Account;

public class AccountsActivity extends SnackBarActivity {
    public static final String TAG = "AccountsActivity";
    private static final int ADD_NEW_ACCOUNT = 1;

    public final int GAME_DETAILS = 0;

    public AccountManager accountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);

        accountManager = new AccountManager(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAccount();
            }
        });

        ArrayList<Account> accounts = accountManager.getAccounts();
        JSONObject j = new JSONObject();
        try {
            j.put("accounts_count", accounts.size());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (accounts.size() > 0) {
            ((LolApplication) getApplication()).getMixpanel().getPeople().set("$username", accounts.get(0).summonerName);
            ((LolApplication) getApplication()).getMixpanel().getPeople().set("$name", accounts.get(0).summonerName);
            ((LolApplication) getApplication()).getMixpanel().getPeople().set("region", accounts.get(0).region);
        }

        if (!accounts.isEmpty()) {
            Account mainAccount = accounts.get(0);
            Intent i = new Intent(this, GameActivity.class);
            i.putExtra("account", mainAccount);
            startActivityForResult(i, GAME_DETAILS);
            finish();
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
        // automatically handle clicks on the AccountsActivity/Up button, so long
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

    private void addAccount() {
        Intent i = new Intent(AccountsActivity.this, AddAccountActivity.class);
        startActivityForResult(i, ADD_NEW_ACCOUNT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_NEW_ACCOUNT && resultCode == RESULT_OK) {
            Account newAccount = (Account) data.getSerializableExtra("account");

            displaySnack(String.format("Added account %s", newAccount.summonerName));

            ArrayList<Account> newAccounts = accountManager.getAccounts();
            JSONObject j = newAccount.toJsonObject();
            ((LolApplication) getApplication()).getMixpanel().track("Account added", j);
            ((LolApplication) getApplication()).getMixpanel().getPeople().set("accounts_length", newAccounts.size());

            Intent i = new Intent(this, GameActivity.class);
            i.putExtra("account", newAccount);
            startActivityForResult(i, GAME_DETAILS);
            finish();
        } else if (requestCode == ADD_NEW_ACCOUNT && resultCode == AddAccountActivity.RESULT_ERROR) {
            displaySnack(data.getStringExtra("error"));
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
