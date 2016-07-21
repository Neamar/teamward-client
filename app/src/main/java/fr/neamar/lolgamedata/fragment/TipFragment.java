package fr.neamar.lolgamedata.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.adapter.TipAdapter;
import fr.neamar.lolgamedata.pojo.Game;

/**
 * Created by neamar on 04/07/16.
 */
public class TipFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_SECTION_GAME = "section_game";

    private Game game;

    public TipFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static TipFragment newInstance(int sectionNumber, Game game) {
        TipFragment fragment = new TipFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putSerializable(ARG_SECTION_GAME, game);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tips, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));

        game = (Game) getArguments().getSerializable(ARG_SECTION_GAME);
        assert game != null;

        TipAdapter adapter = new TipAdapter(game, getContext());
        recyclerView.setAdapter(adapter);
        return rootView;
    }
}
