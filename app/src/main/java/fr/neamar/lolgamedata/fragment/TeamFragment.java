package fr.neamar.lolgamedata.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.adapter.PlayerAdapter;
import fr.neamar.lolgamedata.pojo.Player;


public class TeamFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_SECTION_PLAYERS = "section_players";

    private ArrayList<Player> players;

    public TeamFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static TeamFragment newInstance(int sectionNumber, ArrayList<Player> players) {
        TeamFragment fragment = new TeamFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putSerializable(ARG_SECTION_PLAYERS, players);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_team, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));

        players = (ArrayList<Player>) getArguments().getSerializable(ARG_SECTION_PLAYERS);
        PlayerAdapter adapter = new PlayerAdapter(players);
        recyclerView.setAdapter(adapter);
        return rootView;
    }
}
