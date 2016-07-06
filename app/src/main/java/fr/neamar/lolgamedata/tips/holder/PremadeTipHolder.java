package fr.neamar.lolgamedata.tips.holder;

import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.holder.TipHolder;
import fr.neamar.lolgamedata.pojo.Champion;
import fr.neamar.lolgamedata.pojo.Game;
import fr.neamar.lolgamedata.pojo.Player;
import fr.neamar.lolgamedata.pojo.Team;
import fr.neamar.lolgamedata.tips.PremadeTip;
import fr.neamar.lolgamedata.tips.Tip;

/**
 * Created by neamar on 04/07/16.
 */
public class PremadeTipHolder extends TipHolder {
    LinearLayout redTeamLayout;
    LinearLayout blueTeamLayout;

    public PremadeTipHolder(View itemView) {
        super(itemView);
        redTeamLayout = (LinearLayout) itemView.findViewById(R.id.redTeam);
        blueTeamLayout = (LinearLayout) itemView.findViewById(R.id.blueTeam);
    }

    public void bindTip(Tip tip) {
        PremadeTip premadeTip = (PremadeTip) tip;
        Game game = premadeTip.game;

        drawChampions(game.teams.get(0));
        drawChampions(game.teams.get(1));
    }

    public void drawChampions(Team team) {
        LinearLayout linearLayout = team.teamId == 100 ? blueTeamLayout : redTeamLayout;
        int dpConversion = (int) itemView.getResources().getDimension(R.dimen.tip_champion_thumbnail);
        int spConversion = (int) itemView.getResources().getDimension(R.dimen.tip_champion_text_separator);


        redTeamLayout.removeAllViews();
        for (List<Integer> subPremade : team.premades) {
            for(int summonerId: subPremade) {
                Champion champion = findPlayerById(team, summonerId).champion;

                ImageView imageview = new ImageView(itemView.getContext());
                imageview.setImageResource(R.drawable.default_champion);
                imageview.setLayoutParams(new LinearLayout.LayoutParams(dpConversion, dpConversion));
                linearLayout.addView(imageview);

                ImageLoader.getInstance().displayImage(champion.imageUrl, imageview);
                imageview.setContentDescription(champion.name);
            }

            if(subPremade != team.premades.get(team.premades.size() - 1)) {
                TextView textView = new TextView(itemView.getContext());
                textView.setText("—");
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(spConversion);
                linearLayout.addView(textView);
            }
        }
    }

    public Player findPlayerById(Team team, int summonerId) {
        for (Player player : team.players) {
            if (player.summoner.id == summonerId) {
                return player;
            }
        }

        throw new RuntimeException("Non existing player in premade?");
    }
}
