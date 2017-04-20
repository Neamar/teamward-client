package fr.neamar.lolgamedata;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import fr.neamar.lolgamedata.adapter.MatchAdapter;
import fr.neamar.lolgamedata.pojo.Champion;
import fr.neamar.lolgamedata.pojo.Game;
import fr.neamar.lolgamedata.pojo.Match;
import fr.neamar.lolgamedata.pojo.Player;
import fr.neamar.lolgamedata.pojo.Team;

import static fr.neamar.lolgamedata.holder.PlayerHolder.CHAMPION_MASTERIES_RESOURCES;
import static fr.neamar.lolgamedata.holder.PlayerHolder.RANKING_TIER_RESOURCES;

public class ChampionDetailActivity extends SnackBarActivity {
    private static final String TAG = "ChampionDetailActivity";
    private Player player;
    private Game game;

    private static final Map<String, Integer> QUEUE_NAMES;

    static {
        Map<String, Integer> queueNames = new HashMap<>();
        queueNames.put("NORMAL", R.string.normal);
        queueNames.put("CUSTOM_GAME", R.string.custom);
        queueNames.put("ARAM_UNRANKED_5x5", R.string.aram);
        queueNames.put("NORMAL_3x3", R.string.normal_3);
        queueNames.put("RANKED_SOLO_5x5", R.string.ranked_solo_5);
        queueNames.put("TEAM_BUILDER_RANKED_SOLO", R.string.ranked_solo_5);
        queueNames.put("RANKED_FLEX_SR", R.string.ranked_flex_5);
        queueNames.put("RANKED_FLEX_TT", R.string.ranked_flex_3);
        queueNames.put("TEAM_BUILDER_DRAFT_RANKED_5x5", R.string.teambuilder_ranked);

        QUEUE_NAMES = Collections.unmodifiableMap(queueNames);
    }

    @NonNull
    public static Integer getQueueName(String queue) {
        return QUEUE_NAMES.containsKey(queue) ? QUEUE_NAMES.get(queue) : R.string.unknown_queue;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champion_detail);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        game = (Game) getIntent().getSerializableExtra("game");
        player = (Player) getIntent().getSerializableExtra("player");

        // HERO
        setTitle(player.summoner.name);
        final ImageView splashArtImage = (ImageView) findViewById(R.id.splashArt);
        ImageLoader.getInstance().displayImage(player.champion.splashUrl, splashArtImage);

        // CHAMPION MASTERY
        View masteryHolder = findViewById(R.id.masteryHolder);
        ImageView championMasteryImage = (ImageView) findViewById(R.id.championMasteryImage);
        TextView championMasteryText = (TextView) findViewById(R.id.championMasteryText);
        TextView championPointsText = (TextView) findViewById(R.id.championPointsText);

        @DrawableRes
        int championMasteryResource = CHAMPION_MASTERIES_RESOURCES[player.champion.mastery];
        if (championMasteryResource == 0) {
            masteryHolder.setVisibility(View.GONE);
        } else {
            championMasteryImage.setImageResource(CHAMPION_MASTERIES_RESOURCES[player.champion.mastery]);
            championMasteryText.setText(String.format(getString(R.string.champion_mastery_lvl), player.champion.mastery));
            if (player.champion.mastery >= 5) {
                championPointsText.setText(String.format(getString(R.string.champion_points), NumberFormat.getInstance().format(player.champion.points)));
            } else {
                championPointsText.setVisibility(View.GONE);
            }
            masteryHolder.setVisibility(View.VISIBLE);
        }

        // RANKED INFORMATION
        ImageView rankingTierImage = (ImageView) findViewById(R.id.rankingTierImage);
        TextView rankingText = (TextView) findViewById(R.id.rankingText);
        TextView rankingQueue = (TextView) findViewById(R.id.rankingQueue);

        View rankingHolder = findViewById(R.id.rankingHolder);
        if (player.rank.tier.isEmpty() || !RANKING_TIER_RESOURCES.containsKey(player.rank.tier.toLowerCase())) {
            rankingHolder.setVisibility(View.GONE);
        } else {
            rankingTierImage.setImageResource(RANKING_TIER_RESOURCES.get(player.rank.tier.toLowerCase()));
            rankingText.setText(String.format(getString(R.string.ranking), player.rank.tier.toUpperCase(), player.rank.division));
            rankingHolder.setVisibility(View.VISIBLE);
            rankingQueue.setText(getQueueName(player.rank.queue));
        }

        // MATCHUP INFORMATION
        View matchupHolder = findViewById(R.id.matchupHolder);
        ImageView ownChampion = (ImageView) findViewById(R.id.ownChampion);
        ImageView enemyChampion = (ImageView) findViewById(R.id.enemyChampion);
        TextView matchupTextView = (TextView) findViewById(R.id.matchupStats);

        Team playerTeam = game.getTeamForPlayer(player);
        Team otherTeam = game.teams.get(0) == playerTeam ? game.teams.get(1) : game.teams.get(0);
        Player oppositePlayer = null;
        for (Player tplayer : otherTeam.players) {
            if (player.champion.role.equals(tplayer.champion.role)) {
                oppositePlayer = tplayer;
                break;
            }
        }

