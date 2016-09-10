package fr.neamar.lolgamedata;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import fr.neamar.lolgamedata.adapter.CounterCountersAdapter;
import fr.neamar.lolgamedata.pojo.Counter;

public class CounterCountersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter_counters);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Counter counter = (Counter) getIntent().getSerializableExtra("counter");
        CounterCountersAdapter adapter = new CounterCountersAdapter(counter);

        recyclerView.setAdapter(adapter);

        setTitle(String.format(getString(R.string.counter_counters_activity_title), counter.champion.name));
    }

}
