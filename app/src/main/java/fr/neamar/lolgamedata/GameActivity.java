package fr.neamar.lolgamedata;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import fr.neamar.lolgamedata.adapter.SectionsPagerAdapter;
import fr.neamar.lolgamedata.pojo.Account;
import fr.neamar.lolgamedata.pojo.Game;

public class GameActivity extends SnackBarActivity {
    public static final String TAG = "GameActivity";

    public static final int NO_GAME_FOUND = 44;

    public Account account;
    public Game game = null;
    public String summonerName;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private TabLayout mTabLayout;

    private SectionsPagerAdapter sectionsPagerAdapter;

    private Date lastLoaded = null;

    @StringRes
    private static final Map<Integer, Integer> MAP_NAMES;

    static {
        Map<Integer, Integer> mapNames = new HashMap<>();
        mapNames.put(1, R.string.summoners_rift);
        mapNames.put(2, R.string.summoners_rift);
        mapNames.put(3, R.string.proving_grounds);
        mapNames.put(4, R.string.twisted_treeline);
        mapNames.put(8, R.string.crystal_scar);
        mapNames.put(10, R.string.twisted_treeline);
        mapNames.put(11, R.string.summoners_rift);
        mapNames.put(12, R.string.howling_abyss);
        mapNames.put(14, R.string.butchers_bridge);

        MAP_NAMES = Collections.unmodifiableMap(mapNames);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        assert toolbar != null;
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);

        toolbar.setTitle(R.string.title_activity_game);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(GameActivity.this);
            }
        });


        mViewPager = (ViewPager) findViewById(R.id.container);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);

        assert mTabLayout != null;
        mTabLayout.setVisibility(View.GONE);

        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this);

        // Set up the ViewPager with the sections adapter.
        assert mViewPager != null;
        assert mTabLayout != null;
        mViewPager.setAdapter(sectionsPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        account = (Account) getIntent().getSerializableExtra("account");

        ((LolApplication) getApplication()).getMixpanel().getPeople().increment("games_viewed_count", 1);
        ((LolApplication) getApplication()).getMixpanel().timeEvent("Game viewed");


        if (savedInstanceState == null || !savedInstanceState.containsKey("game")) {
            loadCurrentGame(account.summonerName, account.region);
        }
    }

    @Override
    protected void onResume() {
        if (lastLoaded != null) {
            long timeSinceLastView = new Date().getTime() - lastLoaded.getTime();
            long timeSinceGameStart = new Date().getTime() - game.startTime.getTime();
            Log.i(TAG, "Game started since " + Math.floor(timeSinceGameStart / 1000 / 60));
            if (timeSinceLastView > 30000 && timeSinceGameStart > 60000 * 15) {
                displaySnack("Stale data?", "Reload", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadCurrentGame(account.summonerName, account.region);
                    }
                });
            }
        }

        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
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

    public void loadCurrentGame(final String summonerName, final String region) {
        final ProgressDialog dialog = ProgressDialog.show(this, "",
                String.format(getString(R.string.loading_game_data), summonerName), true);
        dialog.show();

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonRequest = null;
        try {
            jsonRequest = new JsonObjectRequest(Request.Method.GET, LolApplication.API_URL + "/game/data?summoner=" + URLEncoder.encode(summonerName, "UTF-8") + "&region=" + region, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                game = new Game(response);
                                GameActivity.this.summonerName = summonerName;
                                displayGame(summonerName, game);

                                Log.i(TAG, "Displaying game #" + game.gameId);

                                // Timing automatically added (see timeEvent() call)
                                JSONObject j = account.toJsonObject();
                                ((LolApplication) getApplication()).getMixpanel().track("Game viewed", j);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            dialog.dismiss();

                            lastLoaded = new Date();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                    Log.e(TAG, error.toString());

                    try {
                        String responseBody = new String(error.networkResponse.data, "utf-8");
                        Log.i(TAG, responseBody);

                        Intent intent = new Intent();
                        intent.putExtra("error", responseBody);
                        setResult(NO_GAME_FOUND, intent);

                        JSONObject j = account.toJsonObject();
                        j.put("error", responseBody);
                        ((LolApplication) getApplication()).getMixpanel().track("Error viewing game", j);

                    } catch (UnsupportedEncodingException | JSONException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        // Do nothing, no text content in the HTTP reply.
                    }

                    finish();
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
    }

    public void displayGame(String summonerName, Game game) {
        String titleTemplate = getString(R.string.game_data_title);

        @StringRes Integer stringRes = MAP_NAMES.containsKey(game.mapId) ? MAP_NAMES.get(game.mapId) : R.string.unknown_map;

        setTitle(String.format(titleTemplate, summonerName, getString(stringRes)));
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        sectionsPagerAdapter.setTeams(game.teams);

        // Set up the ViewPager with the sections adapter.
        assert mTabLayout != null;
        mTabLayout.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (game != null) {
            outState.putSerializable("summonerName", summonerName);
            outState.putSerializable("game", game);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey("game")) {
            summonerName = savedInstanceState.getString("summonerName");
            game = (Game) savedInstanceState.getSerializable("game");
            displayGame(summonerName, game);
        }
    }
}
