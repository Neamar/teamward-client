package fr.neamar.lolgamedata.tips.holder;

import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
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
    View redTeamLayout;
    View blueTeamLayout;
    List<ImageView> redTeamChampionsImages = new ArrayList<>(5);
    List<View> redTeamChampionsSeparator = new ArrayList<>(4);
    List<ImageView> blueTeamChampionsImages = new ArrayList<>(5);
    List<View> blueTeamChampionsSeparator = new ArrayList<>(4);

    public PremadeTipHolder(View itemView) {
        super(itemView);
        redTeamLayout = itemView.findViewById(R.id.redTeam);
        blueTeamLayout = itemView.findViewById(R.id.blueTeam);

        int[] premadeChampionsIds = new int[]{R.id.premadeChampion1, R.id.premadeChampion2, R.id.premadeChampion3, R.id.premadeChampion4, R.id.premadeChampion5};
        for (int premadeChampionsId : premadeChampionsIds) {
            redTeamChampionsImages.add((ImageView) redTeamLayout.findViewById(premadeChampionsId));
            blueTeamChampionsImages.add((ImageView) blueTeamLayout.findViewById(premadeChampionsId));
        }

        int[] premadeSeparatorsIds = new int[]{R.id.premadeSeparator1, R.id.premadeSeparator2, R.id.premadeSeparator3, R.id.premadeSeparator4};
        for (int premadeSeparatorsId : premadeSeparatorsIds) {
            redTeamChampionsSeparator.add(redTeamLayout.findViewById(premadeSeparatorsId));
            blueTeamChampionsSeparator.add(blueTeamLayout.findViewById(premadeSeparatorsId));
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
        List<View> teamChampionsSeparators = team.teamId == 100 ? blueTeamChampionsSeparator : redTeamChampionsSeparator;

        int championCounter = 0;
        for(int i = 0; i < team.premades.length(); i++) {
            try {
                JSONArray subPremade = team.premades.getJSONArray(i);
                for(int j = 0; j < subPremade.length(); j++) {
                    int summonerId = subPremade.getInt(j);
                    Champion champion = findPlayerById(team, summonerId).champion;
                    ImageView championImage = teamChampionsImages.get(championCounter);
                    ImageLoader.getInstance().displayImage(champion.imageUrl, championImage);
                    championImage.setContentDescription(champion.name);
                    if(championCounter < 4) {
                        teamChampionsSeparators.get(championCounter).setVisibility(View.INVISIBLE);
                    }
                    championCounter++;
                }
                if(championCounter < 5) {
                    teamChampionsSeparators.get(championCounter - 1).setVisibility(View.VISIBLE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public Player findPlayerById(Team team, int summonerId) {
        for(Player player: team.players) {
            if(player.summoner.id == summonerId) {
                return player;
            }
        }

        throw new RuntimeException("Non existing player in premade?");
    }
}
