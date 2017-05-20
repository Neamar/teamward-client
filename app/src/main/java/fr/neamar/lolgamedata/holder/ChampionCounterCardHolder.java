package fr.neamar.lolgamedata.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.pojo.ChampionCounter;

public class ChampionCounterCardHolder extends DummyHolder {
        private final ImageView championImage;
        private final TextView championNameText;
        private final TextView winrateText;
        private final TextView disclaimer;

        public ChampionCounterCardHolder(View itemView) {
            super(itemView);
            championImage = (ImageView) itemView.findViewById(R.id.championImage);
            championNameText = (TextView) itemView.findViewById(R.id.championNameText);
            winrateText = (TextView) itemView.findViewById(R.id.winrateText);
            disclaimer = (TextView) itemView.findViewById(R.id.disclaimer);
        }

        public void bindChampion(ChampionCounter champion) {
            ImageLoader.getInstance().displayImage(champion.image, championImage);
            championNameText.setText(champion.name);
            winrateText.setText(String.format(winrateText.getContext().getString(R.string.s_winrate), champion.winRate + "%"));
            disclaimer.setText(String.format(disclaimer.getContext().getString(R.string.patch_s), champion.patch));
        }
    }
