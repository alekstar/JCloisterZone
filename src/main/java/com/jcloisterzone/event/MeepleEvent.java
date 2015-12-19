package com.jcloisterzone.event;

import com.jcloisterzone.Player;
import com.jcloisterzone.board.pointer.FeaturePointer;
import com.jcloisterzone.feature.Feature;
import com.jcloisterzone.figure.Meeple;
import com.jcloisterzone.game.Game;

public class MeepleEvent extends MoveEvent<FeaturePointer> implements Undoable {

    private final Meeple meeple;

    public MeepleEvent(Player triggeringPlayer, Meeple meeple, FeaturePointer from, FeaturePointer to) {
        super(triggeringPlayer, from, to);
        this.meeple = meeple;
    }

    public Meeple getMeeple() {
        return meeple;
    }

    @Override
    public void undo(Game game) {
        if (getTo() != null) {
            meeple.getFeature().removeMeeple(meeple);
            meeple.clearDeployment();
        }
        if (getFrom() != null) {
            Feature f = game.getBoard().get(getFrom());
            f.addMeeple(meeple);
            meeple.setFeaturePointer(getFrom());
            meeple.setFeature(f);
        }
    }

    @Override
    public Event getInverseEvent() {
        return new MeepleEvent(getTriggeringPlayer(), meeple, to, from);
    }

    @Override
    public String toString() {
        return super.toString() + " meeple:" + meeple + " player:" + meeple.getPlayer().getNick();
    }
}
