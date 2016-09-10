package fr.neamar.lolgamedata.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.pojo.ChampionCounter;
import fr.neamar.lolgamedata.pojo.Counter;

public class CounterCountersHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final ImageView championImage;
    private final ImageView counterImage;
    private final TextView winRateText;
    private final ImageView masteryImage;

    public CounterCountersHolder(View view) {
        super(view);

        championImage = (ImageView) view.findViewById(R.id.champion);
        counterImage = (ImageView) view.findViewById(R.id.counter);
        winRateText = (TextView) view.findViewById(R.id.matchupStats);
        masteryImage = (ImageView) view.findViewById(R.id.counterMastery);

        view.setOnClickListener(this);
    }

    public void bindAdvert(ChampionCounter championCounter, Counter counter) {
        ImageLoader.getInstance().displayImage(counter.champion.image, championImage);
        championImage.setContentDescription(counter.champion.name);

            ImageLoader.getInstance().displayImage(championCounter.image, counterImage);
            counterImage.setContentDescription(championCounter.name);

            int winRate = championCounter.winRate;
            winRateText.setText(String.format("%d%%", winRate));
            if (winRate < 50) {
                winRateText.setTextColor(winRateText.getResources().getColor(R.color.colorBadMatchup));
            } else {
                winRateText.setTextColor(winRateText.getResources().getColor(R.color.colorGoodMatchup));
            }

            masteryImage.setImageResource(PlayerHolder.CHAMPION_MASTERIES_RESOURCES[championCounter.mastery]);
//        } else {
//            counterImage.setImageResource(R.drawable.default_champion);
//            winRateText.setText("?");
//            winRateText.setTextColor(winRateText.getResources().getColor(R.color.colorUnknownMatchup));
//            masteryImage.setImageResource(0);
//        }
    }

    @Override
    public void onClick(View v) {
    }
}