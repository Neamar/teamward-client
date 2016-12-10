package fr.neamar.lolgamedata.tips.holder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.holder.TipHolder;
import fr.neamar.lolgamedata.tips.PlayerStandardTip;
import fr.neamar.lolgamedata.tips.Tip;

/**
 * Created by neamar on 06/07/16.
 */
public class PlayerStandardTipHolder extends TipHolder {
    public final ImageView championImage;
    public final TextView descriptionText;
    public final TextView titleText;

    public PlayerStandardTipHolder(View itemView) {
        super(itemView);

        championImage = (ImageView) itemView.findViewById(R.id.championImage);
        titleText = (TextView) itemView.findViewById(R.id.title);
        descriptionText = (TextView) itemView.findViewById(R.id.hotStreakDescription);
    }

    public static TipHolder onCreateViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.item_tip_text_description_image, parent, false);

        return new PlayerStandardTipHolder(view);
    }

    public void bind(Tip tip) {
        PlayerStandardTip playerStandardTip = (PlayerStandardTip) tip;

        if (!playerStandardTip.image.isEmpty()) {
            ImageLoader.getInstance().displayImage(playerStandardTip.image, championImage);
        } else {
            championImage.setImageResource(playerStandardTip.imageId);
        }

        titleText.setText(playerStandardTip.text);
        descriptionText.setText(playerStandardTip.description);
    }
}
