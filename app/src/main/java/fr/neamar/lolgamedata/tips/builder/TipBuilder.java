package fr.neamar.lolgamedata.tips.builder;

import android.content.Context;

import java.util.ArrayList;

import fr.neamar.lolgamedata.pojo.Game;
import fr.neamar.lolgamedata.tips.Tip;

/**
 * Created by neamar on 04/07/16.
 */
public class TipBuilder {
    // Return an array list of tips related to the game
    public ArrayList<Tip> getTips(Game game, Context context) {
        return new ArrayList<>();
    }
}
