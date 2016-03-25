package fr.neamar.lolgamedata.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.holder.PlayerHolder;
import fr.neamar.lolgamedata.pojo.Player;

/**
 * Created by neamar on 25/03/16.
 */
public class PlayerAdapter extends RecyclerView.Adapter<PlayerHolder> {
    public ArrayList<Player> players;

    public PlayerAdapter(ArrayList<Player> players) {
        this.players = players;
    }

    @Override
    public PlayerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.item_player, parent, false);

        return new PlayerHolder(view);
    }

    @Override
    public void onBindViewHolder(PlayerHolder holder, int position) {
        holder.bindAdvert(players.get(position));
    }

    @Override
    public int getItemCount() {
        return players.size();
    }
}
