package fr.neamar.lolgamedata.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.holder.CounterHolder;
import fr.neamar.lolgamedata.pojo.Counters;

/**
 * Created by neamar on 14/08/16.
 */
public class CounterCountersAdapter extends RecyclerView.Adapter<CounterHolder> {
    public final Counters counters;

    public CounterCountersAdapter(Counters counters) {
        this.counters = counters;
    }

    @Override
    public CounterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.item_counter_counters, parent, false);

        return new CounterHolder(view);
    }

    @Override
    public void onBindViewHolder(CounterHolder holder, int position) {
        holder.bindAdvert(counters.counters.get(position));
    }

    @Override
    public int getItemCount() {
        return counters.counters.size();
    }
}
