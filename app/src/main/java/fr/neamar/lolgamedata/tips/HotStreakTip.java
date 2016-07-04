package fr.neamar.lolgamedata.tips;

import fr.neamar.lolgamedata.pojo.Game;
import fr.neamar.lolgamedata.pojo.Player;

/**
 * Created by neamar on 04/07/16.
 */
public class HotStreakTip extends Tip {
    public Player player;
    public HotStreakTip(Game game, Player player) {
        super(game);
        this.player = player;
    }
}
