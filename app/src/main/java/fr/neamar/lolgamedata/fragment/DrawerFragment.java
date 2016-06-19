package fr.neamar.lolgamedata.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import fr.neamar.lolgamedata.AccountManager;
import fr.neamar.lolgamedata.AddAccountActivity;
import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.adapter.AccountAdapter;
import fr.neamar.lolgamedata.pojo.Account;

/**
 * A simple {@link Fragment} subclass.
 */
public class DrawerFragment extends Fragment {
    private static final String TAG = "DrawerFragment";
    protected RecyclerView recyclerView;
    protected AccountManager accountManager;
    private BroadcastReceiver mBroadcastReceiver;

    public DrawerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_drawer, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        accountManager = new AccountManager(getActivity());

        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
        assert recyclerView != null;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ArrayList<Account> accounts = accountManager.getAccounts();

        final AccountAdapter adapter = new AccountAdapter(accounts);
        recyclerView.setAdapter(adapter);

        getView().findViewById(R.id.addLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AddAccountActivity.class);
                startActivity(i);
            }
        });

        getView().findViewById(R.id.settingsLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Settings will be there soon! Check back next update...", Toast.LENGTH_SHORT).show();
            }
        });

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                adapter.updateAccounts(accountManager.getAccounts());
            }
        };

        Log.i(TAG, "Starting account change receiver");
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mBroadcastReceiver, new IntentFilter(AccountManager.ACCOUNTS_CHANGE));
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Stopping account change receiver");
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mBroadcastReceiver);

        super.onStop();
    }
}