        if (playerTeam == null || player.champion.role.equals(Champion.UNKNOWN_ROLE) || oppositePlayer == null) {
            matchupHolder.setVisibility(View.GONE);
        } else {
            ImageLoader.getInstance().displayImage(player.champion.imageUrl, ownChampion);
            ImageLoader.getInstance().displayImage(oppositePlayer.champion.imageUrl, enemyChampion);

            if (player.champion.winRate >= 0) {
                matchupTextView.setText(String.format(Locale.getDefault(), "%d%%", player.champion.winRate));
                if (player.champion.winRate > 50) {
                    matchupTextView.setTextColor(getResources().getColor(R.color.colorGoodMatchup));
                } else if (player.champion.winRate < 50) {
                    matchupTextView.setTextColor(getResources().getColor(R.color.colorBadMatchup));
                }
            } else {
                matchupTextView.setText("?");
                matchupTextView.setTextColor(getResources().getColor(R.color.colorUnknownMatchup));
            }
        }

        // RECENT MATCHES
        TextView recentMatchesText = (TextView) findViewById(R.id.recentMatchesTitle);
        recentMatchesText.setText(String.format(getString(R.string.recent_matches), player.champion.name));
        downloadPerformance();

        // CHAMPION INFORMATION
        TextView abilityTitle = (TextView) findViewById(R.id.abilityTitle);
        abilityTitle.setText(String.format(getString(R.string.champion_ability), player.champion.name));
        downloadChampionDetails(player.champion);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.action_gg) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(player.champion.ggUrl));
            startActivity(browserIntent);

            Tracker.trackClickOnGG(this, player);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_champion_detail, menu);

        return true;
    }

    public void downloadPerformance() {
        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(this);

        try {
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, ((LolApplication) getApplication()).getApiUrl() + "/summoner/performance?summoner=" + URLEncoder.encode(player.summoner.name, "UTF-8") + "&region=" + player.region + "&champion=" + URLEncoder.encode(player.champion.name, "UTF-8"), null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            ArrayList<Match> matches = Match.getMatches(response);
                            displayPerformance(matches);

                            Log.i(TAG, "Displaying performance for " + player.summoner.name);

                            Tracker.trackDetailsViewed(ChampionDetailActivity.this, player);

                            queue.stop();

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    findViewById(R.id.progressBar).setVisibility(View.GONE);

                    Log.e(TAG, error.toString());

                    queue.stop();

                    if (error instanceof NoConnectionError) {
                        displaySnack(getString(R.string.no_internet_connection));
                        return;
                    }


                    try {
                        String responseBody = new String(error.networkResponse.data, "utf-8");
                        Log.i(TAG, responseBody);

                        Tracker.trackErrorViewingDetails(ChampionDetailActivity.this, responseBody.replace("Error:", ""));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        // Do nothing, no text content in the HTTP reply.
                    }

                    findViewById(R.id.matchHistoryHolder).setVisibility(View.INVISIBLE);
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

    private void displayPerformance(ArrayList<Match> matches) {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setVisibility(View.VISIBLE);
        findViewById(R.id.progressBar).setVisibility(View.GONE);
        recyclerView.setAdapter(new MatchAdapter(matches));

        if (matches.size() == 0) {
            findViewById(R.id.matchHistoryHolder).setVisibility(View.INVISIBLE);
        }
    }

    private void downloadChampionDetails(final Champion champion) {
        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, ((LolApplication) getApplication()).getApiUrl() + "/champion/" + Integer.toString(champion.id) + "&locale=" + Locale.getDefault().toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        displayChampionDetails(response);

                        Log.i(TAG, "Displaying champion details for " + champion.name);
                        queue.stop();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
                queue.stop();
            }
        });

        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonRequest);
    }

    private void displayChampionDetails(JSONObject champion) {
        try {
            displayChampionAbility(getString(R.string.ability_passive), champion.getJSONObject("passive"), findViewById(R.id.abilityP));
            displayChampionAbility(getString(R.string.ability_q), champion.getJSONArray("spells").getJSONObject(0), findViewById(R.id.abilityQ));
            displayChampionAbility(getString(R.string.ability_w), champion.getJSONArray("spells").getJSONObject(1), findViewById(R.id.abilityW));
            displayChampionAbility(getString(R.string.ability_e), champion.getJSONArray("spells").getJSONObject(2), findViewById(R.id.abilityE));
            displayChampionAbility(getString(R.string.ability_r), champion.getJSONArray("spells").getJSONObject(3), findViewById(R.id.abilityR));

            findViewById(R.id.championAbilityDetails).setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void displayChampionAbility(String name, JSONObject ability, View holder) throws JSONException {
        ((TextView) holder.findViewById(R.id.abilityName)).setText(String.format(getString(R.string.ability_name), name, ability.getString("name")));
        ((TextView) holder.findViewById(R.id.abilityDescription)).setText(ability.getString("description"));

        if(ability.has("cooldowns")) {
            ((TextView) holder.findViewById(R.id.abilityCooldown)).setText(String.format(getString(R.string.ability_cooldowns), ability.getString("cooldowns")));
        }
        else {
            holder.findViewById(R.id.abilityCooldown).setVisibility(View.GONE);
        }
        ImageLoader.getInstance().displayImage(ability.getString("image"), (ImageView) holder.findViewById(R.id.abilityImage));
    }
}
