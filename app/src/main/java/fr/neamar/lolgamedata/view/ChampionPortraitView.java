package fr.neamar.lolgamedata.view;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.holder.PlayerHolder;
import fr.neamar.lolgamedata.pojo.Champion;


public class ChampionPortraitView extends RelativeLayout {
    @StringRes
    private static final int[] mainChampionResources = new int[]{
            0,
            R.string.first_main,
            R.string.second_main,
            R.string.third_main
    };

    private ImageView championImage;
    private ImageView championMastery;
    private TextView mainChampionText;
    private AdApView adApView;

    public ChampionPortraitView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ChampionPortraitView(Context context) {
        this(context, null);
        init(context);
    }

    public ChampionPortraitView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init(context);
    }

    public void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.champion_portrait, this, true);
        championImage = (ImageView) findViewById(R.id.championImage);
        championMastery = (ImageView) findViewById(R.id.championMasteryImage);
        mainChampionText = (TextView) findViewById(R.id.mainChampion);
        adApView = (AdApView) findViewById(R.id.apAd);
    }

    public void setChampion(Champion champion) {
        ImageLoader.getInstance().displayImage(champion.imageUrl, championImage);
        championImage.setContentDescription(champion.name);

        @DrawableRes
        int championMasteryResource = PlayerHolder.CHAMPION_MASTERIES_RESOURCES[champion.mastery];

        if (championMasteryResource == 0) {
            championMastery.setVisibility(View.GONE);
        } else {
            championMastery.setVisibility(View.VISIBLE);
            championMastery.setImageResource(championMasteryResource);
            String chammpionMasteryTemplate = championMastery.getContext().getString(R.string.champion_mastery_level);
            championMastery.setContentDescription(String.format(chammpionMasteryTemplate, champion.mastery));
        }

        if(champion.points > 200000) {
            mainChampionText.setVisibility(View.VISIBLE);
            mainChampionText.setText(String.format(mainChampionText.getContext().getString(R.string.champion_points_k), Integer.toString(champion.points / 100000)));
        }
        else {
            // Less than 100k points, but can still be his main

            // To qualify as a main champion, has to be:
            // * at least champion mastery level 3
            // * be in the top 3 champions played on this account
            // * championRank should not be -1
            if (champion.mastery >= 3 && champion.championRank <= 3 && champion.championRank >= 1) {
                // Main champion!
                mainChampionText.setVisibility(View.VISIBLE);
                String mainText = mainChampionText.getContext().getString(mainChampionResources[champion.championRank]);
                mainChampionText.setText(Html.fromHtml(mainText));
            } else {
                mainChampionText.setVisibility(View.GONE);
            }
        }

        adApView.setAd(champion.ad);
        adApView.setAp(champion.ap);
    }
}
