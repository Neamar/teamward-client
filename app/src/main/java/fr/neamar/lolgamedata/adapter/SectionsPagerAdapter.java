package fr.neamar.lolgamedata.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.ArrayList;

import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.fragment.TeamFragment;
import fr.neamar.lolgamedata.fragment.TipFragment;
import fr.neamar.lolgamedata.pojo.Game;
import fr.neamar.lolgamedata.pojo.Team;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
    public final Context context;
    public Game game;
    public ArrayList<Team> teams = new ArrayList<>();

    public SectionsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    public void setGame(Game game) {
        this.game = game;
        this.teams = game.teams;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        Log.e("WTF", "POS" + position);
        Log.e("WTF", "TEAM" + teams.size());
        if(position == teams.size()) {
            return TipFragment.newInstance(position + 1, game);
        }

        // getItem is called to instantiate the fragment for the given page.
        return TeamFragment.newInstance(position + 1, teams.get(position));
    }

    @Override
    public int getCount() {
        if(teams.size() == 0) {
            return 0;
        }
        return teams.size() + 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position == teams.size()) {
            return "Tips";
        }

        int teamId = teams.get(position).teamId;

        if (teamId == 100) {
            return context.getString(R.string.blue_team);
        } else if (teamId == 200) {
            return context.getString(R.string.red_team);
        }

        return context.getString(R.string.unknown_team);
    }

    public int getItemPosition(Object item) {
        // Fix for a very weird bug:
        // http://stackoverflow.com/questions/10849552/update-viewpager-dynamically
        // FragmentAdapter will not refresh its content unless forced to do so
        // even if notifyDatasetChanged() was called.
        // So when new data is sent, ensure old fragments are removed.
        if(item instanceof TeamFragment) {
            TeamFragment teamFragment = (TeamFragment) item;
            if (teams.indexOf(teamFragment.getTeam()) == -1) {
                return POSITION_NONE;
            }
        }

        return super.getItemPosition(item);
    }
}