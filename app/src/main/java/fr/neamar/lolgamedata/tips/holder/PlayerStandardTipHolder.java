package fr.neamar.lolgamedata.tips.holder;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.Tracker;
import fr.neamar.lolgamedata.holder.TipHolder;
import fr.neamar.lolgamedata.tips.PlayerStandardTip;
import fr.neamar.lolgamedata.tips.Tip;

public class PlayerStandardTipHolder extends TipHolder {
    private final ImageView championImage;
    private final TextView descriptionText;
    private final TextView titleText;

    private PlayerStandardTipHolder(View itemView) {
        super(itemView);

        championImage = (ImageView) itemView.findViewById(R.id.championImage);
        titleText = (TextView) itemView.findViewById(R.id.title);
        descriptionText = (TextView) itemView.findViewById(R.id.hotStreakDescription);
    }

    public static TipHolder onCreateViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.item_tip_text_description_image, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getTag() == null) {
                    return;
                }
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(v.getTag().toString()));
                v.getContext().startActivity(browserIntent);
                Tracker.trackHttpClicked(v.getContext(), v.getTag().toString());
            }
        });
        return new PlayerStandardTipHolder(view);
    }

    public void bind(Tip tip) {
        PlayerStandardTip playerStandardTip = (PlayerStandardTip) tip;

        if (!playerStandardTip.image.isEmpty()) {
            ImageLoader.getInstance().displayImage(playerStandardTip.image, championImage);
        } else {
            championImage.setImageResource(playerStandardTip.imageId);
        }

        if(playerStandardTip.urlTarget == null) {
            itemView.setClickable(false);
            itemView.setFocusable(true);
            itemView.setTag(null);
        }
        else {
            itemView.setClickable(true);
            itemView.setFocusable(true);
            itemView.setTag(playerStandardTip.urlTarget);
        }

        titleText.setText(playerStandardTip.text);
        descriptionText.setText(playerStandardTip.description);
    }
}
