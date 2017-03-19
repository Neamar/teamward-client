package fr.neamar.lolgamedata.tips.holder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Locale;

import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.holder.TipHolder;
import fr.neamar.lolgamedata.tips.MatchupsTip;
import fr.neamar.lolgamedata.tips.Tip;

public class MatchupsTipHolder extends TipHolder {
    private final LinearLayout matchupsLayout;

    private MatchupsTipHolder(View itemView) {
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

            ImageView enemyChampionImageView = (ImageView) view.findViewById(R.id.enemyChampion);
            ImageLoader.getInstance().displayImage(matchup.enemyPlayer.champion.imageUrl, enemyChampionImageView);
            enemyChampionImageView.setContentDescription(matchup.enemyPlayer.champion.name);

            TextView matchupTextView = (TextView) view.findViewById(R.id.matchupStats);

            if (matchup.ownPlayer.champion.winRate >= 0) {
                matchupTextView.setText(String.format(Locale.getDefault(), "%d%%", matchup.ownPlayer.champion.winRate));
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
