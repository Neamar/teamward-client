package fr.neamar.lolgamedata.holder;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import fr.neamar.lolgamedata.CounterCountersActivity;
import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.pojo.Counter;

public class CounterChampionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final ImageView championImage;

    private Counter counter;

    public CounterChampionHolder(View view) {
        super(view);

        championImage = (ImageView) view.findViewById(R.id.champion);

        view.setOnClickListener(this);
    }

    public void bindAdvert(Counter counter) {
        this.counter = counter;

        championImage.setImageResource(R.drawable.default_champion);
        ImageLoader.getInstance().displayImage(counter.champion.image, championImage);
        championImage.setContentDescription(counter.champion.name);
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(v.getContext(), CounterCountersActivity.class);
        i.putExtra("counter", counter);

        v.getContext().startActivity(i);
    }
}