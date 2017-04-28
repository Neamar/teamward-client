package fr.neamar.lolgamedata.tips.builder;

import android.content.Context;

import java.util.ArrayList;

import fr.neamar.lolgamedata.pojo.Game;
import fr.neamar.lolgamedata.tips.PremadeTip;
import fr.neamar.lolgamedata.tips.Tip;

public class PremadeTipBuilder extends TipBuilder {
    @Override
    public ArrayList<Tip> getTips(Game game, Context context) {

        ArrayList<Tip> tips = new ArrayList<>();

        if (game.teams.get(0).premades.size() != 0) {
            PremadeTip premadeTip = new PremadeTip(game);
            tips.add(premadeTip);
        }
        return tips;
    }
}
