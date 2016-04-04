package fr.neamar.lolgamedata.holder;

import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.pojo.Player;

/**
 * Created by neamar on 25/03/16.
 */
public class PlayerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private static final int[] championMasteriesResources = new int[]{
            0,
            R.drawable.champion_mastery_1,
            R.drawable.champion_mastery_2,
            R.drawable.champion_mastery_3,
            R.drawable.champion_mastery_4,
            R.drawable.champion_mastery_5,
            R.drawable.champion_mastery_6,
            R.drawable.champion_mastery_7,
    };

    private static final Map<String, Integer> rankingTierResources;

    static {
        Map<String, Integer> map = new HashMap<>();
        map.put("bronze", R.drawable.tier_bronze);
        map.put("silver", R.drawable.tier_silver);
        map.put("gold", R.drawable.tier_gold);
        map.put("platinum", R.drawable.tier_platinum);
        map.put("diamond", R.drawable.tier_diamond);
        map.put("master", R.drawable.tier_master);
        map.put("challenger", R.drawable.tier_challenger);

        rankingTierResources = Collections.unmodifiableMap(map);
    }

    private Player player;

    private final ImageView championImage;
    private final ImageView championMastery;
    private final TextView championName;
    private final TextView summonerName;
    private final TextView previousRanking;
    private final TextView rankingDivision;
    private final ImageView rankingTier;
    private final ImageView spellDImage;
    private final ImageView spellFImage;

    public PlayerHolder(View view) {
        super(view);

        championImage = (ImageView) view.findViewById(R.id.championImage);
        championMastery = (ImageView) view.findViewById(R.id.championMasteryImage);
        championName = (TextView) view.findViewById(R.id.championNameText);
        summonerName = (TextView) view.findViewById(R.id.summonerNameText);
        previousRanking = (TextView) view.findViewById(R.id.previousRankingText);
        rankingDivision = (TextView) view.findViewById(R.id.rankingDivisionText);
        rankingTier = (ImageView) view.findViewById(R.id.rankingTierImage);
        spellDImage = (ImageView) view.findViewById(R.id.spellDImage);
        spellFImage = (ImageView) view.findViewById(R.id.spellFImage);

        view.setOnClickListener(this);
    }

    public void bindAdvert(Player player) {
        this.player = player;

        this.championName.setText(player.champion.name);
        this.summonerName.setText(player.summoner.name);

        ImageLoader.getInstance().displayImage(player.champion.imageUrl, championImage);
        championImage.setContentDescription(player.champion.name);
        ImageLoader.getInstance().displayImage(player.spellD.imageUrl, spellDImage);
        spellDImage.setContentDescription(player.spellD.name);
        ImageLoader.getInstance().displayImage(player.spellF.imageUrl, spellFImage);
        spellFImage.setContentDescription(player.spellF.name);

        @DrawableRes
        int championMasteryResource = championMasteriesResources[player.champion.mastery];

        if (championMasteryResource == 0) {
            championMastery.setVisibility(View.GONE);
        } else {
            championMastery.setVisibility(View.VISIBLE);
            championMastery.setImageResource(championMasteryResource);
            String chammpionMasteryTemplate = championMastery.getContext().getString(R.string.champion_mastery_level);
            championMastery.setContentDescription(String.format(chammpionMasteryTemplate, player.champion.mastery));
        }


        Log.e("WTF", "TIER" + player.rank.tier);
        Log.e("WTF", "DIVISION" + player.rank.division);

        if (player.rank.tier.isEmpty() || !rankingTierResources.containsKey(player.rank.tier.toLowerCase())) {
            rankingDivision.setVisibility(View.INVISIBLE);
            rankingTier.setVisibility(View.INVISIBLE);

            previousRanking.setVisibility(View.VISIBLE);
            String summonerLevelTemplate = previousRanking.getContext().getString(R.string.summoner_level);
            previousRanking.setText(summonerLevelTemplate.replace("%s", Integer.toString(player.summoner.level)));
        } else {
            rankingDivision.setVisibility(View.VISIBLE);
            rankingDivision.setText(player.rank.division);
            rankingTier.setVisibility(View.VISIBLE);
            rankingTier.setImageResource(rankingTierResources.get(player.rank.tier.toLowerCase()));
            rankingTier.setContentDescription(player.rank.tier);
            previousRanking.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        if (player == null) {
            return;
        }

        // TODO
    }
}
