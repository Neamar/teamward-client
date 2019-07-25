package fr.neamar.lolgamedata.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.holder.CounterCountersNoDataHolder;
import fr.neamar.lolgamedata.pojo.Counter;

public class CounterCountersNoDataAdapter extends RecyclerView.Adapter<CounterCountersNoDataHolder> {
    private final Counter counter;

    public CounterCountersNoDataAdapter(Counter counter) {
        this.counter = counter;
    }

    @Override
    public CounterCountersNoDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.item_counter_no_data, parent, false);

        return new CounterCountersNoDataHolder(view);
    }

    @Override
    public void onBindViewHolder(CounterCountersNoDataHolder holder, int position) {
        holder.bind(counter.noData.get(position));
    }

    @Override
    public int getItemCount() {
        return counter.noData.size();
    }
}
