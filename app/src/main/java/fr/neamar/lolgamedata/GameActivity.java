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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.neamar.lolgamedata.fragment.TeamFragment;
import fr.neamar.lolgamedata.pojo.Game;
import fr.neamar.lolgamedata.pojo.Player;
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

        try {
            dummy = new JSONObject("{\"map_id\":11,\"teams\":[{\"team_id\":100,\"players\":[{\"team_id\":100,\"summoner\":{\"id\":70448430,\"name\":\"Neamar\",\"level\":30},\"champion\":{\"name\":\"Illaoi\",\"image\":\"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/champion/Illaoi.png\",\"level\":5,\"champion_rank\":1},\"known_champions\":27,\"spell_d\":{\"name\":\"Flash\",\"image\":\"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/spell/SummonerFlash.png\"},\"spell_f\":{\"name\":\"Smite\",\"image\":\"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/spell/SummonerSmite.png\"},\"currentSeasonRank\":{\"tier\":\"\",\"division\":\"\"}},{\"team_id\":100,\"summoner\":{\"id\":19083089,\"name\":\"MrFaper\",\"level\":30},\"champion\":{\"name\":\"Gragas\",\"image\":\"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/champion/Gragas.png\",\"level\":0,\"champion_rank\":-1},\"known_champions\":-1,\"spell_d\":{\"name\":\"Flash\",\"image\":\"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/spell/SummonerFlash.png\"},\"spell_f\":{\"name\":\"Ignite\",\"image\":\"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/spell/SummonerDot.png\"},\"currentSeasonRank\":{\"tier\":\"GOLD\",\"division\":\"I\"}},{\"team_id\":100,\"summoner\":{\"id\":19917877,\"name\":\"whitewolfhd\",\"level\":30},\"champion\":{\"name\":\"Tristana\",\"image\":\"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/champion/Tristana.png\",\"level\":0,\"champion_rank\":-1},\"known_champions\":-1,\"spell_d\":{\"name\":\"Flash\",\"image\":\"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/spell/SummonerFlash.png\"},\"spell_f\":{\"name\":\"Heal\",\"image\":\"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/spell/SummonerHeal.png\"},\"currentSeasonRank\":{\"tier\":\"GOLD\",\"division\":\"V\"}},{\"team_id\":100,\"summoner\":{\"id\":57780340,\"name\":\"XxxJorge19xxX\",\"level\":30},\"champion\":{\"name\":\"Blitzcrank\",\"image\":\"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/champion/Blitzcrank.png\",\"level\":0,\"champion_rank\":-1},\"known_champions\":-1,\"spell_d\":{\"name\":\"Flash\",\"image\":\"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/spell/SummonerFlash.png\"},\"spell_f\":{\"name\":\"Exhaust\",\"image\":\"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/spell/SummonerExhaust.png\"},\"currentSeasonRank\":{\"tier\":\"\",\"division\":\"\"}},{\"team_id\":100,\"summoner\":{\"id\":53870009,\"name\":\"ORIOLbrcnslpz\",\"level\":30},\"champion\":{\"name\":\"Irelia\",\"image\":\"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/champion/Irelia.png\",\"level\":0,\"champion_rank\":-1},\"known_champions\":-1,\"spell_d\":{\"name\":\"Flash\",\"image\":\"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/spell/SummonerFlash.png\"},\"spell_f\":{\"name\":\"Teleport\",\"image\":\"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/spell/SummonerTeleport.png\"},\"currentSeasonRank\":{\"tier\":\"\",\"division\":\"\"}}]},{\"team_id\":200,\"players\":[{\"team_id\":200,\"summoner\":{\"id\":19917878,\"name\":\"Touchpad Hero\",\"level\":30},\"champion\":{\"name\":\"Lee Sin\",\"image\":\"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/champion/LeeSin.png\",\"level\":0,\"champion_rank\":-1},\"known_champions\":-1,\"spell_d\":{\"name\":\"Smite\",\"image\":\"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/spell/SummonerSmite.png\"},\"spell_f\":{\"name\":\"Flash\",\"image\":\"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/spell/SummonerFlash.png\"},\"currentSeasonRank\":{\"tier\":\"\",\"division\":\"\"}},{\"team_id\":200,\"summoner\":{\"id\":27321542,\"name\":\"LeGrasCèLaVie\",\"level\":30},\"champion\":{\"name\":\"LeBlanc\",\"image\":\"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/champion/Leblanc.png\",\"level\":0,\"champion_rank\":-1},\"known_champions\":-1,\"spell_d\":{\"name\":\"Ignite\",\"image\":\"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/spell/SummonerDot.png\"},\"spell_f\":{\"name\":\"Flash\",\"image\":\"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/spell/SummonerFlash.png\"},\"currentSeasonRank\":{\"tier\":\"\",\"division\":\"\"}},{\"team_id\":200,\"summoner\":{\"id\":78179191,\"name\":\"Acarys\",\"level\":30},\"champion\":{\"name\":\"Aatrox\",\"image\":\"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/champion/Aatrox.png\",\"level\":0,\"champion_rank\":-1},\"known_champions\":-1,\"spell_d\":{\"name\":\"Flash\",\"image\":\"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/spell/SummonerFlash.png\"},\"spell_f\":{\"name\":\"Teleport\",\"image\":\"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/spell/SummonerTeleport.png\"},\"currentSeasonRank\":{\"tier\":\"\",\"division\":\"\"}},{\"team_id\":200,\"summoner\":{\"id\":38621938,\"name\":\"TDK d view\",\"level\":30},\"champion\":{\"name\":\"Veigar\",\"image\":\"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/champion/Veigar.png\",\"level\":0,\"champion_rank\":-1},\"known_champions\":-1,\"spell_d\":{\"name\":\"Flash\",\"image\":\"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/spell/SummonerFlash.png\"},\"spell_f\":{\"name\":\"Ignite\",\"image\":\"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/spell/SummonerDot.png\"},\"currentSeasonRank\":{\"tier\":\"\",\"division\":\"\"}},{\"team_id\":200,\"summoner\":{\"id\":79947339,\"name\":\"Scrubalist\",\"level\":28},\"champion\":{\"name\":\"Kog'Maw\",\"image\":\"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/champion/KogMaw.png\",\"level\":0,\"champion_rank\":-1},\"known_champions\":-1,\"spell_d\":{\"name\":\"Heal\",\"image\":\"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/spell/SummonerHeal.png\"},\"spell_f\":{\"name\":\"Flash\",\"image\":\"http://ddragon.leagueoflegends.com/cdn/6.5.1/img/spell/SummonerFlash.png\"},\"currentSeasonRank\":{\"tier\":\"\",\"division\":\"\"}}]}]}");

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
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }
}
