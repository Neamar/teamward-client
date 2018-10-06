package fr.neamar.lolgamedata;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.widget.Toast;

import fr.neamar.lolgamedata.pojo.Account;

public class BottomNavigationListener implements BottomNavigationView.OnNavigationItemSelectedListener {
    private final Context context;
    private final Account account;
    @IdRes
    private final int selectedAction;

    public BottomNavigationListener(Context context, Account account, @IdRes int selectedAction) {
        this.context = context;
        this.account = account;
        this.selectedAction = selectedAction;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == selectedAction) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_pre_game:
                Intent counterIntent = new Intent(context, CounterChampionsActivity.class);
                counterIntent.putExtra("account", account);
                counterIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context.startActivity(counterIntent);
                break;
            case R.id.action_in_game:
                Intent gameIntent = new Intent(context, GameActivity.class);
                gameIntent.putExtra("account", account);
                gameIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context.startActivity(gameIntent);
                break;
            case R.id.action_stats:
                Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}
