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
public class ColdStreakTipBuilder extends TipBuilder {
    @Override
    public ArrayList<Tip> getTips(Game game, Context context) {
        ArrayList<Tip> tips = new ArrayList<>();

        for (Team team : game.teams) {
            for (Player player : team.players) {
                if (player.totalRecentGames > 5 && player.lossRecentGames >= player.totalRecentGames - 2) {
                    String descriptionTemplate = context.getString(R.string.s_is_on_a_cold_streak_s_loss_in_s_last_games);
                    String description = String.format(descriptionTemplate, player.summoner.name, player.lossRecentGames, player.totalRecentGames);
                    tips.add(new PlayerStandardTip(game, player, player.champion.imageUrl, context.getString(R.string.cold_streak), description));
                }
            }
        }

        return tips;
    }
}
