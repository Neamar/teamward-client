package fr.neamar.lolgamedata.tips.holder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.holder.TipHolder;
import fr.neamar.lolgamedata.tips.MatchupsTip;
import fr.neamar.lolgamedata.tips.Tip;

/**
 * Created by neamar on 14/07/16.
 */
public class MatchupsTipHolder extends TipHolder {
    public final LinearLayout matchupsLayout;

    public MatchupsTipHolder(View itemView) {
        super(itemView);

        matchupsLayout = (LinearLayout) itemView.findViewById(R.id.matchups);
    }

    public static TipHolder onCreateViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.item_tip_matchups, parent, false);

        return new MatchupsTipHolder(view);
    }

    public void bind(Tip tip) {
        MatchupsTip matchupsTip = (MatchupsTip) tip;

        matchupsLayout.removeAllViews();

        LayoutInflater inflater = LayoutInflater.from(matchupsLayout.getContext());
        for (MatchupsTip.Matchup matchup : matchupsTip.matchups) {
            View view = inflater.inflate(R.layout.item_tip_matchups_matchup, matchupsLayout, false);

            ImageView ownChampionImageView = (ImageView) view.findViewById(R.id.ownChampion);
            ImageLoader.getInstance().displayImage(matchup.ownPlayer.champion.imageUrl, ownChampionImageView);
            ownChampionImageView.setContentDescription(matchup.ownPlayer.champion.name);

            ImageView ennemyChampionImageView = (ImageView) view.findViewById(R.id.ennemyChampion);
            ImageLoader.getInstance().displayImage(matchup.ennemyPlayer.champion.imageUrl, ennemyChampionImageView);
            ennemyChampionImageView.setContentDescription(matchup.ennemyPlayer.champion.name);

            TextView matchupTextView = (TextView) view.findViewById(R.id.matchupStats);

            if (matchup.ownPlayer.champion.winRate >= 0) {
                matchupTextView.setText(String.format("%d%%", matchup.ownPlayer.champion.winRate));
                if (matchup.ownPlayer.champion.winRate > 50) {
                    matchupTextView.setTextColor(matchupsLayout.getContext().getResources().getColor(R.color.colorGoodMatchup));
                } else if (matchup.ownPlayer.champion.winRate < 50) {
                    matchupTextView.setTextColor(matchupsLayout.getContext().getResources().getColor(R.color.colorBadMatchup));
                }
            } else {
                matchupTextView.setText("?");
                matchupTextView.setTextColor(matchupsLayout.getContext().getResources().getColor(R.color.colorUnknownMatchup));
            }
            matchupsLayout.addView(view);
        }
    }
}
