package fr.neamar.lolgamedata.tips.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.holder.TipHolder;
import fr.neamar.lolgamedata.pojo.Player;
import fr.neamar.lolgamedata.tips.HotStreakTip;
import fr.neamar.lolgamedata.tips.Tip;

/**
 * Created by neamar on 04/07/16.
 */
public class HotStreakTipHolder extends TipHolder {
    public ImageView championImage;
    public TextView descriptionText;

    public HotStreakTipHolder(View itemView) {
        super(itemView);

        championImage = (ImageView) itemView.findViewById(R.id.championImage);
        descriptionText = (TextView) itemView.findViewById(R.id.hotStreakDescription);
    }

    public void bindTip(Tip tip) {
        HotStreakTip hotStreakTip = (HotStreakTip) tip;
        Player player = hotStreakTip.player;

        String descriptionTemplate = descriptionText.getContext().getString(R.string.s_is_on_a_hot_streak_s_wins_in_s_last_games);
        descriptionText.setText(String.format(descriptionTemplate, player.summoner.name, player.winRecentGames, player.totalRecentGames));

        ImageLoader.getInstance().displayImage(player.champion.imageUrl, championImage);
    }
}
