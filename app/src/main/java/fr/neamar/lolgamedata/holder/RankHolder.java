package fr.neamar.lolgamedata.holder;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;

import fr.neamar.lolgamedata.PerformanceActivity;
import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.Tracker;
import fr.neamar.lolgamedata.pojo.Player;
import fr.neamar.lolgamedata.pojo.Rank;

import static fr.neamar.lolgamedata.holder.PlayerHolder.RANKING_TIER_RESOURCES;

public class RankHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final TextView rankedQueue;
    private final TextView rankedText;
    private final TextView rankedWinrate;

    private final ImageView rankedTier;

    private Player player;

    public RankHolder(View view) {
        super(view);

        rankedQueue = view.findViewById(R.id.rankedQueue);
        rankedText = view.findViewById(R.id.rankedText);
        rankedWinrate = view.findViewById(R.id.rankedWinrate);
        rankedTier = view.findViewById(R.id.rankedTierImage);

        view.setOnClickListener(this);
    }

    public void bind(Player player, Rank rank) {
        this.player = player;

        rankedTier.setImageResource(RANKING_TIER_RESOURCES.get(rank.tier.toLowerCase(Locale.ROOT)));
        rankedText.setText(String.format(rankedText.getContext().getString(R.string.ranking), rank.tier.toUpperCase(Locale.ROOT), rank.division));
        rankedQueue.setText(PerformanceActivity.getQueueName(rank.queue));

        int totalGames = rank.wins + rank.losses;
        if(totalGames >= 10) {
            float winrate = 100 * (float) rank.wins / totalGames;
            rankedWinrate.setText(String.format(rankedWinrate.getContext().getString(R.string.s_detailed_winrate), totalGames, winrate));
            rankedWinrate.setVisibility(View.VISIBLE);
        }
        else {
            rankedWinrate.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://" + player.region + ".op.gg/summoner/userName=" + URLEncoder.encode(player.summoner.name, "UTF-8")));
            v.getContext().startActivity(browserIntent);

            Tracker.trackClickOnOpGG((Activity) v.getContext(), player);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch(ClassCastException e) {
            e.printStackTrace();
        }
        catch(ActivityNotFoundException e) {
            Toast.makeText(v.getContext(), R.string.unable_to_open_browser, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
