package fr.neamar.lolgamedata.tips.holder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.holder.TipHolder;
import fr.neamar.lolgamedata.pojo.Team;
import fr.neamar.lolgamedata.tips.Tip;

public class WinrateByTimeTipHolder extends TipHolder {
    private final GraphView graphView;
    private final TextView blueTeam;
    private final TextView redTeam;

    private WinrateByTimeTipHolder(View itemView) {
        super(itemView);

        graphView = (GraphView) itemView.findViewById(R.id.graph);
        blueTeam = (TextView) itemView.findViewById(R.id.blueTeam);
        redTeam = (TextView) itemView.findViewById(R.id.redTeam);

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

            blueTeam.setText(tip.game.getPlayerOwnTeam().teamId == 100 ? R.string.your_team : R.string.their_team);
            redTeam.setText(tip.game.getPlayerOwnTeam().teamId == 200 ? R.string.your_team : R.string.their_team);
        }
    }
}
