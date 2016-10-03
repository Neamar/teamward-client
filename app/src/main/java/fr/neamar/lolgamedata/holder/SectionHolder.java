package fr.neamar.lolgamedata.holder;

import android.support.annotation.StringRes;
import android.view.View;
import android.widget.TextView;

import fr.neamar.lolgamedata.R;

public class SectionHolder extends DummyHolder {
    public TextView textView;
    public TextView noCountersView;

    public SectionHolder(View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.sectionTitle);
        noCountersView = (TextView) itemView.findViewById(R.id.noCounters);
    }

    public void bindSection(@StringRes int textId, int itemsInSection) {
        textView.setText(textId);

        noCountersView.setVisibility(itemsInSection == 0 ? View.VISIBLE : View.GONE);
    }
}