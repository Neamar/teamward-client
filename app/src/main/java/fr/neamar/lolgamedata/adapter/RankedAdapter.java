package fr.neamar.lolgamedata.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.holder.RankHolder;
import fr.neamar.lolgamedata.pojo.Player;

public class RankedAdapter extends RecyclerView.Adapter<RankHolder> {
    private final Player player;

    public RankedAdapter(Player player) {
        this.player = player;
    }

    @Override
    public RankHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.item_ranked, parent, false);

        return new RankHolder(view);
    }

    @Override
    public void onBindViewHolder(RankHolder holder, int position) {
        holder.bind(player, player.allRanks.get(position));
    }

    @Override
    public long getItemId(int position) {
        return player.allRanks.get(position).hashCode();
    }

    @Override
    public int getItemCount() {
        return player.allRanks.size();
    }
}
