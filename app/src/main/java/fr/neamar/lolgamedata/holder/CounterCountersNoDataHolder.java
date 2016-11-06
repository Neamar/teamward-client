package fr.neamar.lolgamedata.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.pojo.ChampionCounter;


public class CounterCountersNoDataHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final ImageView championImage;
    private ChampionCounter championCounter;

    public CounterCountersNoDataHolder(View view) {
        super(view);

        championImage = (ImageView) view.findViewById(R.id.champion);

        view.setOnClickListener(this);
    }

    public void bind(ChampionCounter championCounter) {
        ImageLoader.getInstance().displayImage(championCounter.image, championImage);
        championImage.setContentDescription(championCounter.name);
        this.championCounter = championCounter;
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(view.getContext(), String.format(view.getContext().getString(R.string.not_enough_information), championCounter.name), Toast.LENGTH_LONG).show();
    }
}
