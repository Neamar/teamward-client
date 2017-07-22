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
import fr.neamar.lolgamedata.pojo.Team;
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
        for(Team team: tip.game.teams) {
            DataPoint[] points = new DataPoint[team.winrateByGameLength.size()];

            int counter = 0;


            for(int i = 0; i < team.winrateByGameLength.size(); i++) {
                int key = team.winrateByGameLength.keyAt(i);
                points[counter] = new DataPoint(key, team.winrateByGameLength.get(key));
                counter += 1;
            }

            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points);

            if(team.teamId == 100) {
                series.setColor(graphView.getContext().getResources().getColor(R.color.blueTeam));
            }
            else {
                series.setColor(graphView.getContext().getResources().getColor(R.color.redTeam));
            }

            graphView.addSeries(series);
        }
    }
}
