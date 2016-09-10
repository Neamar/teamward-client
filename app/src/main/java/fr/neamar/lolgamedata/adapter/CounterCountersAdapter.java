package fr.neamar.lolgamedata.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.holder.CounterCountersHolder;
import fr.neamar.lolgamedata.pojo.Counter;

/**
 * Created by neamar on 14/08/16.
 */
public class CounterCountersAdapter extends RecyclerView.Adapter<CounterCountersHolder> {
    public final Counter counter;

    public CounterCountersAdapter(Counter counter) {
        this.counter = counter;
    }

    @Override
    public CounterCountersHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.item_counter_counter, parent, false);

        return new CounterCountersHolder(view);
    }

    @Override
    public void onBindViewHolder(CounterCountersHolder holder, int position) {
        holder.bindAdvert(counter.counters.get(position), counter);
    }

    @Override
    public int getItemCount() {
        return counter.counters.size();
    }
}
