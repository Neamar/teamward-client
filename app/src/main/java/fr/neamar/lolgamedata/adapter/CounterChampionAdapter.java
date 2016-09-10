package fr.neamar.lolgamedata.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.holder.CounterChampionHolder;
import fr.neamar.lolgamedata.pojo.Counters;

public class CounterChampionAdapter extends RecyclerView.Adapter<CounterChampionHolder> {
    public final Counters counters;

    public CounterChampionAdapter(Counters counters) {
        this.counters = counters;
    }

    @Override
    public CounterChampionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.item_counter_champion, parent, false);

        return new CounterChampionHolder(view);
    }

    @Override
    public void onBindViewHolder(CounterChampionHolder holder, int position) {
        holder.bindAdvert(counters.counters.get(position));
    }

    @Override
    public int getItemCount() {
        return counters.counters.size();
    }
}
