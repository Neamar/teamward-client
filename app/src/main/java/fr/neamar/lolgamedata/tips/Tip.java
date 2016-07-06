package fr.neamar.lolgamedata.tips;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.neamar.lolgamedata.pojo.Game;
import fr.neamar.lolgamedata.tips.builder.ColdStreakTipBuilder;
import fr.neamar.lolgamedata.tips.builder.FilthyCasualTipBuilder;
import fr.neamar.lolgamedata.tips.builder.HardcoreGamerTipBuilder;
import fr.neamar.lolgamedata.tips.builder.HotStreakTipBuilder;
import fr.neamar.lolgamedata.tips.builder.PremadeTipBuilder;
import fr.neamar.lolgamedata.tips.builder.TipBuilder;

/**
 * Created by neamar on 04/07/16.
 */
public class Tip {
    public static final List<TipBuilder> tipsBuilders = new ArrayList<>(Arrays.asList(
            new TipBuilder(),
            new PremadeTipBuilder(),
            new HotStreakTipBuilder(),
            new ColdStreakTipBuilder(),
            new HardcoreGamerTipBuilder(),
            new FilthyCasualTipBuilder()
    ));

    public static ArrayList<Tip> getTips(Game game, Context context) {
        ArrayList<Tip> tips = new ArrayList<>();

        for (TipBuilder tipBuilder : tipsBuilders) {
            ArrayList<Tip> newTips = tipBuilder.getTips(game, context);
            for (Tip tip : newTips) {
                tips.add(tip);
            }
        }

        return tips;
    }

    public Game game;

    public Tip(Game game) {
        this.game = game;
    }
}
