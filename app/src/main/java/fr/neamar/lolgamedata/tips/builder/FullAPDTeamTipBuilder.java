package fr.neamar.lolgamedata.tips.builder;

import android.content.Context;

import java.util.ArrayList;

import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.pojo.Game;
import fr.neamar.lolgamedata.pojo.Player;
import fr.neamar.lolgamedata.pojo.Team;
import fr.neamar.lolgamedata.tips.PlayerStandardTip;
import fr.neamar.lolgamedata.tips.Tip;

public class FullAPDTeamTipBuilder extends TipBuilder {
    private static final String MAGIC_MANTLE_URL = "https://ddragon.leagueoflegends.com/cdn/9.1.1/img/item/1033.png";
    private static final String CLOTH_ARMOR = "https://ddragon.leagueoflegends.com/cdn/9.1.1/img/item/1029.png";

    @Override
    public ArrayList<Tip> getTips(Game game, Context context) {
        ArrayList<Tip> tips = new ArrayList<>();

        // Full AP
        for (Team team : game.teams) {
            Boolean fullAP = true;
            for (Player player : team.players) {
                if (!player.champion.isAp()) {
                    fullAP = false;
                    break;
                }
            }

            if (fullAP) {
                String description = context.getString(R.string.full_ap);
                description = String.format(description, team.getName(context));
                tips.add(new PlayerStandardTip(game, null, MAGIC_MANTLE_URL, context.getString(R.string.such_ability), description));
            }
        }

        // Full AD
        for (Team team : game.teams) {
            Boolean fullAD = true;
            for (Player player : team.players) {
                if (!player.champion.isAd() && !player.champion.role.equals("SUPPORT")) {
                    fullAD = false;
                    break;
                }
            }

            if (fullAD) {
                String description = context.getString(R.string.full_ad);
                description = String.format(description, team.getName(context));
                tips.add(new PlayerStandardTip(game, null, CLOTH_ARMOR, context.getString(R.string.much_damage), description));
            }
        }

        return tips;
    }
}
