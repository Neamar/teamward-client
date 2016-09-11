package fr.neamar.lolgamedata;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import fr.neamar.lolgamedata.adapter.CounterCountersAdapter;
import fr.neamar.lolgamedata.pojo.Counter;

public class CounterCountersActivity extends SnackBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter_counters);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Counter counter = (Counter) getIntent().getSerializableExtra("counter");
        CounterCountersAdapter adapter = new CounterCountersAdapter(counter);

        recyclerView.setAdapter(adapter);

        getSupportActionBar().setTitle(String.format(getString(R.string.counter_counters_activity_title), counter.champion.name));

        if(counter.counters.size() == 0) {
            displaySnack(String.format("You don't play a single counter to %s. Go play more champions!", counter.champion.name));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
