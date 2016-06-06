package fr.neamar.lolgamedata;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by neamar on 03/04/16.
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

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

    public void displayAboutDialog() {
        mFirebaseAnalytics.logEvent("display_about_dialog", new Bundle());
        new AlertDialog.Builder(this)
                .setTitle(R.string.action_about)
                .setMessage(getString(R.string.about_text))
                .setPositiveButton(R.string.rammus_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
}
