package fr.neamar.lolgamedata.holder;

import androidx.annotation.StringRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import fr.neamar.lolgamedata.R;

public class SectionHolder extends DummyHolder {
    private final TextView textView;
    private final ImageView noCountersView;

    public SectionHolder(View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.sectionTitle);
        noCountersView = (ImageView) itemView.findViewById(R.id.noCounters);
    }

    public void bindSection(@StringRes int textId, String role, int itemsInSection) {
        textView.setText(String.format(textView.getContext().getString(textId), role));

        noCountersView.setVisibility(itemsInSection == 0 ? View.VISIBLE : View.GONE);
    }
}