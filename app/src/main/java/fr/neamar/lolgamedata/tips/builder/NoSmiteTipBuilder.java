package fr.neamar.lolgamedata.tips.builder;

import android.content.Context;

import java.util.ArrayList;

import fr.neamar.lolgamedata.GameActivity;
import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.pojo.Game;
import fr.neamar.lolgamedata.pojo.Player;
import fr.neamar.lolgamedata.pojo.Team;
import fr.neamar.lolgamedata.tips.PlayerStandardTip;
import fr.neamar.lolgamedata.tips.Tip;

public class NoSmiteTipBuilder extends TipBuilder {
    private static final String SMITE_URL = "https://ddragon.leagueoflegends.com/cdn/9.1.1/img/spell/SummonerSmite.png";

    @Override
    public ArrayList<Tip> getTips(Game game, Context context) {
        ArrayList<Tip> tips = new ArrayList<>();

        // Only apply on Summoner's Rift
        if (GameActivity.getMapName(game.mapId) != R.string.summoners_rift) {
            return tips;
        }

        for (Team team : game.teams) {
            Boolean teamHasSmite = false;
            for (Player player : team.players) {
                if (player.spellD.name.equals("Smite") || player.spellF.name.equals("Smite")) {
                    teamHasSmite = true;
                    break;
                }
            }

            if (!teamHasSmite) {
                String description = context.getString(R.string.no_smite_desc);
                description = String.format(description, team.getName(context));
                tips.add(new PlayerStandardTip(game, null, SMITE_URL, context.getString(R.string.no_smite), description));

            }
        }

        return tips;
    }
}
