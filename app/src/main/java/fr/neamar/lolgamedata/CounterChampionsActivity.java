package fr.neamar.lolgamedata;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

import fr.neamar.lolgamedata.adapter.RoleAdapter;
import fr.neamar.lolgamedata.fragment.CounterChampionsFragment;
import fr.neamar.lolgamedata.pojo.Account;

public class CounterChampionsActivity extends SnackBarActivity {
    private static final String[] ROLES = new String[]{
            "Top",
            "Jungle",
            "Mid",
            "Bot",
            "Support"
    };
    private static final String TAG = "CounterChampionActivity";
    private CounterChampionsFragment currentFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter_champions);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Setup spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(new RoleAdapter(
                toolbar.getContext(),
                ROLES));

        final Account account = (Account) getIntent().getSerializableExtra("account");

        final SharedPreferences prefs = getPreferences(MODE_PRIVATE);

        spinner.setSelection(prefs.getInt("lastUsedPosition", 2));

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentFragment = CounterChampionsFragment.newInstance(ROLES[position], account);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, currentFragment)
                        .commitAllowingStateLoss();

                prefs.edit().putInt("lastUsedPosition", position).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ((BottomNavigationView) findViewById(R.id.bottom_navigation)).setOnNavigationItemSelectedListener(new BottomNavigationListener(this, account, R.id.action_pre_game));
        ((BottomNavigationView) findViewById(R.id.bottom_navigation)).setSelectedItemId(R.id.action_pre_game);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_counter_champions, menu);

        final MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e(TAG, "SearchOnQueryTextSubmit: " + query);
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                searchMenuItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.e(TAG, "Filtering: " + s);

                if (currentFragment != null) {
                    currentFragment.filterChampions(s);
                }
                return false;
            }
        });
        return true;
    }
}
