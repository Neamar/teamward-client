package fr.neamar.lolgamedata.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.holder.CounterCountersHolder;
import fr.neamar.lolgamedata.holder.DummyHolder;
import fr.neamar.lolgamedata.holder.SectionHolder;
import fr.neamar.lolgamedata.pojo.Counter;

public class CounterCountersAdapter extends RecyclerView.Adapter<DummyHolder> {
    public final Counter counter;

    public CounterCountersAdapter(Counter counter) {
        this.counter = counter;
    }

    @Override
    public DummyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            View view = inflater.inflate(R.layout.item_section, parent, false);

            return new SectionHolder(view);
        } else {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            View view = inflater.inflate(R.layout.item_counter_counter, parent, false);

            return new CounterCountersHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(DummyHolder holder, int position) {
        if (getItemViewType(position) == 1) {
            ((CounterCountersHolder) holder).bind(counter.counters.get(getRealPosition(position)), counter);
        } else if (position == 0) {
            ((SectionHolder) holder).bindSection(R.string.good_counters, counter.goodCountersThreshold);
        } else {
            ((SectionHolder) holder).bindSection(R.string.bad_counters, counter.counters.size() - counter.goodCountersThreshold);
        }
    }

    @Override
    public int getItemCount() {
        return counter.counters.size() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == counter.goodCountersThreshold + 1) {
            return 0;
        }
        return 1;
    }

    public int getRealPosition(int position) {
        if (position <= counter.goodCountersThreshold) {
            return position - 1;
        }

        return position - 2;
    }
}
