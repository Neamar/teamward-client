package fr.neamar.lolgamedata.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.pojo.Account;

public class AccountHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private Account account;

    private final ImageView summonerImage;
    private final TextView summonerName;

    public AccountHolder(View view) {
        super(view);

        summonerImage = (ImageView) view.findViewById(R.id.summonerImage);
        summonerName = (TextView) view.findViewById(R.id.summonerNameText);

        view.setOnClickListener(this);
    }

    public void bindAdvert(Account account) {
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

        // TODO
    }
}
