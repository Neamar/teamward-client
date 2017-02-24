package fr.neamar.lolgamedata;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.support.annotation.NonNull;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Map;

import fr.neamar.lolgamedata.pojo.Account;
import fr.neamar.lolgamedata.pojo.Counter;
import fr.neamar.lolgamedata.pojo.Game;
import fr.neamar.lolgamedata.pojo.Player;

import static fr.neamar.lolgamedata.GameActivity.getMapName;

public class Tracker {
    private static LolApplication getApplication(@NonNull Activity activity) {
        return ((LolApplication) activity.getApplication());
    }

    private static MixpanelAPI getMixpanel(@NonNull Activity activity) {
        return getApplication(activity).getMixpanel();
    }

    static void flush(Activity activity) {
        getMixpanel(activity).flush();
    }

    static void trackGameViewed(Activity activity, Account account, Game game, String defaultTab, Boolean shouldDisplayChampionName, String source) {
        JSONObject j = account.toJsonObject();
        LolApplication application = getApplication(activity);

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

        MixpanelAPI mixpanel = getMixpanel(activity);
        mixpanel.track("Game viewed", j);

        mixpanel.getPeople().increment("games_viewed_count", 1);
        mixpanel.getPeople().set("last_viewed_game", new Date());
    }

    static void trackErrorViewingGame(Activity activity, Account account, String error) {
        JSONObject j = account.toJsonObject();
        try {
            j.put("error", error);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getMixpanel(activity).track("Error viewing game", j);
    }

    static void trackRateTheApp(Activity activity) {
        MixpanelAPI mixpanel = getMixpanel(activity);
        mixpanel.track("Rate the app");
        mixpanel.getPeople().set("app_rated", true);
    }

    static void trackFirstTimeAppOpen(Activity activity) {
        getMixpanel(activity).track("First time app open");
    }

    static void trackReloadStaleGame(Activity activity, Account account) {
        getMixpanel(activity).track("Reload stale game", account.toJsonObject());
    }

    static void trackSettingsUpdated(Context context, Preference preference, String value) {
        JSONObject j = new JSONObject();
        try {
            j.put("setting", preference.getKey());
            j.put("setting_name", preference.getTitle());
            j.put("value", value);
        }
        catch(JSONException e) {
            e.printStackTrace();
        }

        ((LolApplication) context.getApplicationContext()).getMixpanel().track("Setting updated", j);
    }

    static void trackAccessSettings(Activity activity) {
        getMixpanel(activity).track("Access settings");
    }

    static void trackViewChampionCounters(Activity activity, Counter counter) {

        JSONObject j = new JSONObject();
        try {
            j.put("role", counter.role);
            j.put("champion", counter.champion.name);
            j.put("counters", counter.counters.size());
            j.put("goodCountersThreshold", counter.goodCountersThreshold);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        getMixpanel(activity).track("View champion counters", j);
    }

    static void trackClickOnGG(Activity activity, Counter counter) {
        JSONObject j = new JSONObject();

        try {
            j.put("role", counter.role);
            j.put("champion", counter.champion.name);
            j.put("counters", counter.counters.size());
            j.put("source", "counters");
            j.put("goodCountersThreshold", counter.goodCountersThreshold);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        getMixpanel(activity).track("Click on GG", j);
    }

    static void trackClickOnGG(Activity activity, Player player) {
        JSONObject j = new JSONObject();
        try {
            j.put("name", player.summoner.name);
            j.put("region", player.region.toUpperCase());
            j.put("champion", player.champion.name);
            j.put("tier", player.rank.tier);
            j.put("division", player.rank.division);
            j.put("source", "detail view");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        getMixpanel(activity).track("Click on GG", j);
    }

    static void trackDetailsViewed(Activity activity, Player player) {
        JSONObject j = new JSONObject();
        try {
            j.put("region", player.region.toUpperCase());
            j.put("name", player.summoner.name);
            j.put("champion", player.champion.name);
            j.put("tier", player.rank.tier);
            j.put("division", player.rank.division);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        getMixpanel(activity).track("Details viewed", j);
    }

    static void trackcErrorViewingDetails(Activity activity, String error) {
        JSONObject j = new JSONObject();
        try {
            j.put("error", error);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getMixpanel(activity).track("Error viewing details", j);
    }

    static void trackAccountAdded(Activity activity, Account account, int index) {
        JSONObject j = account.toJsonObject();
        try {
            j.putOpt("account_index", index);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getMixpanel(activity).track("Account added", j);
        getApplication(activity).identifyOnMixpanel();
    }

    static void trackErrorAddingAccount(Activity activity, Account account, String error) {
        JSONObject j = account.toJsonObject();
        try {
            j.putOpt("error", error);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getMixpanel(activity).track("Error adding account", j);
    }

    public static void trackViewCounters(Activity activity, Account account, String role, int requiredChampionMastery) {
        JSONObject j = account.toJsonObject();
        try {
            j.put("role", role);
            j.put("mastery", requiredChampionMastery);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        getMixpanel(activity).track("View counters", j);
    }

    public static void trackErrorViewingCounters(Activity activity, Account account, String error) {
        JSONObject j = account.toJsonObject();
        try {
            j.put("error", error);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getMixpanel(activity).track("Error viewing counters", j);
    }

    static void trackUserProperties(Context context, Account account, int accountsLength, SharedPreferences sp) {
        MixpanelAPI.People people = ((LolApplication) context.getApplicationContext()).getMixpanel().getPeople();

        people.set("accounts_length", accountsLength);
        people.set("$username", account.summonerName);
        people.set("$name", account.summonerName);
        people.set("region", account.region);

        Map<String, ?> properties = sp.getAll();
        for (Map.Entry<String, ?> entry : properties.entrySet()) {
            people.set("settings_" + entry.getKey(), entry.getValue());
        }
    }
}
