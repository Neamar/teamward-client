package fr.neamar.lolgamedata.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;

import fr.neamar.lolgamedata.tips.HotStreakTip;
import fr.neamar.lolgamedata.tips.PremadeTip;
import fr.neamar.lolgamedata.tips.builder.HotStreakTipBuilder;
import fr.neamar.lolgamedata.tips.builder.PremadeTipBuilder;
import fr.neamar.lolgamedata.tips.Tip;
import fr.neamar.lolgamedata.holder.TipHolder;
import fr.neamar.lolgamedata.pojo.Game;

/**
 * Created by neamar on 04/07/16.
 */
public class TipAdapter extends RecyclerView.Adapter<TipHolder> {
    public ArrayList<Tip> tips;
    public TipAdapter(Game game) {
        tips = Tip.getTips(game);
    }

    @Override
    public TipHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == PremadeTip.class.getName().hashCode()) {
            return PremadeTipBuilder.onCreateViewHolder(parent);
        }
        else if(viewType == HotStreakTip.class.getName().hashCode()) {
            return HotStreakTipBuilder.onCreateViewHolder(parent);
        }

        throw new RuntimeException("Unknown tip class!");
    }

    @Override
    public void onBindViewHolder(TipHolder holder, int position) {
        holder.bindTip(tips.get(position));

    }

    @Override
    public int getItemCount() {
        return tips.size();
    }

    @Override
    public int getItemViewType(int position) {
        return tips.get(position).getClass().getName().hashCode();
    }
}
