package fr.neamar.lolgamedata;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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

import fr.neamar.lolgamedata.pojo.Account;

public class AddAccountActivity extends Activity {
    public static final String TAG = "AddAccountActivity";
    public static final String NEW_ACCOUNT = "new_account";
    public static final int RESULT_ERROR = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_account);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        final TextView nameText = (TextView) findViewById(R.id.summonerText);
        final Spinner regionSpinner = (Spinner) findViewById(R.id.summonerRegion);
        regionSpinner.setFocusable(true);
        regionSpinner.setFocusableInTouchMode(true);

        setFinishOnTouchOutside(false);

        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameText.getText().toString();
                String region = regionSpinner.getSelectedItem().toString().replaceAll(" .+$", "");

                if(name.isEmpty()) {
                    nameText.setError("Please enter your summoner name!");
                    nameText.requestFocus();
                    return;
                }
                Log.i(TAG, "Adding new account: " + name + " (" + region + ")");

                saveAccount(name, region);
            }
        });
    }

    private Account saveAccount(final String name, final String region) {
        final Account newAccount = new Account(name, region, "");

        final ProgressDialog dialog = ProgressDialog.show(this, "",
                String.format(getString(R.string.loading_summoner_data), name), true);
        dialog.show();

        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(this);

        try {
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, ((LolApplication) getApplication()).getApiUrl() + "/summoner/data?summoner=" + URLEncoder.encode(name, "UTF-8") + "&region=" + region,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            dialog.dismiss();

                            newAccount.summonerImage = response.optString("profileIcon", "");
                            newAccount.summonerName = response.optString("name", newAccount.summonerName);

                            Intent intent = new Intent(NEW_ACCOUNT);
                            intent.putExtra("account", newAccount);
                            setResult(RESULT_OK, intent);

                            AccountManager accountManager = new AccountManager(AddAccountActivity.this);
                            accountManager.addAccount(newAccount);

                            ((LolApplication) getApplication()).getMixpanel().track("Account added", newAccount.toJsonObject());

                            ((LolApplication) getApplication()).identifyOnMixpanel();

                            queue.stop();
                            finish();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                    Log.e(TAG, error.toString());

                    try {
                        String responseBody = new String(error.networkResponse.data, "utf-8");
                        String errorMessage = responseBody.isEmpty() ? "Unable to load player data" : responseBody;
                        Log.i(TAG, errorMessage);

                        Intent intent = new Intent();
                        intent.putExtra("type", NEW_ACCOUNT);
                        intent.putExtra("error", errorMessage);
                        setResult(RESULT_ERROR, intent);

                        JSONObject j = newAccount.toJsonObject();
                        j.putOpt("error", errorMessage);
                        j.putOpt("name", name);
                        j.putOpt("region", region);
                        ((LolApplication) getApplication()).getMixpanel().track("Error adding account", j);

                    } catch (UnsupportedEncodingException | JSONException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                    }

                    queue.stop();

                    final TextView nameText = (TextView) findViewById(R.id.summonerText);
                    nameText.setError("Error adding account. Please double check your summoner name and region!");
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
}
