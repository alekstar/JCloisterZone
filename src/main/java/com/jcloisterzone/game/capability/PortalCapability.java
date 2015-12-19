package com.jcloisterzone.game.capability;

import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.jcloisterzone.XMLUtils;
import com.jcloisterzone.board.Tile;
import com.jcloisterzone.board.TileTrigger;
import com.jcloisterzone.board.pointer.FeaturePointer;
import com.jcloisterzone.event.Event;
import com.jcloisterzone.event.MeepleEvent;
import com.jcloisterzone.event.TileEvent;
import com.jcloisterzone.game.Capability;
import com.jcloisterzone.game.Game;
import com.jcloisterzone.game.SnapshotCorruptedException;

public class PortalCapability extends Capability {

    boolean portalUsed = false;

    public PortalCapability(Game game) {
        super(game);
    }

    @Override
    public Object backup() {
        return portalUsed;
    }

    @Override
    public void restore(Object data) {
        portalUsed = (Boolean) data;
    }

    @Override
    public void initTile(Tile tile, Element xml) {
        if (xml.getElementsByTagName("portal").getLength() > 0) {
            tile.setTrigger(TileTrigger.PORTAL);
        }
    }

    @Override
    public void handleEvent(Event event) {
    	if (event.isUndo() && event instanceof MeepleEvent) {
    		MeepleEvent ev = (MeepleEvent) event;
	    	if (ev.getTo() == null && game.getCurrentTile().hasTrigger(TileTrigger.PORTAL)) {
	    		portalUsed = false;
	    	}
    	}
    }


    @Override
    public void extendFollowOptions(Set<FeaturePointer> followerOptions) {
        if (getCurrentTile().hasTrigger(TileTrigger.PORTAL)) {
            if (game.getActivePlayer().hasFollower()) {
                prepareMagicPortal(followerOptions);
            }
        }
    }

    public void prepareMagicPortal(Set<FeaturePointer> followerOptions) {
        if (portalUsed) return;
        for (Tile tile : getBoard().getAllTiles()) {
            if (tile == getCurrentTile()) continue; //already contained in original followerOptions
            Set<FeaturePointer> locations = game.prepareFollowerLocations(tile, true);
            followerOptions.addAll(locations);
        }
    }

    @Override
    public void turnPartCleanUp() {
        portalUsed = false;
    }

    public boolean isPortalUsed() {
        return portalUsed;
    }

    public void setPortalUsed(boolean portalUsed) {
        this.portalUsed = portalUsed;
    }

    @Override
    public void saveToSnapshot(Document doc, Element node) {
        if (portalUsed) {
            node.setAttribute("portalUsed", "true");
        }
    }

    @Override
    public void loadFromSnapshot(Document doc, Element node) throws SnapshotCorruptedException {
        if (XMLUtils.attributeBoolValue(node, "portalUsed")) {
            portalUsed = true;
        }
    }
}
