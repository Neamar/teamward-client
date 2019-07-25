package fr.neamar.lolgamedata.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.holder.MatchHolder;
import fr.neamar.lolgamedata.pojo.Match;

public class MatchAdapter extends RecyclerView.Adapter<MatchHolder> {
    private final ArrayList<Match> matches;

    public MatchAdapter(ArrayList<Match> matches) {
        this.matches = matches;
    }

    @Override
    public MatchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.item_match, parent, false);

        return new MatchHolder(view);
    }

    @Override
    public void onBindViewHolder(MatchHolder holder, int position) {
        holder.bind(matches.get(position));
    }

    @Override
    public long getItemId(int position) {
        return matches.get(position).hashCode();
    }

    @Override
    public int getItemCount() {
        return matches.size();
    }
}
