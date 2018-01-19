package fr.neamar.lolgamedata.tips;

import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;

import fr.neamar.lolgamedata.pojo.Champion;
import fr.neamar.lolgamedata.pojo.Game;
import fr.neamar.lolgamedata.pojo.Player;

public class ChampionStandardTip extends Tip {
    @Nullable
    public final Player player;
    public final String text;
    public final String description;
    public Champion champion;
    @DrawableRes
    public int imageId = 0;

    public ChampionStandardTip(Game game, @Nullable Player player, Champion champion, String text, String description) {
        super(game);
        this.player = player;
        this.text = text;
        this.champion = champion;
        this.description = description;
    }
}
