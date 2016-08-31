package fr.neamar.lolgamedata.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.pojo.Counter;

public class CounterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final ImageView championImage;
    private final ImageView counterImage;
    private final TextView winRateText;

    private Counter counter;

    public CounterHolder(View view) {
        super(view);

        championImage = (ImageView) view.findViewById(R.id.champion);
        counterImage = (ImageView) view.findViewById(R.id.counter);
        winRateText = (TextView) view.findViewById(R.id.matchupStats);

        view.setOnClickListener(this);
    }

    public void bindAdvert(Counter counter) {
        this.counter = counter;

        ImageLoader.getInstance().displayImage(counter.championImage, championImage);
        championImage.setContentDescription(counter.championName);

        if (counter.counterImage != null) {
            ImageLoader.getInstance().displayImage(counter.counterImage, counterImage);
            counterImage.setContentDescription(counter.counterName);
        }

        winRateText.setText(String.format("%d%%", counter.winRate));
    }

    @Override
    public void onClick(View v) {
    }
}