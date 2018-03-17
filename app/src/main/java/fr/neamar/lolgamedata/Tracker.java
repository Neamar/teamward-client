package fr.neamar.lolgamedata;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.support.annotation.NonNull;

import com.amplitude.api.Amplitude;
import com.amplitude.api.Identify;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Locale;
import java.util.Map;

import fr.neamar.lolgamedata.pojo.Account;
import fr.neamar.lolgamedata.pojo.Counter;
import fr.neamar.lolgamedata.pojo.Game;
import fr.neamar.lolgamedata.pojo.Player;

import static fr.neamar.lolgamedata.GameActivity.getMapName;

public class Tracker {
    private static LolApplication getApplication(@NonNull Context context) {
        return ((LolApplication) context.getApplicationContext());
    }

    private static void track(Activity activity, String eventName) {
        track(activity, eventName, new JSONObject());
    }

    // Track event on both Mixpanel and Amplitude
    private static void track(Activity activity, String eventName, JSONObject props) {
        LolApplication application = getApplication(activity);
        application.getAmplitude().logEvent(eventName, props);
    }

    private static void trackProfile(Context context, JSONObject props) {
        LolApplication application = (LolApplication) context.getApplicationContext();
        application.getAmplitude().setUserProperties(props);

        if(props.has("$username")) {
            application.getAmplitude().setUserId(props.optString("$username", null));
        }
    }


    static void trackGameViewed(Activity activity, Account account, Game game, String defaultTab, Boolean shouldDisplayChampionName, String source) {
        JSONObject j = account.toJsonObject();
        LolApplication application = getApplication(activity);

        try {
            j.putOpt("game_map_id", game.mapId);

            j.putOpt("game_map_name", activity.getString(getMapName(game.mapId)));
            j.putOpt("queue", game.queue);
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
                    JSONObject jp = new JSONObject();
                    jp.put("player_rank",  p.rank.tier.isEmpty() ? p.rank.oldTier : p.rank.tier);
                    jp.put("last_viewed_game", new Date());
                    trackProfile(activity, jp);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        track(activity, "Game viewed", j);

        Identify identify = new Identify()
                .add("games_viewed_count", 1)
                .set("last_viewed_game", new Date().toString());
        Amplitude.getInstance().identify(identify);
    }

    static void trackErrorViewingGame(Activity activity, Account account, String error) {
        JSONObject j = account.toJsonObject();
        try {
            j.put("error", error);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        track(activity, "Error viewing game", j);
    }

    public static void trackNotificationDisplayed(Context context, Account account, int mapId, String mapName, long gameId, boolean unableToDisplay) {
        JSONObject j = account.toJsonObject();
        try {
            j.put("game_map_id", mapId);
            j.put("game_map_name", mapName);
            j.put("game_id", gameId);
            j.put("notification_threw_runtime_error", unableToDisplay);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Send this event as outOfSession
        Amplitude.getInstance().logEvent("Notification displayed", j, true);
    }

    static void trackRateTheApp(Activity activity) {
        track(activity, "Rate the app");

        JSONObject j = new JSONObject();
        try {
            j.put("app_rated", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        trackProfile(activity, j);
    }

    static void trackFirstTimeAppOpen(Activity activity) {
        track(activity, "First time app open");
    }

    static void trackReloadStaleGame(Activity activity, Account account) {
        track(activity, "Reload stale game", account.toJsonObject());
    }

    static void trackSettingsUpdated(Context context, Preference preference, String value) {
        JSONObject j = new JSONObject();
        try {
            j.put("setting", preference.getKey());
            j.put("setting_name", preference.getTitle());
            j.put("value", value);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Amplitude.getInstance().logEvent("Setting updated", j);
    }

    static void trackAccessSettings(Activity activity) {
        track(activity, "Access settings");
    }

    static void trackViewChampionCounters(Activity activity, Counter counter) {

        JSONObject j = new JSONObject();
        try {
            j.put("role", counter.role);
            j.put("champion_name", counter.champion.name);
            j.put("champion", counter.champion.id);
            j.put("counters", counter.counters.size());
            j.put("goodCountersThreshold", counter.goodCountersThreshold);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        track(activity, "View champion counters", j);
    }

    static void trackClickOnGG(Activity activity, String championName, int championId, String source) {
        JSONObject j = new JSONObject();

        try {
            j.put("champion_name", championName);
            j.put("champion", championId);
            j.put("source", source);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        track(activity, "Click on GG", j);
    }

    static void trackClickOnOpGG(Activity activity, Player player) {
        JSONObject j = new JSONObject();
        try {
            j.put("region", player.region.toUpperCase(Locale.ROOT));
            j.put("name", player.summoner.name);
            j.put("champion_name", player.champion.name);
            j.put("champion", player.champion.id);
            j.put("tier", player.rank.tier);
            j.put("division", player.rank.division);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        track(activity, "Click on op.gg", j);
    }

    static void trackPerformanceViewed(Activity activity, Player player, int matchHistoryLength) {
        JSONObject j = new JSONObject();
        try {
            j.put("region", player.region.toUpperCase(Locale.ROOT));
            j.put("name", player.summoner.name);
            j.put("champion_name", player.champion.name);
            j.put("champion", player.champion.id);
            j.put("tier", player.rank.tier);
            j.put("division", player.rank.division);
            j.put("match_history_length", matchHistoryLength);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        track(activity, "Performance viewed", j);
    }

    static void trackChampionViewed(Activity activity, String championName, int championId, String from) {
        JSONObject j = new JSONObject();
        try {
            j.put("champion_name", championName);
            j.put("champion", championId);
            j.put("from", from);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        track(activity, "Champion viewed", j);
    }

    static void trackErrorViewingDetails(Activity activity, String region, String error) {
        JSONObject j = new JSONObject();
        try {
            j.put("region", region);
            j.put("error", error);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        track(activity, "Error viewing details", j);
    }

    static void trackAccountAdded(Activity activity, Account account, int index) {
        JSONObject j = account.toJsonObject();
        try {
            j.putOpt("account_index", index);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        track(activity, "Account added", j);
        getApplication(activity).identifyOnTrackers();
    }

    static void trackErrorAddingAccount(Activity activity, Account account, String error) {
        JSONObject j = account.toJsonObject();
        try {
            j.putOpt("error", error);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        track(activity, "Error adding account", j);
    }

    public static void trackViewCounters(Activity activity, Account account, String role, int requiredChampionMastery) {
        JSONObject j = account.toJsonObject();
        try {
            j.put("role", role);
            j.put("mastery", requiredChampionMastery);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        track(activity, "View counters", j);
    }

    public static void trackErrorViewingCounters(Activity activity, Account account, String error) {
        JSONObject j = account.toJsonObject();
        try {
            j.put("error", error);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        track(activity, "Error viewing counters", j);
    }

    static void trackUserProperties(Context context, Account mainAccount, int accountsLength, SharedPreferences sp) {
       JSONObject j = mainAccount.toJsonObject();

       try {
           j.put("accounts_length", accountsLength);
           j.put("$username", mainAccount.summonerName);
           j.put("$name", mainAccount.summonerName);
           j.put("region", mainAccount.region);

           Map<String, ?> properties = sp.getAll();
           for (Map.Entry<String, ?> entry : properties.entrySet()) {
               j.put("settings_" + entry.getKey(), entry.getValue());
           }
       } catch(JSONException e) {
           e.printStackTrace();
       }

       trackProfile(context, j);
    }
}
