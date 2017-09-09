package fr.neamar.lolgamedata.holder;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import fr.neamar.lolgamedata.PlayerDetailActivity;
import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.pojo.Match;

public class MatchHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final TextView kdaText;
    private final TextView gameLengthText;
    private final TextView csText;


    private final ImageView wardImage;
    private final View winOrLossView;

    private final ArrayList<ImageView> itemImages = new ArrayList<>(6);

    private Match match;

    public MatchHolder(View view) {
        super(view);

        kdaText = (TextView) view.findViewById(R.id.kdaText);
        wardImage = (ImageView) view.findViewById(R.id.itemWard);
        winOrLossView = view.findViewById(R.id.winOrLossBanner);
        gameLengthText = (TextView) view.findViewById(R.id.gameLengthText);
        csText = (TextView) view.findViewById(R.id.csText);

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

        String kdaTemplate = kdaText.getContext().getString(R.string.kda_template);
        kdaText.setText(Html.fromHtml(String.format(kdaTemplate, match.k, match.d, match.a)));

        if (match.ward == null) {
            wardImage.setImageResource(R.drawable.item_empty);
        } else {
            ImageLoader.getInstance().displayImage(match.ward, wardImage);
        }
        for (int i = 0; i < match.items.size(); i++) {
            ImageLoader.getInstance().displayImage(match.items.get(i), itemImages.get(i));
        }

        for (int i = match.items.size(); i < itemImages.size(); i++) {
            itemImages.get(i).setImageResource(R.drawable.item_empty);
        }

        winOrLossView.setBackgroundColor(winOrLossView.getContext().getResources().getColor(match.victory ? R.color.colorGoodMatchup : R.color.colorBadMatchup));


        int minDuration = match.duration / 60;
        int secDuration = match.duration % 60;
        String gameLengthTemplate = this.gameLengthText.getContext().getString(R.string.game_length_template);
        String queueName = winOrLossView.getContext().getString(PlayerDetailActivity.getQueueName(match.queue));
        this.gameLengthText.setText(String.format(gameLengthTemplate, minDuration < 10 ? "0" + minDuration : minDuration, secDuration < 10 ? "0" + secDuration : secDuration, match.role, queueName));

        float csPerMin = (float) match.cs / minDuration;
        String csTemplate = this.gameLengthText.getContext().getString(R.string.cs_template);
        this.csText.setText(Html.fromHtml(String.format(csTemplate, match.cs, csPerMin)));
    }

    @Override
    public void onClick(View v) {
        String url = match.matchUrl;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        try {
            v.getContext().startActivity(i);
        }
        catch(ActivityNotFoundException e) {
            Toast.makeText(v.getContext(), R.string.unable_to_open_browser, Toast.LENGTH_SHORT).show();
        }
    }
}
