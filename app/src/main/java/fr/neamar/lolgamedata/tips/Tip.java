package fr.neamar.lolgamedata.tips;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.neamar.lolgamedata.pojo.Game;
import fr.neamar.lolgamedata.tips.builder.PremadeTipBuilder;
import fr.neamar.lolgamedata.tips.builder.TipBuilder;

/**
 * Created by neamar on 04/07/16.
 */
public class Tip {
    public static final List<TipBuilder> tipsBuilders = new ArrayList<>(Arrays.asList(new TipBuilder(), new PremadeTipBuilder()));

    public static ArrayList<Tip> getTips(Game game) {
        ArrayList<Tip> tips = new ArrayList<>();

        for(TipBuilder tipBuilder: tipsBuilders) {
            ArrayList<Tip> newTips = tipBuilder.getTips(game);
            for(Tip tip: newTips) {
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
