package fr.neamar.lolgamedata.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.pojo.Account;

/**
 * A placeholder fragment containing a simple view.
 */
public class CounterFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_ROLE = "role";
    private static final String ARG_SUMMONER = "summoner";

    public String role;
    public Account user;

    public CounterFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static CounterFragment newInstance(int sectionNumber, Account user) {
        CounterFragment fragment = new CounterFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ROLE, sectionNumber);
        args.putSerializable(ARG_SUMMONER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        role = getArguments().getString(ARG_ROLE);
        user = (Account) getArguments().getSerializable(ARG_SUMMONER);

        View rootView = inflater.inflate(R.layout.fragment_counter, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(user.summonerName);
        return rootView;
    }
}