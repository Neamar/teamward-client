package fr.neamar.lolgamedata.tips;

import android.support.annotation.Nullable;

import fr.neamar.lolgamedata.pojo.Game;
import fr.neamar.lolgamedata.pojo.Player;

/**
 * Created by neamar on 06/07/16.
 */
public class PlayerStandardTip extends Tip {
    @Nullable
    public Player player;
    public String image;
    public String text;
    public String description;
    public PlayerStandardTip(Game game, Player player, String image, String text, String description) {
        super(game);
        this.player = player;
        this.image = image;
        this.text = text;
        this.description = description;
    }
}
