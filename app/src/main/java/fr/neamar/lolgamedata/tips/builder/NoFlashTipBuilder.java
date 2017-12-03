package fr.neamar.lolgamedata.tips.builder;

import android.content.Context;

import java.util.ArrayList;

import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.pojo.Game;
import fr.neamar.lolgamedata.pojo.Player;
import fr.neamar.lolgamedata.pojo.Team;
import fr.neamar.lolgamedata.tips.ChampionStandardTip;
import fr.neamar.lolgamedata.tips.Tip;

public class NoFlashTipBuilder extends TipBuilder {
    @Override
    public ArrayList<Tip> getTips(Game game, Context context) {
        ArrayList<Tip> tips = new ArrayList<>();

        for (Team team : game.teams) {
            for (Player player : team.players) {
                if (!player.spellD.name.equals("Flash") && !player.spellF.name.equals("Flash")) {
                    String descriptionTemplate = context.getString(R.string.s_has_no_flash);
                    String description = String.format(descriptionTemplate, player.summoner.name, player.spellD.name, player.spellF.name);
                    tips.add(new ChampionStandardTip(game, player, player.champion, context.getString(R.string.no_flash), description));
                }
            }
        }

        return tips;
    }
}
