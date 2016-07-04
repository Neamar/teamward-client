package fr.neamar.lolgamedata.tips.builder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.holder.TipHolder;
import fr.neamar.lolgamedata.pojo.Game;
import fr.neamar.lolgamedata.tips.PremadeTip;
import fr.neamar.lolgamedata.tips.Tip;
import fr.neamar.lolgamedata.tips.holder.PremadeTipHolder;

/**
 * Created by neamar on 04/07/16.
 */
public class PremadeTipBuilder extends TipBuilder {
    public ArrayList<Tip> getTips(Game game) {
        PremadeTip premadeTip = new PremadeTip(game);

        ArrayList<Tip> tips = new ArrayList<>();
        tips.add(premadeTip);
        return tips;
    }

    public static TipHolder onCreateViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.item_tip_premade, parent, false);

        return new PremadeTipHolder(view);
    }
}
