package fr.neamar.lolgamedata.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import fr.neamar.lolgamedata.HomeActivity;
import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.holder.AccountHolder;
import fr.neamar.lolgamedata.pojo.Account;

/**
 * Created by neamar on 25/03/16.
 */
public class AccountAdapter extends RecyclerView.Adapter<AccountHolder> {
    public ArrayList<Account> accounts;
    public final HomeActivity homeActivity;
    private final View emptyView;
    private final RecyclerView recyclerView;

    public AccountAdapter(ArrayList<Account> accounts, HomeActivity homeActivity, View emptyView, RecyclerView recyclerView) {
        this.emptyView = emptyView;
        this.recyclerView = recyclerView;
        this.homeActivity = homeActivity;

        updateAccounts(accounts);
        setHasStableIds(true);
    }

    @Override
    public AccountHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.item_account, parent, false);

        return new AccountHolder(view);
    }

    public void updateAccounts(ArrayList<Account> accounts) {
        this.accounts = accounts;
        notifyDataSetChanged();

        if (accounts.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBindViewHolder(AccountHolder holder, int position) {
        holder.bindAdvert(this, accounts.get(position));
    }

    @Override
    public long getItemId(int position) {
        return accounts.get(position).hashCode();
    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }
}
