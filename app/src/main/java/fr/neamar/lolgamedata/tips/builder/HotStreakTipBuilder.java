package fr.neamar.lolgamedata.tips.builder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.holder.TipHolder;
import fr.neamar.lolgamedata.pojo.Game;
import fr.neamar.lolgamedata.pojo.Player;
import fr.neamar.lolgamedata.pojo.Team;
import fr.neamar.lolgamedata.tips.HotStreakTip;
import fr.neamar.lolgamedata.tips.Tip;
import fr.neamar.lolgamedata.tips.holder.HotStreakTipHolder;

/**
 * Created by neamar on 04/07/16.
 */
public class HotStreakTipBuilder extends TipBuilder {
    public ArrayList<Tip> getTips(Game game) {
        ArrayList<Tip> tips = new ArrayList<>();

        for(Team team: game.teams) {
            for(Player player: team.players) {
                if(player.totalRecentGames > 5 && player.winRecentGames >= player.totalRecentGames - 1) {
                    tips.add(new HotStreakTip(game, player));
                }
            }
        }

        return tips;
    }

    public static TipHolder onCreateViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.item_tip_hotstreak, parent, false);

        return new HotStreakTipHolder(view);
    }
}
