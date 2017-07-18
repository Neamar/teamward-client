package fr.neamar.lolgamedata.tips.holder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.holder.TipHolder;
import fr.neamar.lolgamedata.tips.Tip;

import static fr.neamar.lolgamedata.R.id.graph;

public class WinrateByTimeTipHolder extends TipHolder {
    private final GraphView graphView;

    private WinrateByTimeTipHolder(View itemView) {
        super(itemView);

        graphView = (GraphView) itemView.findViewById(graph);

        graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    return super.formatLabel(value, isValueX) + "'";
                } else {
                    // show currency for y values
                    return super.formatLabel(value, isValueX) + "%";
                }
            }
        });
    }

    public static TipHolder onCreateViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.item_tip_winrate_by_time, parent, false);

        return new WinrateByTimeTipHolder(view);
    }

    public void bind(Tip tip) {
        // WinrateByTimeTip winrateByTimeTip = (WinrateByTimeTip) tip;

        LineGraphSeries<DataPoint> blueSeries = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(20, 50),
                new DataPoint(25, 55),
                new DataPoint(30, 57),
                new DataPoint(35, 52),
                new DataPoint(40, 48),
                new DataPoint(45, 40),
        });
        blueSeries.setColor(graphView.getContext().getResources().getColor(R.color.blueTeam));
        graphView.addSeries(blueSeries);

        LineGraphSeries<DataPoint> redSeries = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(20, 45),
                new DataPoint(25, 45),
                new DataPoint(30, 47),
                new DataPoint(35, 49),
                new DataPoint(40, 52),
                new DataPoint(45, 55),
        });
        blueSeries.setColor(graphView.getContext().getResources().getColor(R.color.redTeam));
        graphView.addSeries(redSeries);


        LineGraphSeries<DataPoint> championSeries = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(20, 40),
                new DataPoint(25, 45),
                new DataPoint(30, 49),
                new DataPoint(35, 55),
                new DataPoint(40, 45),
                new DataPoint(45, 40),
        });
        championSeries.setColor(graphView.getContext().getResources().getColor(R.color.yourChampion));
        graphView.addSeries(championSeries);
    }
}
