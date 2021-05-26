package fr.neamar.lolgamedata.tips.builder;

import android.content.Context;

import java.util.ArrayList;

import fr.neamar.lolgamedata.pojo.Game;
import fr.neamar.lolgamedata.tips.PlayerStandardTip;
import fr.neamar.lolgamedata.tips.Tip;

public class HttpTipBuilder extends TipBuilder {
    @Override
    public ArrayList<Tip> getTips(Game game, Context context) {
        ArrayList<Tip> tips = new ArrayList<>();

        for(int i = 0; i < game.httpTips.size(); i++) {
                fr.neamar.lolgamedata.pojo.Tip tip = game.httpTips.get(i);
                PlayerStandardTip standardTip = new PlayerStandardTip(game, null, tip.image, tip.title, tip.description);
                standardTip.urlTarget = tip.link;
                tips.add(standardTip);

        }

        return tips;
    }
}
