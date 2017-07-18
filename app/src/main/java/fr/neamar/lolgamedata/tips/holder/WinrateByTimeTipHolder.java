package fr.neamar.lolgamedata.tips.holder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.holder.TipHolder;
import fr.neamar.lolgamedata.tips.Tip;

/**
 * Created by neamar on 7/18/2017.
 */

public class WinrateByTimeTipHolder extends TipHolder {
    private final GraphView graphView;

    private WinrateByTimeTipHolder(View itemView) {
        super(itemView);

        graphView = (GraphView) itemView.findViewById(R.id.graph);
    }

    public static TipHolder onCreateViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.item_tip_winrate_by_time, parent, false);

        return new WinrateByTimeTipHolder(view);
    }

    public void bind(Tip tip) {
        // WinrateByTimeTip winrateByTimeTip = (WinrateByTimeTip) tip;

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graphView.addSeries(series);
    }
}
