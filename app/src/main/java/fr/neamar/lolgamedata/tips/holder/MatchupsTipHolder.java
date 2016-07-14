package fr.neamar.lolgamedata.tips.holder;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.ImageLoader;

import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.holder.TipHolder;
import fr.neamar.lolgamedata.pojo.Game;
import fr.neamar.lolgamedata.tips.MatchupsTip;
import fr.neamar.lolgamedata.tips.Tip;

/**
 * Created by neamar on 14/07/16.
 */
public class MatchupsTipHolder extends TipHolder {
    public LinearLayout matchupsLayout;

    public MatchupsTipHolder(View itemView) {
        super(itemView);

        matchupsLayout = (LinearLayout) itemView.findViewById(R.id.matchups);
    }


    public void bindTip(Tip tip) {
        MatchupsTip matchupsTip = (MatchupsTip) tip;
        Game game = matchupsTip.game;

        Log.e("WTF", "SIZE:" + matchupsTip.matchups.size());

        matchupsLayout.removeAllViews();

        LayoutInflater inflater = LayoutInflater.from(matchupsLayout.getContext());
        for(MatchupsTip.Matchup matchup: matchupsTip.matchups) {
            View view = inflater.inflate(R.layout.item_tip_matchups_matchup, null, false);

            ImageView ownChampionImageView = (ImageView) view.findViewById(R.id.ownChampion);
            ImageLoader.getInstance().displayImage(matchup.ownPlayer.champion.imageUrl, ownChampionImageView);
            ownChampionImageView.setContentDescription(matchup.ownPlayer.champion.name);

            ImageView ennemyChampionImageView = (ImageView) view.findViewById(R.id.ennemyChampion);
            ImageLoader.getInstance().displayImage(matchup.ennemyPlayer.champion.imageUrl, ennemyChampionImageView);
            ennemyChampionImageView.setContentDescription(matchup.ennemyPlayer.champion.name);

            Log.e("WTF", "Adding new view");
            matchupsLayout.addView(view);
        }
    }


    public static TipHolder onCreateViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.item_tip_matchups, parent, false);

        return new MatchupsTipHolder(view);
    }
}
