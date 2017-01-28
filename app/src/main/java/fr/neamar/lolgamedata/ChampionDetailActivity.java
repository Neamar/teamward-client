package fr.neamar.lolgamedata;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import fr.neamar.lolgamedata.adapter.MatchAdapter;
import fr.neamar.lolgamedata.pojo.Match;
import fr.neamar.lolgamedata.pojo.Player;

import static fr.neamar.lolgamedata.holder.PlayerHolder.CHAMPION_MASTERIES_RESOURCES;
import static fr.neamar.lolgamedata.holder.PlayerHolder.RANKING_TIER_RESOURCES;

public class ChampionDetailActivity extends SnackBarActivity {
    private static final String TAG = "ChampionDetailActivity";
    private Player player;

    private static final Map<String, Integer> QUEUE_NAMES;

    static {
        Map<String, Integer> queueNames = new HashMap<>();
        queueNames.put("RANKED_SOLO_5x5", R.string.ranked_solo_5);
        queueNames.put("RANKED_FLEX_SR", R.string.ranked_flex_5);
        queueNames.put("RANKED_FLEX_TT", R.string.ranked_flex_3);

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

        // player = (Player) getIntent().getSerializableExtra("player");
        try {
            player = new Player(new JSONObject("{\"summoner\": {\"id\": 19445791,\"name\": \"Durandal\",\"level\": 30},\"champion\": {\"id\": \"103\",\"name\": \"Ahri\",\"image\": \"https://ddragon.leagueoflegends.com/cdn/7.1.1/img/champion/Ahri.png\",\"splash\": \"https://ddragon.leagueoflegends.com/cdn/img/champion/splash/Ahri_0.jpg\",\"gg\": \"https://champion.gg/Ahri\",\"ad\": 3,\"ap\": 8,\"mastery\": 1,\"points\": 167,\"champion_rank\": 107,\"role\": \"?\",\"matchup\": {}},\"known_champions\": 114,\"spell_d\": {\"id\": \"SummonerFlash\",\"name\": \"Flash\",\"image\": \"https://ddragon.leagueoflegends.com/cdn/7.1.1/img/spell/SummonerFlash.png\"},\"spell_f\": {\"id\": \"SummonerDot\",\"name\": \"Ignite\",\"image\": \"https://ddragon.leagueoflegends.com/cdn/7.1.1/img/spell/SummonerDot.png\"},\"current_season_rank\": {\"tier\": \"PLATINUM\",\"queue\": \"RANKED_FLEX_SR\",\"division\": \"II\"},\"last_season_rank\": null,\"recent_games\": {\"total\": 10,\"win\": 6,\"loss\": 4,\"average_time_between_games\": 15335}},"), "euw");
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException("FUCK");
        }

        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        final ImageView splashArtImage = (ImageView) findViewById(R.id.splashArt);

        setTitle(player.summoner.name);

        ImageLoader.getInstance().displayImage(player.champion.splashUrl, splashArtImage);

        ImageView championMasteryImage = (ImageView) findViewById(R.id.championMasteryImage);
        TextView championMasteryText = (TextView) findViewById(R.id.championMasteryText);
        View masteryHolder = findViewById(R.id.masteryHolder);

        @DrawableRes
        int championMasteryResource = CHAMPION_MASTERIES_RESOURCES[player.champion.mastery];
        if (championMasteryResource == 0) {
            masteryHolder.setVisibility(View.INVISIBLE);
        } else {
            championMasteryImage.setImageResource(CHAMPION_MASTERIES_RESOURCES[player.champion.mastery]);
            championMasteryText.setText(String.format(getString(R.string.champion_mastery_lvl), player.champion.mastery));
            masteryHolder.setVisibility(View.VISIBLE);
        }

        ImageView rankingTierImage = (ImageView) findViewById(R.id.rankingTierImage);
        TextView rankingText = (TextView) findViewById(R.id.rankingText);
        TextView rankingQueue = (TextView) findViewById(R.id.rankingQueue);

        View rankingHolder = findViewById(R.id.rankingHolder);
        if (player.rank.tier.isEmpty() || !RANKING_TIER_RESOURCES.containsKey(player.rank.tier.toLowerCase())) {
            rankingHolder.setVisibility(View.INVISIBLE);
        } else {
            rankingTierImage.setImageResource(RANKING_TIER_RESOURCES.get(player.rank.tier.toLowerCase()));
            rankingText.setText(String.format(getString(R.string.ranking), player.rank.tier.toUpperCase(), player.rank.division));
            rankingHolder.setVisibility(View.VISIBLE);
            rankingQueue.setText(getQueueName(player.rank.queue));
        }

        downloadPerformance();
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

            JSONObject j = new JSONObject();
            try {
                j.put("name", player.summoner.name);
                j.put("region", player.region.toUpperCase());
                j.put("champion", player.champion.name);
                j.put("tier", player.rank.tier);
                j.put("division", player.rank.division);
                j.put("source", "detail view");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            ((LolApplication) getApplication()).getMixpanel().track("Click on GG", j);

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
        ((LolApplication) getApplication()).getMixpanel().timeEvent("Details viewed");


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

/*                                // Timing automatically added (see timeEvent() call)
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
*/
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
                    } catch (UnsupportedEncodingException e) {
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
        } catch (
                UnsupportedEncodingException e
                )

        {
            e.printStackTrace();
        }

    }

    private void displayPerformance(ArrayList<Match> matches) {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setVisibility(View.VISIBLE);
        findViewById(R.id.progressBar).setVisibility(View.GONE);

        recyclerView.setAdapter(new MatchAdapter(matches));
    }

}
