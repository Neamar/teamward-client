package fr.neamar.lolgamedata.holder;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import fr.neamar.lolgamedata.AccountManager;
import fr.neamar.lolgamedata.GameActivity;
import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.pojo.Account;

public class AccountHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    private final ImageView summonerImage;
    private final TextView summonerName;
    private Account account;

    public AccountHolder(View view) {
        super(view);

        summonerImage = (ImageView) view.findViewById(R.id.summonerImage);
        summonerName = (TextView) view.findViewById(R.id.summonerText);

        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
    }

    public void bind(Account account) {
        this.account = account;

        this.summonerName.setText(account.summonerName);

        ImageLoader.getInstance().displayImage(account.summonerImage, summonerImage);
        summonerImage.setContentDescription(account.summonerName);
    }

    @Override
    public void onClick(View v) {
        if (account == null) {
            return;
        }

        Intent i = new Intent(v.getContext(), GameActivity.class);
        i.putExtra("account", account);
        i.putExtra("source", "drawer");
        v.getContext().startActivity(i);
    }

    @Override
    public boolean onLongClick(final View v) {
        new AlertDialog.Builder(v.getContext())
                .setTitle(R.string.dialog_remove_account_title)
                .setMessage((R.string.dialog_remove_account_message))
                .setPositiveButton(R.string.dialog_remove, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        AccountManager accountManager = new AccountManager(v.getContext());
                        accountManager.removeAccount(account);

                        Toast.makeText(v.getContext(), R.string.account_removed, Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();

        return true;
    }
}
