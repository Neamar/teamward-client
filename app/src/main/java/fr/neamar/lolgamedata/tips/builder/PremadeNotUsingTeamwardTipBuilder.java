package fr.neamar.lolgamedata.tips.builder;

import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.pojo.Account;
import fr.neamar.lolgamedata.pojo.Game;
import fr.neamar.lolgamedata.pojo.Player;
import fr.neamar.lolgamedata.pojo.Team;
import fr.neamar.lolgamedata.tips.PlayerStandardTip;
import fr.neamar.lolgamedata.tips.Tip;

public class PremadeNotUsingTeamwardTipBuilder extends TipBuilder {
    @Override
    public ArrayList<Tip> getTips(Game game, Context context) {
        ArrayList<Tip> tips = new ArrayList<>();

        Account mainAccount = game.associatedAccount;
        Player mainPlayer = game.getPlayerByAccount(mainAccount);
        if (mainPlayer == null) {
            return tips;
        }

        List<Integer> premade = findPlayerPremade(game, mainPlayer);
        if (premade == null) {
            return tips;
        }

        List<String> playersNotUsingTeamward = new ArrayList<>();
        for (Integer summonerId : premade) {
            Player player = findPlayerById(game, summonerId);
            if (player != null && !player.teamwardUser) {
                playersNotUsingTeamward.add(player.summoner.name);
            }
        }

        if (!playersNotUsingTeamward.isEmpty()) {
            String descriptionTemplate;
            String description;
            if (playersNotUsingTeamward.size() == 1) {
                descriptionTemplate = context.getString(R.string.s_is_not_using_teamward);
                description = String.format(descriptionTemplate, playersNotUsingTeamward.get(0));
            } else {
                descriptionTemplate = context.getString(R.string.s_are_not_using_teamward);
                String lastPlayer = playersNotUsingTeamward.get(playersNotUsingTeamward.size() - 1);
                playersNotUsingTeamward.remove(playersNotUsingTeamward.size() - 1);
                String players = TextUtils.join(context.getString(R.string.standard_separator) + " ", playersNotUsingTeamward);
                players += " " + context.getString(R.string.final_separator) + " " + lastPlayer;
                description = String.format(descriptionTemplate, players);
            }

            tips.add(new PlayerStandardTip(game, null, R.mipmap.ic_launcher, context.getString(R.string.not_using_teamward), description));
        }

        return tips;
    }

    private List<Integer> findPlayerPremade(Game game, Player player) {
        for (Team team : game.teams) {
            for (List<Integer> premade : team.premades) {
                for (Integer summonerId : premade) {
                    if (player.summoner.id == summonerId) {
                        return premade;
                    }
                }
            }
        }

        return null;
    }

    private Player findPlayerById(Game game, int summonerId) {
        for (Team team : game.teams) {
            for (Player player : team.players) {
                if (player.summoner.id == summonerId) {
                    return player;
                }
            }
        }
        return null;
    }

}