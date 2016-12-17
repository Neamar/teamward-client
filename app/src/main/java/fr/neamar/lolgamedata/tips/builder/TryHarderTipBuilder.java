package fr.neamar.lolgamedata.tips.builder;

import android.content.Context;

import java.util.ArrayList;

import fr.neamar.lolgamedata.R;
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
            boolean areTryHarder = true;
            for (Player player : team.players) {
                if (player.champion.mastery < 4) {
                    areTryHarder = false;
                    break;
                }
            }

            if (areTryHarder) {
                String description = context.getString(R.string.try_harder_desc);
                description = String.format(description, team.getName(context));
                tips.add(new PlayerStandardTip(game, null, R.drawable.champion_mastery_5, context.getString(R.string.try_harder), description));

            }
        }

        return tips;
    }
}
