package fr.neamar.lolgamedata;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.neamar.lolgamedata.fragment.TeamFragment;
import fr.neamar.lolgamedata.pojo.Game;
import fr.neamar.lolgamedata.pojo.Team;

public class GameActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;


    private JSONObject dummy;
    private Game game;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Create global configuration and initialize ImageLoader with this config
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);

        try {
            dummy = new JSONObject("{\"map_id\": 11,\"teams\": [{\"team_id\": 100,\"players\": [{\"team_id\": 100,\"summoner\": {\"id\": 23407958,\"name\": \"Mr Coconuts\",\"level\": 30},\"champion\": {\"id\": \"136\",\"name\": \"Aurelion Sol\",\"image\": \"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/champion/AurelionSol.png\",\"mastery\": 2,\"champion_rank\": 29},\"known_champions\": 53,\"spell_d\": {\"name\": \"Flash\",\"image\": \"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/spell/SummonerFlash.png\"},\"spell_f\": {\"name\": \"Smite\",\"image\": \"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/spell/SummonerSmite.png\"},\"current_season_rank\": {\"tier\": \"GOLD\",\"division\": \"III\"}},{\"team_id\": 100,\"summoner\": {\"id\": 23471946,\"name\": \"Quéen\",\"level\": 30},\"champion\": {\"id\": \"58\",\"name\": \"Renekton\",\"image\": \"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/champion/Renekton.png\",\"mastery\": 3,\"champion_rank\": 13},\"known_champions\": 79,\"spell_d\": {\"name\": \"Flash\",\"image\": \"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/spell/SummonerFlash.png\"},\"spell_f\": {\"name\": \"Teleport\",\"image\": \"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/spell/SummonerTeleport.png\"},\"current_season_rank\": {\"tier\": \"PLATINUM\",\"division\": \"I\"}},{\"team_id\": 100,\"summoner\": {\"id\": 19489169,\"name\": \"Majestrix\",\"level\": 30},\"champion\": {\"id\": \"127\",\"name\": \"Lissandra\",\"image\": \"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/champion/Lissandra.png\",\"mastery\": 2,\"champion_rank\": 22},\"known_champions\": 73,\"spell_d\": {\"name\": \"Flash\",\"image\": \"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/spell/SummonerFlash.png\"},\"spell_f\": {\"name\": \"Ignite\",\"image\": \"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/spell/SummonerDot.png\"},\"current_season_rank\": {\"tier\": \"PLATINUM\",\"division\": \"IV\"}},{\"team_id\": 100,\"summoner\": {\"id\": 33943853,\"name\": \"Napkin Holder\",\"level\": 30},\"champion\": {\"id\": \"21\",\"name\": \"Miss Fortune\",\"image\": \"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/champion/MissFortune.png\",\"mastery\": 3,\"champion_rank\": 10},\"known_champions\": 38,\"spell_d\": {\"name\": \"Heal\",\"image\": \"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/spell/SummonerHeal.png\"},\"spell_f\": {\"name\": \"Flash\",\"image\": \"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/spell/SummonerFlash.png\"},\"current_season_rank\": {\"tier\": \"PLATINUM\",\"division\": \"I\"}},{\"team_id\": 100,\"summoner\": {\"id\": 19686199,\"name\": \"Lascar24\",\"level\": 30},\"champion\": {\"id\": \"201\",\"name\": \"Braum\",\"image\": \"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/champion/Braum.png\",\"mastery\": 4,\"champion_rank\": 2},\"known_champions\": 15,\"spell_d\": {\"name\": \"Ignite\",\"image\": \"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/spell/SummonerDot.png\"},\"spell_f\": {\"name\": \"Flash\",\"image\": \"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/spell/SummonerFlash.png\"},\"current_season_rank\": {\"tier\": \"GOLD\",\"division\": \"IV\"}}]},{\"team_id\": 200,\"players\": [{\"team_id\": 200,\"summoner\": {\"id\": 19083089,\"name\": \"N4dlPb\",\"level\": 30},\"champion\": {\"id\": \"143\",\"name\": \"Zyra\",\"image\": \"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/champion/Zyra.png\",\"mastery\": 5,\"champion_rank\": 1},\"known_champions\": 39,\"spell_d\": {\"name\": \"Exhaust\",\"image\": \"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/spell/SummonerExhaust.png\"},\"spell_f\": {\"name\": \"Flash\",\"image\": \"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/spell/SummonerFlash.png\"},\"current_season_rank\": {\"tier\": \"PLATINUM\",\"division\": \"V\"}},{\"team_id\": 200,\"summoner\": {\"id\": 27861215,\"name\": \"Heaven Buster\",\"level\": 30},\"champion\": {\"id\": \"421\",\"name\": \"Rek'Sai\",\"image\": \"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/champion/RekSai.png\",\"mastery\": 3,\"champion_rank\": 44},\"known_champions\": 111,\"spell_d\": {\"name\": \"Flash\",\"image\": \"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/spell/SummonerFlash.png\"},\"spell_f\": {\"name\": \"Smite\",\"image\": \"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/spell/SummonerSmite.png\"},\"current_season_rank\": {\"tier\": \"PLATINUM\",\"division\": \"III\"}},{\"team_id\": 200,\"summoner\": {\"id\": 37710585,\"name\": \"BobZer\",\"level\": 30},\"champion\": {\"id\": \"85\",\"name\": \"Kennen\",\"image\": \"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/champion/Kennen.png\",\"mastery\": 5,\"champion_rank\": 4},\"known_champions\": 61,\"spell_d\": {\"name\": \"Flash\",\"image\": \"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/spell/SummonerFlash.png\"},\"spell_f\": {\"name\": \"Teleport\",\"image\": \"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/spell/SummonerTeleport.png\"},\"current_season_rank\": {\"tier\": \"PLATINUM\",\"division\": \"V\"}},{\"team_id\": 200,\"summoner\": {\"id\": 51398158,\"name\": \"LordWup\",\"level\": 30},\"champion\": {\"id\": \"81\",\"name\": \"Ezreal\",\"image\": \"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/champion/Ezreal.png\",\"mastery\": 4,\"champion_rank\": 6},\"known_champions\": 89,\"spell_d\": {\"name\": \"Flash\",\"image\": \"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/spell/SummonerFlash.png\"},\"spell_f\": {\"name\": \"Heal\",\"image\": \"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/spell/SummonerHeal.png\"},\"current_season_rank\": {\"tier\": \"PLATINUM\",\"division\": \"V\"}},{\"team_id\": 200,\"summoner\": {\"id\": 43652731,\"name\": \"Not Anonymos\",\"level\": 30},\"champion\": {\"id\": \"238\",\"name\": \"Zed\",\"image\": \"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/champion/Zed.png\",\"mastery\": 3,\"champion_rank\": 11},\"known_champions\": 96,\"spell_d\": {\"name\": \"Flash\",\"image\": \"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/spell/SummonerFlash.png\"},\"spell_f\": {\"name\": \"Ignite\",\"image\": \"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/spell/SummonerDot.png\"},\"current_season_rank\": {\"tier\": \"PLATINUM\",\"division\": \"V\"}}]}]}");

            game = new Game(dummy);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("WTF", "WTF");
            Log.e("WTF", e.toString());
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), game.teams);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        assert mViewPager != null;
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        assert tabLayout != null;
        tabLayout.setupWithViewPager(mViewPager);

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
        // automatically handle clicks on the Home/Up button, so long
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

            if(teamId == 100) {
                return getString(R.string.blue_team);
            }
            else if(teamId == 200) {
                return getString(R.string.red_team);
            }

            return getString(R.string.unknown_team);
        }
    }
}
