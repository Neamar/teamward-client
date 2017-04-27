package fr.neamar.lolgamedata;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class ChampionActivity extends SnackBarActivity {
    private static final String TAG = "PlayerDetailActivity";
    private String championName;
    private int championId;
    private String ggUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champion);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        championName = getIntent().getStringExtra("championName");
        championId = getIntent().getIntExtra("championId", 0);

        // HERO
        setTitle(championName);

        // CONTENT
        findViewById(R.id.championAbilityDetailsWrapper).setVisibility(View.GONE);
        findViewById(R.id.championTipsWrapper).setVisibility(View.GONE);

        Tracker.trackChampionViewed(this, championName, championId, getIntent().getStringExtra("from"));
        downloadChampionDetails(championId);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.action_gg && !ggUrl.isEmpty()) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ggUrl));
            startActivity(browserIntent);

            Tracker.trackClickOnGG(this, championName, championId, "champion_details");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_champion, menu);

        return true;
    }

    private void downloadChampionDetails(int championId) {
        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, ((LolApplication) getApplication()).getApiUrl() + "/champion/" + championId + "?locale=" + Locale.getDefault().toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject championData = response;
                        displayChampionDetails(championData);

                        Log.i(TAG, "Displaying champion details for " + championName);
                        queue.stop();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
                findViewById(R.id.progressBar).setVisibility(View.GONE);

                queue.stop();
            }
        });

        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonRequest);
    }

    private void displayChampionDetails(JSONObject championData) {
        findViewById(R.id.championAbilityDetailsWrapper).setVisibility(View.VISIBLE);
        findViewById(R.id.championTipsWrapper).setVisibility(View.VISIBLE);
        findViewById(R.id.progressBar).setVisibility(View.GONE);

        final ImageView splashArtImage = (ImageView) findViewById(R.id.splashArt);

        try {
            ggUrl = championData.getString("gg_url");

            ImageLoader.getInstance().displayImage(championData.getString("splash_url"), splashArtImage);

            displayChampionAbility(getString(R.string.ability_passive), championData.getJSONObject("passive"), findViewById(R.id.abilityP));
            displayChampionAbility(getString(R.string.ability_q), championData.getJSONArray("spells").getJSONObject(0), findViewById(R.id.abilityQ));
            displayChampionAbility(getString(R.string.ability_w), championData.getJSONArray("spells").getJSONObject(1), findViewById(R.id.abilityW));
            displayChampionAbility(getString(R.string.ability_e), championData.getJSONArray("spells").getJSONObject(2), findViewById(R.id.abilityE));
            displayChampionAbility(getString(R.string.ability_r), championData.getJSONArray("spells").getJSONObject(3), findViewById(R.id.abilityR));

            String tips = "";
            for (int i = 0; i < championData.getJSONArray("tips").length(); i++) {
                tips += "\t•&nbsp;" + championData.getJSONArray("tips").getString(i) + "<br>\n";
            }
            ((TextView) findViewById(R.id.championTips)).setText(Html.fromHtml(tips));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void displayChampionAbility(String name, JSONObject ability, View holder) throws JSONException {
        ((TextView) holder.findViewById(R.id.abilityName)).setText(String.format(getString(R.string.ability_name), name, ability.getString("name")));
        ((TextView) holder.findViewById(R.id.abilityDescription)).setText(Html.fromHtml(ability.getString("description")));

        if (ability.has("cooldowns") && !ability.getString("cooldowns").equals("0")) {
            ((TextView) holder.findViewById(R.id.abilityCooldown)).setText(String.format(getString(R.string.ability_cooldowns), ability.getString("cooldowns")));
        } else {
            holder.findViewById(R.id.abilityCooldown).setVisibility(View.GONE);
        }
        ImageLoader.getInstance().displayImage(ability.getString("image"), (ImageView) holder.findViewById(R.id.abilityImage));
    }
}
