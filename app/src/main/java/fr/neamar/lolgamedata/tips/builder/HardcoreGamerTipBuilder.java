package fr.neamar.lolgamedata.tips.builder;

import android.content.Context;

import java.util.ArrayList;

import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.pojo.Game;
import fr.neamar.lolgamedata.pojo.Player;
import fr.neamar.lolgamedata.pojo.Team;
import fr.neamar.lolgamedata.tips.PlayerStandardTip;
import fr.neamar.lolgamedata.tips.Tip;

/**
 * Created by neamar on 06/07/16.
 */
public class HardcoreGamerTipBuilder extends TipBuilder {
    @Override
    public ArrayList<Tip> getTips(Game game, Context context) {
        ArrayList<Tip> tips = new ArrayList<>();

        for (Team team : game.teams) {
            for (Player player : team.players) {
                // 9000 is roughly 24*3600 / 10
                if (player.averageTimeBetweenGames < 9000) {
                    String descriptionTemplate = context.getString(R.string.hardcore_gamer_description);
                    String description = String.format(descriptionTemplate, player.summoner.name);
                    tips.add(new PlayerStandardTip(game, player, player.champion.imageUrl, context.getString(R.string.hardcore_gamer), description));
                }
            }
        }

        return tips;
    }
}
