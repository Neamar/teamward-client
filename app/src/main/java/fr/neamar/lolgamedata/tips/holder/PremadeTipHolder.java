package fr.neamar.lolgamedata.tips.holder;

import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.holder.TipHolder;
import fr.neamar.lolgamedata.pojo.Champion;
import fr.neamar.lolgamedata.pojo.Game;
import fr.neamar.lolgamedata.pojo.Team;
import fr.neamar.lolgamedata.tips.PremadeTip;
import fr.neamar.lolgamedata.tips.Tip;

/**
 * Created by neamar on 04/07/16.
 */
public class PremadeTipHolder extends TipHolder {
    View redTeamLayout;
    View blueTeamLayout;
    List<ImageView> redTeamChampionsImages = new ArrayList<>(5);
    List<View> redTeamChampionsSeparator = new ArrayList<>(5);
    List<ImageView> blueTeamChampionsImages = new ArrayList<>(5);
    List<View> blueTeamChampionsSeparator = new ArrayList<>(5);

    public PremadeTipHolder(View itemView) {
        super(itemView);
        redTeamLayout = itemView.findViewById(R.id.redTeam);
        blueTeamLayout = itemView.findViewById(R.id.blueTeam);

        int[] premadeChampionsIds = new int[]{R.id.premadeChampion1, R.id.premadeChampion2, R.id.premadeChampion3, R.id.premadeChampion4, R.id.premadeChampion5};
        for (int premadeChampionsId : premadeChampionsIds) {
            redTeamChampionsImages.add((ImageView) redTeamLayout.findViewById(premadeChampionsId));
            blueTeamChampionsImages.add((ImageView) blueTeamLayout.findViewById(premadeChampionsId));
        }
    }

    public void bindTip(Tip tip) {
        PremadeTip premadeTip = (PremadeTip) tip;
        Game game = premadeTip.game;

        drawChampions(game.teams.get(0));
        drawChampions(game.teams.get(1));
    }

    public void drawChampions(Team team) {
        List<ImageView> teamChampionsImages = team.teamId == 100 ? blueTeamChampionsImages : redTeamChampionsImages;

        for(int i = 0; i < 5; i++) {
            Champion champion = team.players.get(i).champion;
            ImageView championImage = teamChampionsImages.get(i);
            ImageLoader.getInstance().displayImage(champion.imageUrl, championImage);
            championImage.setContentDescription(champion.name);
        }
    }
}
