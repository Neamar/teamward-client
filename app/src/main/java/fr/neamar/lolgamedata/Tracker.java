package fr.neamar.lolgamedata;

import android.app.Activity;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import fr.neamar.lolgamedata.pojo.Account;
import fr.neamar.lolgamedata.pojo.Game;
import fr.neamar.lolgamedata.pojo.Player;

import static fr.neamar.lolgamedata.GameActivity.getMapName;

/**
 * Created by neamar on 23/02/17.
 */

public class Tracker {
    public static void trackGameViewed(Activity activity, Account account, Game game, String defaultTab, Boolean shouldDisplayChampionName, String source) {
        // Timing automatically added (see timeEvent() call)
        JSONObject j = account.toJsonObject();
        LolApplication application = ((LolApplication) activity.getApplication());

        try {
            j.putOpt("game_map_id", game.mapId);

            j.putOpt("game_map_name", activity.getString(getMapName(game.mapId)));
            j.putOpt("game_mode", game.gameMode);
            j.putOpt("game_type", game.gameType);
            j.putOpt("game_id", game.gameId);
            j.putOpt("default_tab", defaultTab);
            j.putOpt("display_champion_name", shouldDisplayChampionName);
            j.putOpt("source", source);

            // Is this the first account added to the app?
            AccountManager accountManager = new AccountManager(activity);
            int accountIndex = accountManager.getAccountIndex(account);
            j.putOpt("account_index", accountIndex);

            // Add metrics related to the current player
            Player p = game.getPlayerByAccount(account);
            if (p != null) {
                j.putOpt("champion", p.champion.id);
                j.putOpt("champion_name", p.champion.name);
                j.putOpt("champion_mastery", p.champion.mastery);
                j.putOpt("champion_role", p.champion.role);

                j.putOpt("player_rank", p.rank.tier.isEmpty() ? p.rank.oldTier : p.rank.tier);
                j.putOpt("player_level", p.summoner.level);
                j.putOpt("player_champion_index", p.champion.championRank);

                j.putOpt("spell_d", p.spellD.id);
                j.putOpt("spell_d_name", p.spellD.name);

                j.putOpt("spell_f", p.spellF.id);
                j.putOpt("spell_f_name", p.spellF.name);

                if (accountIndex == 0) {
                    // For main user: add data to profile
                    MixpanelAPI.People people = application.getMixpanel().getPeople();
                    people.set("player_rank", p.rank.tier.isEmpty() ? p.rank.oldTier : p.rank.tier);

                    people.union("played_champion_names", application.getJSONArrayFromSingleItem(p.champion.name));
                    people.union("played_champion_ids", application.getJSONArrayFromSingleItem(Integer.toString(p.champion.id)));
                    people.union("played_roles", application.getJSONArrayFromSingleItem(p.champion.role));
                    people.union("played_champions_index", application.getJSONArrayFromSingleItem(Integer.toString(p.champion.championRank)));
                    people.union("played_game_ids", application.getJSONArrayFromSingleItem(Long.toString(game.gameId)));
                    people.union("played_map_ids", application.getJSONArrayFromSingleItem(Long.toString(game.mapId)));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        application.getMixpanel().track("Game viewed", j);
    }
}
