package fr.neamar.lolgamedata;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
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

    public static final int UI_MODE_LOADING = 0;
    public static final int UI_MODE_IN_GAME = 1;
    public static final int UI_MODE_NOT_IN_GAME = 2;
    public static final int UI_MODE_NO_INTERNET = 3;

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

    public Account account;
    public Game game = null;
    public String summonerName;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private View mEmptyView;
    private FloatingActionButton mFab;
    private TabLayout mTabLayout;
    private SectionsPagerAdapter sectionsPagerAdapter;
    private Date lastLoaded = null;
    private DrawerLayout mDrawerLayout;

    @NonNull
    public static Integer getMapName(int mapId) {
        return MAP_NAMES.containsKey(mapId) ? MAP_NAMES.get(mapId) : R.string.unknown_map;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // First run: open accounts activity, finish this one
        AccountManager accountManager = new AccountManager(this);
        if (accountManager.getAccounts().isEmpty()) {
            Intent i = new Intent(this, AccountsActivity.class);
            startActivity(i);

            ((LolApplication) getApplication()).getMixpanel().track("First time app open");

            finish();
            return;
        }


        // Get account
        if (getIntent() != null && getIntent().hasExtra("account")) {
            account = (Account) getIntent().getSerializableExtra("account");
        } else {
            account = accountManager.getAccounts().get(0);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        assert toolbar != null;
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(R.string.title_activity_game);

        mViewPager = (ViewPager) findViewById(R.id.container);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mEmptyView = findViewById(android.R.id.empty);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this);

        // Set up the ViewPager with the sections adapter.
        assert mViewPager != null;
        assert mTabLayout != null;
        mViewPager.setAdapter(sectionsPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        assert mFab != null;
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUiMode(UI_MODE_LOADING);
                loadCurrentGame(account.summonerName, account.region);
            }
        });

        TextView notInGame = ((TextView) findViewById(R.id.summoner_not_in_game_text));
        assert notInGame != null;
        notInGame.setText(String.format(getString(R.string.s_is_not_in_game_right_now), account.summonerName));

        setUiMode(UI_MODE_LOADING);

        ((LolApplication) getApplication()).getMixpanel().getPeople().increment("games_viewed_count", 1);
        ((LolApplication) getApplication()).getMixpanel().getPeople().set("last_viewed_game", new Date());

        if (savedInstanceState == null || !savedInstanceState.containsKey("game")) {
            loadCurrentGame(account.summonerName, account.region);
        }
    }

    @Override
    protected void onResume() {
        if (lastLoaded != null) {
            long timeSinceLastView = new Date().getTime() - lastLoaded.getTime();
            long timeSinceGameStart = new Date().getTime() - game.startTime.getTime();
            Log.i(TAG, "Game started since " + Math.floor(timeSinceGameStart / 1000 / 60) + " minutes.");
            if (timeSinceLastView > 30000 && timeSinceGameStart > 60000 * 15) {
                displaySnack(getString(R.string.stale_data), getString(R.string.reload), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadCurrentGame(account.summonerName, account.region);
                        ((LolApplication) getApplication()).getMixpanel().track("Reload stale game");
                    }
                });
            }

            if (game != null) {
                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(game.getNotificationId());
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
        // automatically handle clicks on the AccountsActivity/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        } else if (id == R.id.action_about) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.action_about)
                    .setMessage(getString(R.string.about_text))
                    .setPositiveButton(R.string.rammus_ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();

            return true;
        } else if (id == R.id.action_counter) {
            Intent counterIntent = new Intent(GameActivity.this, CounterChampionsActivity.class);
            counterIntent.putExtra("account", account);
            startActivity(counterIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void setUiMode(int uiMode) {
        assert mTabLayout != null;
        assert mEmptyView != null;
        assert mFab != null;

        if (uiMode == UI_MODE_LOADING) {
            mTabLayout.setVisibility(View.GONE);
            mViewPager.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.GONE);
            mFab.setVisibility(View.GONE);
        } else if (uiMode == UI_MODE_NOT_IN_GAME) {
            mTabLayout.setVisibility(View.GONE);
            mViewPager.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
            mFab.setVisibility(View.VISIBLE);
        } else if (uiMode == UI_MODE_IN_GAME) {
            mTabLayout.setVisibility(View.VISIBLE);
            mViewPager.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
            mFab.setVisibility(View.GONE);
        } else if (uiMode == UI_MODE_NO_INTERNET) {
            mTabLayout.setVisibility(View.GONE);
            mViewPager.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.GONE);
            mFab.setVisibility(View.VISIBLE);
        }
    }

    public void loadCurrentGame(final String summonerName, final String region) {
        ((LolApplication) getApplication()).getMixpanel().timeEvent("Game viewed");

        final ProgressDialog dialog = ProgressDialog.show(this, "",
                String.format(getString(R.string.loading_game_data), summonerName), true);
        dialog.show();

        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(this);

        try {
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, ((LolApplication) getApplication()).getApiUrl() + "/game/data?summoner=" + URLEncoder.encode(summonerName, "UTF-8") + "&region=" + region, null,
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
                                j.putOpt("game_map_id", game.mapId);
                                j.putOpt("game_map_name", getString(getMapName(game.mapId)));
                                j.putOpt("game_mode", game.gameMode);
                                j.putOpt("game_type", game.gameType);
                                j.putOpt("game_id", game.gameId);
                                if (getIntent() != null && getIntent().hasExtra("source") && !getIntent().getStringExtra("source").isEmpty()) {
                                    j.putOpt("source", getIntent().getStringExtra("source"));
                                } else {
                                    j.putOpt("source", "unknown");
                                }

                                ((LolApplication) getApplication()).getMixpanel().track("Game viewed", j);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            dialog.dismiss();

                            lastLoaded = new Date();

                            queue.stop();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Log.e(TAG, error.toString());

                    queue.stop();

                    setUiMode(UI_MODE_NO_INTERNET);

                    if (error instanceof NoConnectionError) {
                        displaySnack(getString(R.string.no_internet_connection));
                        return;
                    }


                    try {
                        String responseBody = new String(error.networkResponse.data, "utf-8");
                        Log.i(TAG, responseBody);

                        if (!responseBody.contains("ummoner not in game")) {
                            displaySnack(responseBody);
                            JSONObject j = account.toJsonObject();
                            j.put("error", responseBody.replace("Error:", ""));
                            ((LolApplication) getApplication()).getMixpanel().track("Error viewing game", j);
                        } else {
                            setUiMode(UI_MODE_NOT_IN_GAME);
                        }
                    } catch (UnsupportedEncodingException | JSONException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        // Do nothing, no text content in the HTTP reply.
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
    }

    public void displayGame(String summonerName, Game game) {
        String titleTemplate = getString(R.string.game_data_title);

        @StringRes Integer stringRes = getMapName(game.mapId);

        setTitle(String.format(titleTemplate, summonerName, getString(stringRes)));
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        sectionsPagerAdapter.setGame(game);

        String defaultTabName = PreferenceManager.getDefaultSharedPreferences(this).getString("default_game_data_tab", "enemy");

        TabLayout.Tab selectedTab;

        if (defaultTabName.equals("tips")) {
            selectedTab = mTabLayout.getTabAt(2);
        } else {
            int myTeamIndex = game.teams.get(0) == game.getPlayerOwnTeam() ? 0 : 1;
            int enemyTeamIndex = game.teams.get(0) == game.getPlayerOwnTeam() ? 1 : 0;
            if (defaultTabName.equals("enemy")) {
                selectedTab = mTabLayout.getTabAt(enemyTeamIndex);
            } else {
                selectedTab = mTabLayout.getTabAt(myTeamIndex);
            }
        }

        if (selectedTab != null) {
            selectedTab.select();
        }

        setUiMode(UI_MODE_IN_GAME);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (game != null) {
            outState.putSerializable("summonerName", summonerName);
            outState.putSerializable("game", game);
            outState.putSerializable("lastLoaded", lastLoaded);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey("game")) {
            summonerName = savedInstanceState.getString("summonerName");
            game = (Game) savedInstanceState.getSerializable("game");
            lastLoaded = (Date) savedInstanceState.getSerializable("lastLoaded");
            displayGame(summonerName, game);
        }
    }
}
