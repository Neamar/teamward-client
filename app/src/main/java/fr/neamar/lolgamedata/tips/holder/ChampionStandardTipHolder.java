package fr.neamar.lolgamedata.tips.holder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.holder.TipHolder;
import fr.neamar.lolgamedata.tips.ChampionStandardTip;
import fr.neamar.lolgamedata.tips.Tip;
import fr.neamar.lolgamedata.view.ChampionView;

public class ChampionStandardTipHolder extends TipHolder {
    private final ChampionView championImage;
    private final TextView descriptionText;
    private final TextView titleText;

    private ChampionStandardTipHolder(View itemView) {
        super(itemView);

        championImage = (ChampionView) itemView.findViewById(R.id.championImage);
        titleText = (TextView) itemView.findViewById(R.id.title);
        descriptionText = (TextView) itemView.findViewById(R.id.hotStreakDescription);
    }

    public static TipHolder onCreateViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.item_tip_text_description_champion, parent, false);

        return new ChampionStandardTipHolder(view);
    }

    public void bind(Tip tip) {
        ChampionStandardTip playerStandardTip = (ChampionStandardTip) tip;

        if (playerStandardTip.champion != null) {
            championImage.setChampion(playerStandardTip.champion);
            championImage.setTeam(playerStandardTip.game.getTeamForPlayer(playerStandardTip.player));
            championImage.setBorderMode(ChampionView.BorderMode.TEAM);
        } else {
            championImage.setImageResource(playerStandardTip.imageId);
        }

        titleText.setText(playerStandardTip.text);
        descriptionText.setText(playerStandardTip.description);
    }
}
