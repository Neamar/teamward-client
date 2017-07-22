package fr.neamar.lolgamedata.tips.builder;

import android.content.Context;

import java.util.ArrayList;

import fr.neamar.lolgamedata.pojo.Game;
import fr.neamar.lolgamedata.tips.Tip;
import fr.neamar.lolgamedata.tips.WinrateByTimeTip;

public class WinrateByTimeTipBuilder extends TipBuilder {

    @Override
    public ArrayList<Tip> getTips(Game game, Context context) {
        ArrayList<Tip> tips = new ArrayList<>();

        tips.add(new WinrateByTimeTip(game));

        return tips;
    }
}
