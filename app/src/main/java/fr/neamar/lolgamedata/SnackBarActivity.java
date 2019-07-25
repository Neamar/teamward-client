package fr.neamar.lolgamedata;

import android.content.Intent;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

public abstract class SnackBarActivity extends AppCompatActivity {
    public void displaySnack(String snack) {
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        assert coordinatorLayout != null;

        try {
            Snackbar snackbar = Snackbar.make(coordinatorLayout, snack, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        catch(NullPointerException|IllegalArgumentException e) {
            // If the activity is not visible anymore, this will throw.
            e.printStackTrace();
        }
    }

    void displaySnack(String snack, String action, View.OnClickListener listener) {
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        assert coordinatorLayout != null;

        Snackbar snackbar = Snackbar.make(coordinatorLayout, snack, 10000);
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
