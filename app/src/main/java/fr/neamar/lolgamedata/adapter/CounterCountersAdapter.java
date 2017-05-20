package fr.neamar.lolgamedata.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.holder.ChampionCounterCardHolder;
import fr.neamar.lolgamedata.holder.CounterCountersHolder;
import fr.neamar.lolgamedata.holder.DummyHolder;
import fr.neamar.lolgamedata.holder.SectionHolder;
import fr.neamar.lolgamedata.pojo.Counter;

public class CounterCountersAdapter extends RecyclerView.Adapter<DummyHolder> {
    private final Counter counter;

    public CounterCountersAdapter(Counter counter) {
        this.counter = counter;
    }

    @Override
    public DummyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            View view = inflater.inflate(R.layout.item_champion_card, parent, false);

            return new ChampionCounterCardHolder(view);
        } else if (viewType == 1) {
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
        if (position == 0) {
            ((ChampionCounterCardHolder) holder).bindChampion(counter.champion);
        }
        else if (getItemViewType(position) == 2) {
            ((CounterCountersHolder) holder).bind(counter.counters.get(getRealPosition(position)), counter);
        } else if (position == 1) {
            ((SectionHolder) holder).bindSection(R.string.good_counters, counter.role, counter.goodCountersThreshold);
        } else {
            ((SectionHolder) holder).bindSection(R.string.bad_counters, counter.role, counter.counters.size() - counter.goodCountersThreshold);
        }
    }

    @Override
    public int getItemCount() {
        return counter.counters.size() + 3;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        if (position == 1 || position == counter.goodCountersThreshold + 2) {
            return 1;
        }
        return 2;
    }

    private int getRealPosition(int position) {
        if (position - 1 <= counter.goodCountersThreshold) {
            return position - 2;
        }

        return position - 3;
    }
}
