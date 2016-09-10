package fr.neamar.lolgamedata.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import fr.neamar.lolgamedata.LolApplication;
import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.adapter.CounterChampionAdapter;
import fr.neamar.lolgamedata.pojo.Account;
import fr.neamar.lolgamedata.pojo.Counter;
import fr.neamar.lolgamedata.pojo.Counters;

/**
 * A placeholder fragment containing a simple view.
 */
public class CounterChampionsFragment extends Fragment {
    public static final String TAG = "CounterChampionFragment";

    private static final String ARG_ROLE = "role";
    private static final String ARG_SUMMONER = "summoner";

    public String role;
    public Account user;
    public Counter counter;

    public CounterChampionsFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static CounterChampionsFragment newInstance(String role, Account user) {
        CounterChampionsFragment fragment = new CounterChampionsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ROLE, role);
        args.putSerializable(ARG_SUMMONER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        role = getArguments().getString(ARG_ROLE);
        user = (Account) getArguments().getSerializable(ARG_SUMMONER);

        final View rootView = inflater.inflate(R.layout.fragment_counter_champions, container, false);
        final RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        container.post(new Runnable() {
            @Override
            public void run() {
                // How many champions can we fit per row?
                float totalWidth = container.getWidth();
                float itemWidth = getResources().getDimension(R.dimen.champion_size) + 2 * getResources().getDimension(R.dimen.counter_champion_padding);

                int numItems = (int) Math.max(1, Math.floor(totalWidth / itemWidth));
                recyclerView.setLayoutManager(new GridLayoutManager(rootView.getContext(), numItems));

                loadCounters(recyclerView, user, role);
            }
        });

        return rootView;
    }

    private void loadCounters(final RecyclerView recyclerView, final Account account, final String role) {
        ((LolApplication) getActivity().getApplication()).getMixpanel().timeEvent("View counters");

        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(getActivity());
        final String summonerName = account.summonerName;
        String region = account.region;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        final int requiredChampionMastery = Integer.parseInt(prefs.getString("counter_required_mastery", "3"));

        try {
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, ((LolApplication) getActivity().getApplication()).getApiUrl() + "/summoner/counter?summoner=" + URLEncoder.encode(summonerName, "UTF-8") + "&region=" + region.toLowerCase() + "&role=" + role.toLowerCase() + "&level=" + requiredChampionMastery, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Counters counters = new Counters(response);

                                CounterChampionAdapter adapter = new CounterChampionAdapter(counters);
                                recyclerView.setAdapter(adapter);

                                Log.i(TAG, "Loaded counters!");

                                JSONObject j = account.toJsonObject();
                                try {
                                    j.put("role", role);
                                    j.put("mastery", requiredChampionMastery);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                // Timing automatically added
                                if(getActivity() != null) {
                                    ((LolApplication) getActivity().getApplication()).getMixpanel().track("View counters", j);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            queue.stop();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, error.toString());

                    queue.stop();

                    error.printStackTrace();
/*
                    if (error instanceof NoConnectionError) {
                        displaySnack(getString(R.string.no_internet_connection));
                        return;
                    }


                    try {
                        String responseBody = new String(error.networkResponse.data, "utf-8");
                        Log.i(TAG, responseBody);

                        if (!responseBody.contains("ummoner not in game")) {
                            displaySnack(responseBody);
                            JSONObject j = account.toJsonObject();
                            j.put("error", responseBody.replace("Error:", ""));
                            ((LolApplication) getApplication()).getMixpanel().track("Error viewing game", j);
                        }
                        else {
                        }
                    } catch (UnsupportedEncodingException | JSONException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        // Do nothing, no text content in the HTTP reply.
                    }
*/
                }
            });

            jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                    5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(jsonRequest);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}