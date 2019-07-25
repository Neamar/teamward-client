package fr.neamar.lolgamedata.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;

import fr.neamar.lolgamedata.holder.TipHolder;
import fr.neamar.lolgamedata.pojo.Game;
import fr.neamar.lolgamedata.tips.MatchupsTip;
import fr.neamar.lolgamedata.tips.PlayerStandardTip;
import fr.neamar.lolgamedata.tips.PremadeTip;
import fr.neamar.lolgamedata.tips.Tip;
import fr.neamar.lolgamedata.tips.WinrateByTimeTip;
import fr.neamar.lolgamedata.tips.holder.MatchupsTipHolder;
import fr.neamar.lolgamedata.tips.holder.PlayerStandardTipHolder;
import fr.neamar.lolgamedata.tips.holder.PremadeTipHolder;
import fr.neamar.lolgamedata.tips.holder.WinrateByTimeTipHolder;

public class TipAdapter extends RecyclerView.Adapter<TipHolder> {
    private final ArrayList<Tip> tips;

    public TipAdapter(Game game, Context context) {
        tips = Tip.getTips(game, context);
    }

    @Override
    public TipHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == PremadeTip.class.getName().hashCode()) {
            return PremadeTipHolder.onCreateViewHolder(parent);
        } else if (viewType == PlayerStandardTip.class.getName().hashCode()) {
            return PlayerStandardTipHolder.onCreateViewHolder(parent);
        } else if (viewType == MatchupsTip.class.getName().hashCode()) {
            return MatchupsTipHolder.onCreateViewHolder(parent);
        }
        else if(viewType == WinrateByTimeTip.class.getName().hashCode()) {
            return WinrateByTimeTipHolder.onCreateViewHolder(parent);
        }

        throw new RuntimeException("Unknown tip class!");
    }

    @Override
    public void onBindViewHolder(TipHolder holder, int position) {
        holder.bind(tips.get(position));
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
