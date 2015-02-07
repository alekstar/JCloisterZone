package com.jcloisterzone.ui.grid.layer;

import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jcloisterzone.action.SelectFollowerAction;
import com.jcloisterzone.board.Location;
import com.jcloisterzone.board.Position;
import com.jcloisterzone.board.Tile;
import com.jcloisterzone.board.pointer.MeeplePointer;
import com.jcloisterzone.ui.GameController;
import com.jcloisterzone.ui.dialog.AmbiguousUndeployDialog;
import com.jcloisterzone.ui.dialog.AmbiguousUndeployDialog.AmbiguousUndeployDialogEvent;
import com.jcloisterzone.ui.grid.ActionLayer;
import com.jcloisterzone.ui.grid.GridPanel;


public class FollowerAreaLayer extends AbstractAreaLayer implements ActionLayer<SelectFollowerAction> {

    private SelectFollowerAction action;

    public FollowerAreaLayer(GridPanel gridPanel, GameController gc) {
        super(gridPanel, gc);
    }

    @Override
    public void setAction(boolean active, SelectFollowerAction action) {
        this.action = action;
        setActive(active);
    }

    @Override
    public SelectFollowerAction getAction() {
        return action;
    }


    protected Map<Location, Area> prepareAreas(Tile tile, Position p) {
        Set<Location> locations = action.getLocations(p);
        if (locations == null) return null;
        //TODO remove on bridge!!!
        return getClient().getResourceManager().getFeatureAreas(tile, getSquareSize(), locations);
    }


    @Override
    protected void performAction(final Position pos, final Location loc) {
        List<MeeplePointer> pointers = new ArrayList<>();
        for (MeeplePointer mp: action.getOptions()) {
            if (mp.getPosition().equals(pos) && mp.getLocation().equals(loc)) {
                pointers.add(mp);
            }
        }
        if (pointers.size() == 1) {
            action.perform(getRmiProxy(), pointers.get(0));
            return;
        }


        new AmbiguousUndeployDialog(getClient(), pointers, new AmbiguousUndeployDialogEvent() {
            @Override
            public void meepleTypeSelected(MeeplePointer mp) {
               action.perform(getRmiProxy(), mp);
            }
        });
    }


}
