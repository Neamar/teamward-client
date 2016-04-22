package fr.neamar.lolgamedata;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
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
}
