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
 * Created by neamar on 04/07/16.
 */
public class HotStreakTipBuilder extends TipBuilder {
    @Override
    public ArrayList<Tip> getTips(Game game, Context context) {
        ArrayList<Tip> tips = new ArrayList<>();

        for (Team team : game.teams) {
            for (Player player : team.players) {
                if (player.totalRecentGames > 5 && player.winRecentGames >= player.totalRecentGames - 1) {
                    String descriptionTemplate = context.getString(R.string.s_is_on_a_hot_streak_s_wins_in_s_last_games);
                    String description = String.format(descriptionTemplate, player.summoner.name, player.winRecentGames, player.totalRecentGames);
                    tips.add(new PlayerStandardTip(game, player, player.champion.imageUrl, context.getString(R.string.hot_streak), description));
                }
            }
        }

        return tips;
    }
}
