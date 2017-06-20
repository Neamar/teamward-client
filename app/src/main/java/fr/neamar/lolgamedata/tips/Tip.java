package fr.neamar.lolgamedata.tips;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.neamar.lolgamedata.pojo.Game;
import fr.neamar.lolgamedata.tips.builder.DoubleSmiteTipBuilder;
import fr.neamar.lolgamedata.tips.builder.FullAPDTeamTipBuilder;
import fr.neamar.lolgamedata.tips.builder.MatchupsBuilder;
import fr.neamar.lolgamedata.tips.builder.NoFlashTipBuilder;
import fr.neamar.lolgamedata.tips.builder.NoSmiteTipBuilder;
import fr.neamar.lolgamedata.tips.builder.NoobTipBuilder;
import fr.neamar.lolgamedata.tips.builder.OtpTipBuilder;
import fr.neamar.lolgamedata.tips.builder.PremadeNotUsingTeamwardTipBuilder;
import fr.neamar.lolgamedata.tips.builder.PremadeTipBuilder;
import fr.neamar.lolgamedata.tips.builder.TipBuilder;
import fr.neamar.lolgamedata.tips.builder.TryHarderTipBuilder;

public class Tip {
    private static final List<TipBuilder> tipsBuilders = new ArrayList<>(Arrays.asList(
            new TipBuilder(),
            new NoSmiteTipBuilder(),
            new DoubleSmiteTipBuilder(),
            new TryHarderTipBuilder(),
            new FullAPDTeamTipBuilder(),
            new NoobTipBuilder(),
            new OtpTipBuilder(),
            new PremadeTipBuilder(),
            new MatchupsBuilder(),
            new NoFlashTipBuilder(),
            new PremadeNotUsingTeamwardTipBuilder()
    ));
    public final Game game;

    Tip(Game game) {
        this.game = game;
    }

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
}
