package fr.neamar.lolgamedata.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.pojo.Player;

/**
 * Created by neamar on 25/03/16.
 */
public class PlayerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static final String TAG = "AdvertHolder";
    private Player player;

    private final ImageView championImage;
    private final ImageView championMastery;
    private final TextView championName;
    private final TextView summonerName;
    private final TextView previousRanking;
    private final TextView rankingDivision;
    private final ImageView rankingTier;

    public PlayerHolder(View view) {
        super(view);

        championImage = (ImageView) view.findViewById(R.id.championImage);
        championMastery = (ImageView) view.findViewById(R.id.championMasteryImage);
        championName = (TextView) view.findViewById(R.id.championNameText);
        summonerName = (TextView) view.findViewById(R.id.summonerNameText);
        previousRanking = (TextView) view.findViewById(R.id.previousRankingText);
        rankingDivision = (TextView) view.findViewById(R.id.rankingDivisionText);
        rankingTier = (ImageView) view.findViewById(R.id.rankingTierImage);

        view.setOnClickListener(this);
    }

    public void bindAdvert(Player player) {
        this.player = player;

        this.championName.setText(player.champion.name);
        this.summonerName.setText(player.summoner.name);
    }

    @Override
    public void onClick(View v) {
        if (player == null) {
            return;
        }

        // TODO
    }
}
