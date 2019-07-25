package fr.neamar.lolgamedata.holder;

import android.content.Intent;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import androidx.annotation.DrawableRes;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import fr.neamar.lolgamedata.PerformanceActivity;
import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.pojo.Game;
import fr.neamar.lolgamedata.pojo.Player;
import fr.neamar.lolgamedata.view.ChampionPortraitView;

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

    static {
        Map<String, Integer> map = new HashMap<>();
        map.put("iron", R.drawable.tier_iron);
        map.put("bronze", R.drawable.tier_bronze);
        map.put("silver", R.drawable.tier_silver);
        map.put("gold", R.drawable.tier_gold);
        map.put("platinum", R.drawable.tier_platinum);
        map.put("diamond", R.drawable.tier_diamond);
        map.put("master", R.drawable.tier_master);
        map.put("grandmaster", R.drawable.tier_grandmaster);
        map.put("challenger", R.drawable.tier_challenger);

        RANKING_TIER_RESOURCES = Collections.unmodifiableMap(map);
    }

    private final TextView championName;
    private final TextView summonerName;
    private final TextView summonerLevel;
    private final TextView rankingDivision;
    private final ImageView rankingTier;
    private final ImageView spellDImage;
    private final ImageView spellFImage;
    private final ChampionPortraitView championPortrait;

    private Game game;
    private Player player;

    public PlayerHolder(View view) {
        super(view);

        championName = (TextView) view.findViewById(R.id.championNameText);
        summonerName = (TextView) view.findViewById(R.id.summonerNameText);
        summonerLevel = (TextView) view.findViewById(R.id.summonerLevelText);
        rankingDivision = (TextView) view.findViewById(R.id.rankingDivisionText);
        rankingTier = (ImageView) view.findViewById(R.id.rankedTierImage);
        spellDImage = (ImageView) view.findViewById(R.id.spellDImage);
        spellFImage = (ImageView) view.findViewById(R.id.spellFImage);
        championPortrait = (ChampionPortraitView) view.findViewById(R.id.championPortrait);

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

        ImageLoader.getInstance().displayImage(player.spellD.imageUrl, spellDImage);
        spellDImage.setContentDescription(player.spellD.name);
        ImageLoader.getInstance().displayImage(player.spellF.imageUrl, spellFImage);
        spellFImage.setContentDescription(player.spellF.name);

        String summonerLevelTemplate = summonerLevel.getContext().getString(R.string.summoner_level);
        summonerLevel.setText(summonerLevelTemplate.replace("%s", Integer.toString(player.summoner.level)));
        summonerLevel.setTypeface(null, player.summoner.level < 30 ? Typeface.BOLD : Typeface.NORMAL);
        summonerLevel.setVisibility(View.VISIBLE);

        // Are you playing ranked this season?
        if (player.mainRank.tier.isEmpty() || !RANKING_TIER_RESOURCES.containsKey(player.mainRank.tier.toLowerCase(Locale.ROOT))) {
            // Have you ever played rank?
            if (player.mainRank.oldTier.isEmpty() || !RANKING_TIER_RESOURCES.containsKey(player.mainRank.oldTier.toLowerCase(Locale.ROOT))) {
                // Never played rank: display summoner level,
                // Bold level < 30
                rankingDivision.setVisibility(View.INVISIBLE);
                rankingTier.setVisibility(View.INVISIBLE);


            } else {
                // Played ranked last season
                rankingDivision.setVisibility(View.GONE);
                rankingTier.setVisibility(View.VISIBLE);
                rankingTier.setImageResource(RANKING_TIER_RESOURCES.get(player.mainRank.oldTier.toLowerCase(Locale.ROOT)));
                rankingTier.setContentDescription(player.mainRank.oldTier);
                if (!PreferenceManager.getDefaultSharedPreferences(championName.getContext()).getBoolean("always_display_level", false)) {
                    summonerLevel.setVisibility(View.GONE);
                }
            }
        } else {
            // Play ranked this seaon
            rankingDivision.setVisibility(View.VISIBLE);
            rankingDivision.setText(player.mainRank.division);
            rankingTier.setVisibility(View.VISIBLE);
            rankingTier.setImageResource(RANKING_TIER_RESOURCES.get(player.mainRank.tier.toLowerCase(Locale.ROOT)));
            rankingTier.setContentDescription(player.mainRank.tier);
            if (!PreferenceManager.getDefaultSharedPreferences(championName.getContext()).getBoolean("always_display_level", false)) {
                summonerLevel.setVisibility(View.GONE);
            }
        }

        championPortrait.setChampion(player.champion);
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
