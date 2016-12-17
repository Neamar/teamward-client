package fr.neamar.lolgamedata.tips.builder;

import android.content.Context;

import java.util.ArrayList;

import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.pojo.Game;
import fr.neamar.lolgamedata.pojo.Player;
import fr.neamar.lolgamedata.pojo.Team;
import fr.neamar.lolgamedata.tips.PlayerStandardTip;
import fr.neamar.lolgamedata.tips.Tip;

public class FilthyCasualTipBuilder extends TipBuilder {
    @Override
    public ArrayList<Tip> getTips(Game game, Context context) {
        ArrayList<Tip> tips = new ArrayList<>();

        for (Team team : game.teams) {
            for (Player player : team.players) {
                if (player.averageTimeBetweenGames > 3600 * 24 * 7) {
                    String descriptionTemplate = context.getString(R.string.filthy_casual_description);
                    String description = String.format(descriptionTemplate, player.summoner.name);
                    tips.add(new PlayerStandardTip(game, player, player.champion.imageUrl, context.getString(R.string.filthy_casual), description));
                }
            }
        }

        return tips;
    }
}
