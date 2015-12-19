package com.jcloisterzone.feature;


import com.jcloisterzone.PointCategory;
import com.jcloisterzone.board.Position;
import com.jcloisterzone.feature.visitor.score.CloisterScoreContext;
import com.jcloisterzone.feature.visitor.score.CompletableScoreContext;

import static com.jcloisterzone.ui.I18nUtils._;


public class Cloister extends TileFeature implements Completable {

    private boolean shrine;
    private boolean monastery;
    private boolean yagaHut;

    public boolean isShrine() {
        return shrine;
    }

    public void setShrine(boolean shrine) {
        this.shrine = shrine;
    }

    public boolean isMonastery() {
        return monastery;
    }

    public void setMonastery(boolean monastery) {
        this.monastery = monastery;
    }

    public boolean isYagaHut() {
		return yagaHut;
	}

	public void setYagaHut(boolean yagaHut) {
		this.yagaHut = yagaHut;
	}

	@Override
    public boolean isOpen() {
        Position p = getTile().getPosition();
        return getGame().getBoard().getAdjacentAndDiagonalTiles(p).size() < 8;
    }

    @Override
    public CompletableScoreContext getScoreContext() {
        return new CloisterScoreContext(getGame());
    }

    @Override
    public PointCategory getPointCategory() {
        return PointCategory.CLOISTER;
    }

    public static String name() {
        return _("Cloister");
    }

}
