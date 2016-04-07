package fr.neamar.lolgamedata;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
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
import java.util.ArrayList;

import fr.neamar.lolgamedata.fragment.TeamFragment;
import fr.neamar.lolgamedata.pojo.Account;
import fr.neamar.lolgamedata.pojo.Game;
import fr.neamar.lolgamedata.pojo.Team;

public class GameActivity extends AppCompatActivity {
    public static final String TAG = "GameActivity";

    public static final int NO_GAME_FOUND = 44;

    public Account account;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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

        account = (Account) getIntent().getSerializableExtra("account");

        ((LolApplication) getApplication()).getMixpanel().getPeople().increment("games_viewed_count", 1);
        ((LolApplication) getApplication()).getMixpanel().timeEvent("Game viewed");


        loadCurrentGame(account.summonerName, account.region);
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


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public ArrayList<Team> teams;

        public SectionsPagerAdapter(FragmentManager fm, ArrayList<Team> teams) {
            super(fm);
            this.teams = teams;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            return TeamFragment.newInstance(position + 1, teams.get(position));
        }

        @Override
        public int getCount() {
            return teams.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            int teamId = teams.get(position).teamId;

            if (teamId == 100) {
                return getString(R.string.blue_team);
            } else if (teamId == 200) {
                return getString(R.string.red_team);
            }

            return getString(R.string.unknown_team);
        }
    }

    public void loadCurrentGame(final String summonerName, final String region) {
        final ProgressDialog dialog = ProgressDialog.show(this, "",
                String.format(getString(R.string.loading_game_data), summonerName), true);
        dialog.show();

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, "http://lol-game-stats.herokuapp.com/game/data?summoner=" + summonerName + "&region=" + region, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Game game = new Game(response);
                            displayGame(summonerName, game);

                            // Timing automatically added (see timeEvent() call)
                            JSONObject j = account.toJsonObject();
                            ((LolApplication) getApplication()).getMixpanel().track("Game viewed", j);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
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

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                }

                finish();
            }
        });

        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonRequest);
    }

    public void displayGame(String summonerName, Game game) {
        String titleTemplate = getString(R.string.game_data_title);
        setTitle(String.format(titleTemplate, summonerName));
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), game.teams);

        // Set up the ViewPager with the sections adapter.
        assert mViewPager != null;
        assert mTabLayout != null;

        mViewPager.setAdapter(mSectionsPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setVisibility(View.VISIBLE);
    }
}
