package fr.neamar.lolgamedata.holder;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.pojo.Match;

public class MatchHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final TextView kdaText;
    private final ImageView wardImage;
    private final View winOrLossView;

    private final ArrayList<ImageView> itemImages = new ArrayList<>(6);

    private Match match;

    public MatchHolder(View view) {
        super(view);

        kdaText = (TextView) view.findViewById(R.id.kda);
        wardImage = (ImageView) view.findViewById(R.id.itemWard);
        winOrLossView = view.findViewById(R.id.winOrLossBanner);

        itemImages.add((ImageView) view.findViewById(R.id.item1));
        itemImages.add((ImageView) view.findViewById(R.id.item2));
        itemImages.add((ImageView) view.findViewById(R.id.item3));
        itemImages.add((ImageView) view.findViewById(R.id.item4));
        itemImages.add((ImageView) view.findViewById(R.id.item5));
        itemImages.add((ImageView) view.findViewById(R.id.item6));


        view.setOnClickListener(this);
    }

    public void bind(Match match) {
        this.match = match;

        kdaText.setText(Html.fromHtml(String.format(kdaText.getContext().getString(R.string.kda_template), match.k, match.d, match.a)));

        ImageLoader.getInstance().displayImage(match.ward, wardImage);

        for (int i = 0; i < match.items.size(); i++) {
            ImageLoader.getInstance().displayImage(match.items.get(i), itemImages.get(i));
        }

        for (int i = match.items.size(); i < itemImages.size(); i++) {
            itemImages.get(i).setImageResource(R.drawable.item_emtpy);
        }

        winOrLossView.setBackgroundColor(winOrLossView.getContext().getResources().getColor(match.victory ? R.color.colorGoodMatchup : R.color.colorBadMatchup));
    }

    @Override
    public void onClick(View v) {
        // TODO open link
    }
}
