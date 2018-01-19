package fr.neamar.lolgamedata.holder;

import android.content.Intent;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import fr.neamar.lolgamedata.PerformanceActivity;
import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.pojo.Game;
import fr.neamar.lolgamedata.pojo.Player;
import fr.neamar.lolgamedata.view.AdApView;
import fr.neamar.lolgamedata.view.ChampionView;

public class PlayerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @DrawableRes
    public static final int[] CHAMPION_MASTERIES_RESOURCES = new int[]{
            0,
            R.drawable.champion_mastery_1,
            R.drawable.champion_mastery_2,
            R.drawable.champion_mastery_3,
            R.drawable.champion_mastery_4,
            R.drawable.champion_mastery_5,
            R.drawable.champion_mastery_6,
            R.drawable.champion_mastery_7,
    };
    public static final Map<String, Integer> RANKING_TIER_RESOURCES;
    @StringRes
    private static final int[] mainChampionResources = new int[]{
            0,
            R.string.first_main,
            R.string.second_main,
            R.string.third_main
    };

    static {
        Map<String, Integer> map = new HashMap<>();
        map.put("bronze", R.drawable.tier_bronze);
        map.put("silver", R.drawable.tier_silver);
        map.put("gold", R.drawable.tier_gold);
        map.put("platinum", R.drawable.tier_platinum);
        map.put("diamond", R.drawable.tier_diamond);
        map.put("master", R.drawable.tier_master);
        map.put("challenger", R.drawable.tier_challenger);

        RANKING_TIER_RESOURCES = Collections.unmodifiableMap(map);
    }

    private final ChampionView championView;
    private final ImageView championMastery;
    private final TextView championName;
    private final TextView summonerName;
    private final TextView summonerLevel;
    private final TextView rankingDivision;
    private final ImageView rankingTier;
    private final ImageView spellDImage;
    private final ImageView spellFImage;
    private final TextView mainChampionText;

    private Game game;
    private Player player;

    public PlayerHolder(View view) {
        super(view);

        championView = (ChampionView) view.findViewById(R.id.championView);
        championMastery = (ImageView) view.findViewById(R.id.championMasteryImage);
        championName = (TextView) view.findViewById(R.id.championNameText);
        summonerName = (TextView) view.findViewById(R.id.summonerNameText);
        summonerLevel = (TextView) view.findViewById(R.id.summonerLevelText);
        rankingDivision = (TextView) view.findViewById(R.id.rankingDivisionText);
        rankingTier = (ImageView) view.findViewById(R.id.rankingTierImage);
        spellDImage = (ImageView) view.findViewById(R.id.spellDImage);
        spellFImage = (ImageView) view.findViewById(R.id.spellFImage);
        mainChampionText = (TextView) view.findViewById(R.id.mainChampion);

        view.setOnClickListener(this);
    }

    public void bind(Player player, Game game) {
        this.player = player;
        this.game = game;

        if (PreferenceManager.getDefaultSharedPreferences(championName.getContext()).getBoolean("display_champion_name", true)) {
            this.championName.setText(player.champion.name);
            this.summonerName.setText(player.summoner.name);
        } else {
            this.championName.setText(player.summoner.name);
            this.summonerName.setText("");
        }

        championView.setChampion(player.champion);
        championView.setBorderMode(ChampionView.BorderMode.AP_AD);
        championView.setContentDescription(player.champion.name);
        ImageLoader.getInstance().displayImage(player.spellD.imageUrl, spellDImage);
        spellDImage.setContentDescription(player.spellD.name);
        ImageLoader.getInstance().displayImage(player.spellF.imageUrl, spellFImage);
        spellFImage.setContentDescription(player.spellF.name);

        @DrawableRes
        int championMasteryResource = CHAMPION_MASTERIES_RESOURCES[player.champion.mastery];

        if (championMasteryResource == 0) {
            championMastery.setVisibility(View.GONE);
        } else {
            championMastery.setVisibility(View.VISIBLE);
            championMastery.setImageResource(championMasteryResource);
            String chammpionMasteryTemplate = championMastery.getContext().getString(R.string.champion_mastery_level);
            championMastery.setContentDescription(String.format(chammpionMasteryTemplate, player.champion.mastery));
        }

        String summonerLevelTemplate = summonerLevel.getContext().getString(R.string.summoner_level);
        summonerLevel.setText(summonerLevelTemplate.replace("%s", Integer.toString(player.summoner.level)));
        summonerLevel.setTypeface(null, player.summoner.level < 30 ? Typeface.BOLD : Typeface.NORMAL);
        summonerLevel.setVisibility(View.VISIBLE);

        // Are you playing ranked this season?
        if (player.rank.tier.isEmpty() || !RANKING_TIER_RESOURCES.containsKey(player.rank.tier.toLowerCase())) {
            // Have you ever played rank?
            if (player.rank.oldTier.isEmpty() || !RANKING_TIER_RESOURCES.containsKey(player.rank.oldTier.toLowerCase())) {
                // Never played rank: display summoner level,
                // Bold level < 30
                rankingDivision.setVisibility(View.INVISIBLE);
                rankingTier.setVisibility(View.INVISIBLE);


            } else {
                // Played ranked last season
                rankingDivision.setVisibility(View.GONE);
                rankingTier.setVisibility(View.VISIBLE);
                rankingTier.setImageResource(RANKING_TIER_RESOURCES.get(player.rank.oldTier.toLowerCase()));
                rankingTier.setContentDescription(player.rank.oldTier);
                if (!PreferenceManager.getDefaultSharedPreferences(championName.getContext()).getBoolean("always_display_level", false)) {
                    summonerLevel.setVisibility(View.GONE);
                }
            }
        } else {
            // Play ranked this seaon
            rankingDivision.setVisibility(View.VISIBLE);
            rankingDivision.setText(player.rank.division);
            rankingTier.setVisibility(View.VISIBLE);
            rankingTier.setImageResource(RANKING_TIER_RESOURCES.get(player.rank.tier.toLowerCase()));
            rankingTier.setContentDescription(player.rank.tier);
            if (!PreferenceManager.getDefaultSharedPreferences(championName.getContext()).getBoolean("always_display_level", false)) {
                summonerLevel.setVisibility(View.GONE);
            }
        }

        if(player.champion.points > 200000) {
            mainChampionText.setVisibility(View.VISIBLE);
            mainChampionText.setText(String.format(mainChampionText.getContext().getString(R.string.champion_points_k), Integer.toString(player.champion.points / 100000)));
        }
        else {
            // Less than 100k points, but can still be his main

            // To qualify as a main champion, has to be:
            // * at least champion mastery level 3
            // * be in the top 3 champions played on this account
            // * championRank should not be -1
            if (player.champion.mastery >= 3 && player.champion.championRank <= 3 && player.champion.championRank >= 1) {
                // Main champion!
                mainChampionText.setVisibility(View.VISIBLE);
                String mainText = mainChampionText.getContext().getString(mainChampionResources[player.champion.championRank]);
                mainChampionText.setText(Html.fromHtml(mainText));
            } else {
                mainChampionText.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (player == null) {
            return;
        }

        Intent i = new Intent(v.getContext(), PerformanceActivity.class);
        i.putExtra("player", player);
        i.putExtra("game", game);
        v.getContext().startActivity(i);
    }
}
