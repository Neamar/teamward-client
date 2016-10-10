package fr.neamar.lolgamedata;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by neamar on 03/04/16.
 */
public abstract class SnackBarActivity extends AppCompatActivity {
    public void displaySnack(String snack) {
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        assert coordinatorLayout != null;

        Snackbar snackbar = Snackbar.make(coordinatorLayout, snack, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public void displaySnack(String snack, String action, View.OnClickListener listener) {
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        assert coordinatorLayout != null;

        Snackbar snackbar = Snackbar.make(coordinatorLayout, snack, Snackbar.LENGTH_LONG);
        snackbar.setAction(action, listener);
        snackbar.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
