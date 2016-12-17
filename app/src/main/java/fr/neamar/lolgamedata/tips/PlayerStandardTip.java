package fr.neamar.lolgamedata.tips;

import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;

import fr.neamar.lolgamedata.pojo.Game;
import fr.neamar.lolgamedata.pojo.Player;

public class PlayerStandardTip extends Tip {
    @Nullable
    private final Player player;

    public final String text;
    public final String description;
    public String image = "";
    @DrawableRes
    public int imageId = 0;

    public PlayerStandardTip(Game game, @Nullable Player player, String image, String text, String description) {
        super(game);
        this.player = player;
        this.image = image;
        this.text = text;
        this.description = description;
    }

    public PlayerStandardTip(Game game, @Nullable Player player, int imageId, String text, String description) {
        super(game);
        this.player = player;
        this.imageId = imageId;
        this.text = text;
        this.description = description;
    }
}
