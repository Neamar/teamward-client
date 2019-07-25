package fr.neamar.lolgamedata.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Locale;

import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.holder.CounterChampionHolder;
import fr.neamar.lolgamedata.pojo.Counters;

public class CounterChampionAdapter extends RecyclerView.Adapter<CounterChampionHolder> {
    private final Counters allCounters;
    private Counters counters;

    public CounterChampionAdapter(Counters counters) {
        this.allCounters = counters;
        this.counters = this.allCounters;

        setHasStableIds(true);
    }

    @NonNull
    @Override
    public CounterChampionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.item_counter_champion, parent, false);

        return new CounterChampionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CounterChampionHolder holder, int position) {
        holder.bind(counters.counters.get(position));
    }

    @Override
    public int getItemCount() {
        return counters.counters.size();
    }

    @Override
    public long getItemId(int position) {
        return counters.counters.get(position).champion.name.hashCode();
    }

    public void filter(String filter) {
        counters = new Counters();
        Locale locale = Locale.getDefault();
        for (int i = 0; i < allCounters.counters.size(); i++) {
            if (allCounters.counters.get(i).champion.name.toLowerCase(locale).startsWith(filter.toLowerCase(locale))) {
                counters.counters.add(allCounters.counters.get(i));
            }
        }

        notifyDataSetChanged();
    }
}
