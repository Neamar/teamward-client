package fr.neamar.lolgamedata.tips.builder;

import android.content.Context;

import java.util.ArrayList;

import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.holder.PlayerHolder;
import fr.neamar.lolgamedata.pojo.Game;
import fr.neamar.lolgamedata.pojo.Player;
import fr.neamar.lolgamedata.pojo.Team;
import fr.neamar.lolgamedata.tips.PlayerStandardTip;
import fr.neamar.lolgamedata.tips.Tip;

public class TryHarderTipBuilder extends TipBuilder {
    @Override
    public ArrayList<Tip> getTips(Game game, Context context) {
        ArrayList<Tip> tips = new ArrayList<>();


        for (Team team : game.teams) {
            int minMastery = 7;
            for (Player player : team.players) {
                minMastery = Math.min(player.champion.mastery, minMastery);
            }

            if (minMastery >= 4) {
                String description = context.getString(R.string.try_harder_desc);
                description = String.format(description, team.getName(context), minMastery);
                tips.add(new PlayerStandardTip(game, null, PlayerHolder.CHAMPION_MASTERIES_RESOURCES[minMastery], context.getString(R.string.try_harder), description));

            }
        }

        return tips;
    }
}
