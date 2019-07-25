package fr.neamar.lolgamedata.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.holder.PlayerHolder;
import fr.neamar.lolgamedata.pojo.Game;
import fr.neamar.lolgamedata.pojo.Player;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerHolder> {
    private final ArrayList<Player> players;
    private final Game game;

    public PlayerAdapter(ArrayList<Player> players, Game game) {
        this.players = players;
        this.game = game;
    }

    @Override
    public PlayerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.item_player, parent, false);

        return new PlayerHolder(view);
    }

    @Override
    public void onBindViewHolder(PlayerHolder holder, int position) {
        holder.bind(players.get(position), game);
    }

    @Override
    public int getItemCount() {
        return players.size();
    }
}
