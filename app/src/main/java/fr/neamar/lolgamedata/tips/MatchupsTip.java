package fr.neamar.lolgamedata.tips;

import java.util.ArrayList;

import fr.neamar.lolgamedata.pojo.Game;
import fr.neamar.lolgamedata.pojo.Player;

/**
 * Created by neamar on 14/07/16.
 */
public class MatchupsTip extends Tip {
    public final ArrayList<Matchup> matchups;

    public MatchupsTip(Game game, ArrayList<Matchup> matchups) {
        super(game);
        this.matchups = matchups;
    }

    public static class Matchup {
        public final Player ownPlayer;
        public final Player ennemyPlayer;

        public Matchup(Player ownPlayer, Player ennemyPlayer) {
            this.ownPlayer = ownPlayer;
            this.ennemyPlayer = ennemyPlayer;
        }
    }
}
