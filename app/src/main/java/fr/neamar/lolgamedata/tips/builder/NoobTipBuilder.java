package fr.neamar.lolgamedata.tips.builder;

import android.content.Context;

import java.util.ArrayList;

import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.pojo.Game;
import fr.neamar.lolgamedata.pojo.Player;
import fr.neamar.lolgamedata.pojo.Team;
import fr.neamar.lolgamedata.tips.ChampionStandardTip;
import fr.neamar.lolgamedata.tips.Tip;

public class NoobTipBuilder extends TipBuilder {
    @Override
    public ArrayList<Tip> getTips(Game game, Context context) {
        ArrayList<Tip> tips = new ArrayList<>();

        for (Team team : game.teams) {
            for (Player player : team.players) {
                if (player.champion.mastery == 0) {
                    String descriptionTemplate = context.getString(R.string.is_playing_for_first_time);
                    String description = String.format(descriptionTemplate, player.summoner.name, player.champion.name);
                    tips.add(new ChampionStandardTip(game, player, player.champion, context.getString(R.string.noobs_noobs_everywhere), description));
                }
            }
        }

        return tips;
    }
}
