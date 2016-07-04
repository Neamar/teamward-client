package fr.neamar.lolgamedata.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.holder.TipHolder;
import fr.neamar.lolgamedata.pojo.Game;
import fr.neamar.lolgamedata.cards.tips.Tip;

/**
 * Created by neamar on 04/07/16.
 */
public class TipAdapter extends RecyclerView.Adapter<TipHolder> {

    public TipAdapter(Game game) {
    }

    @Override
    public TipHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.item_tip_premade, parent, false);

        return new TipHolder(view);
    }

    @Override
    public void onBindViewHolder(TipHolder holder, int position) {
        holder.bindTip(new Tip());

    }

    @Override
    public int getItemCount() {
        return 12;
    }
}
