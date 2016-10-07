package fr.neamar.lolgamedata;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.json.JSONException;
import org.json.JSONObject;

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
            displaySnack(String.format(getString(R.string.no_counters), counter.champion.name));
        }

        JSONObject j = new JSONObject();
        try {
            j.put("champion", counter.champion);
            j.put("counters", counter.counters.size());
            j.put("goodCountersThreshold", counter.goodCountersThreshold);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        ((LolApplication) getApplication()).getMixpanel().track("View champion counters", j);
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
