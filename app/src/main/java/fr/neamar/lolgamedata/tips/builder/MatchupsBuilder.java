package fr.neamar.lolgamedata.tips.builder;

import android.content.Context;
import android.support.annotation.Nullable;

import java.util.ArrayList;

import fr.neamar.lolgamedata.pojo.Champion;
import fr.neamar.lolgamedata.pojo.Game;
import fr.neamar.lolgamedata.pojo.Player;
import fr.neamar.lolgamedata.pojo.Team;
import fr.neamar.lolgamedata.tips.MatchupsTip;
import fr.neamar.lolgamedata.tips.Tip;

public class MatchupsBuilder extends TipBuilder {
    @Override
    public ArrayList<Tip> getTips(Game game, Context context) {
        ArrayList<Tip> tips = new ArrayList<>();

        if (game.teams.size() != 2) {
            return tips;
        }

        Team playerTeam = game.getPlayerOwnTeam();
        Team otherTeam = game.teams.get(0) == playerTeam ? game.teams.get(1) : game.teams.get(0);

        ArrayList<MatchupsTip.Matchup> matchups = new ArrayList<>();

        for (Player player : playerTeam.players) {
            if (!player.champion.role.equals(Champion.UNKNOWN_ROLE)) {
                // Does the other team have someone with this role too?
                Player otherPlayer = getPlayerWithRole(otherTeam, player.champion.role);
                if (otherPlayer != null) {
                    MatchupsTip.Matchup matchup = new MatchupsTip.Matchup(player, otherPlayer);
                    matchups.add(matchup);
                }
            }
        }

        if (matchups.size() > 0) {
            MatchupsTip tip = new MatchupsTip(game, matchups);
            tips.add(tip);
        }

        return tips;
    }

    @Nullable
    private Player getPlayerWithRole(Team team, String role) {
        for (Player player : team.players) {
            if (player.champion.role.equals(role)) {
                return player;
            }
        }

        return null;
    }
}
