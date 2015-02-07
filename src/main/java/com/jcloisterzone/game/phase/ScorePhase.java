package com.jcloisterzone.game.phase;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.jcloisterzone.Player;
import com.jcloisterzone.PointCategory;
import com.jcloisterzone.board.Location;
import com.jcloisterzone.board.Position;
import com.jcloisterzone.board.Tile;
import com.jcloisterzone.board.pointer.FeaturePointer;
import com.jcloisterzone.event.FeatureCompletedEvent;
import com.jcloisterzone.event.NeutralFigureMoveEvent;
import com.jcloisterzone.event.ScoreEvent;
import com.jcloisterzone.feature.Castle;
import com.jcloisterzone.feature.City;
import com.jcloisterzone.feature.Cloister;
import com.jcloisterzone.feature.Completable;
import com.jcloisterzone.feature.Farm;
import com.jcloisterzone.feature.Feature;
import com.jcloisterzone.feature.Road;
import com.jcloisterzone.feature.visitor.score.CityScoreContext;
import com.jcloisterzone.feature.visitor.score.CompletableScoreContext;
import com.jcloisterzone.feature.visitor.score.FarmScoreContext;
import com.jcloisterzone.feature.visitor.score.PositionCollectingScoreContext;
import com.jcloisterzone.figure.Barn;
import com.jcloisterzone.figure.Builder;
import com.jcloisterzone.figure.Meeple;
import com.jcloisterzone.figure.Wagon;
import com.jcloisterzone.game.Game;
import com.jcloisterzone.game.capability.BarnCapability;
import com.jcloisterzone.game.capability.BuilderCapability;
import com.jcloisterzone.game.capability.CastleCapability;
import com.jcloisterzone.game.capability.MageAndWitchCapability;
import com.jcloisterzone.game.capability.TunnelCapability;
import com.jcloisterzone.game.capability.WagonCapability;

public class ScorePhase extends Phase {

    private Set<Completable> alredyScored = new HashSet<>();

    private final BarnCapability barnCap;
    private final BuilderCapability builderCap;
    private final CastleCapability castleCap;
    private final TunnelCapability tunnelCap;
    private final WagonCapability wagonCap;
    private final MageAndWitchCapability mageWitchCap;

    public ScorePhase(Game game) {
        super(game);
        barnCap = game.getCapability(BarnCapability.class);
        builderCap = game.getCapability(BuilderCapability.class);
        tunnelCap = game.getCapability(TunnelCapability.class);
        castleCap = game.getCapability(CastleCapability.class);
        wagonCap = game.getCapability(WagonCapability.class);
        mageWitchCap = game.getCapability(MageAndWitchCapability.class);
    }

    private void scoreCompletedOnTile(Tile tile) {
        for (Feature feature : tile.getFeatures()) {
            if (feature instanceof Completable) {
                scoreCompleted((Completable) feature, true);
            }
        }
    }

    private void scoreCompletedNearAbbey(Position pos) {
        for (Entry<Location, Tile> e : getBoard().getAdjacentTilesMap(pos).entrySet()) {
            Tile tile = e.getValue();
            Feature feature = tile.getFeaturePartOf(e.getKey().rev());
            if (feature instanceof Completable) {
                scoreCompleted((Completable) feature, false);
            }
        }
    }

    private void scoreFollowersOnBarnFarm(Farm farm, Map<City, CityScoreContext> cityCache) {
        FarmScoreContext ctx = farm.getScoreContext();
        ctx.setCityCache(cityCache);
        farm.walk(ctx);

        boolean hasBarn = false;
        for (Meeple m : ctx.getSpecialMeeples()) {
            if (m instanceof Barn) {
                hasBarn = true;
                break;
            }
        }
        if (hasBarn) {
            for (Player p : ctx.getMajorOwners()) {
                int points = ctx.getPointsWhenBarnIsConnected(p);
                game.scoreFeature(points, ctx, p);
            }
            for (Meeple m : ctx.getMeeples()) {
                if (!(m instanceof Barn)) {
                    undeloyMeeple(m);
                }
            }
        }
    }

    @Override
    public void enter() {
        Position pos = getTile().getPosition();

        //TODO separate event here ??? and move this code to abbey and mayor game
        if (barnCap != null) {
            Map<City, CityScoreContext> cityCache = new HashMap<>();
            for (Feature feature : getTile().getFeatures()) {
                if (feature instanceof Farm) {
                    scoreFollowersOnBarnFarm((Farm) feature, cityCache);
                }
            }
        }

        scoreCompletedOnTile(getTile());
        if (getTile().isAbbeyTile()) {
            scoreCompletedNearAbbey(pos);
        }

        if (tunnelCap != null) {
            Road r = tunnelCap.getPlacedTunnel();
            if (r != null) {
                scoreCompleted(r, true);
            }
        }

        for (Tile neighbour : getBoard().getAdjacentAndDiagonalTiles(pos)) {
            Cloister cloister = neighbour.getCloister();
            if (cloister != null) {
                scoreCompleted(cloister, false);
            }
        }

        if (castleCap != null) {
            for (Entry<Castle, Integer> entry : castleCap.getCastleScore().entrySet()) {
                scoreCastle(entry.getKey(), entry.getValue());
            }
        }

        alredyScored.clear();
        next();
    }

    protected void undeployMeeples(CompletableScoreContext ctx) {
        for (Meeple m : ctx.getMeeples()) {
            undeloyMeeple(m);
            //TODO decouple this hack
            if (ctx instanceof PositionCollectingScoreContext) {
                PositionCollectingScoreContext pctx = (PositionCollectingScoreContext) ctx;
                if (pctx.containsMage()) {
                    FeaturePointer oldPlacement = mageWitchCap.getMagePlacement();
                    mageWitchCap.setMagePlacement(null);
                    game.post(new NeutralFigureMoveEvent(NeutralFigureMoveEvent.MAGE, getActivePlayer(), oldPlacement, null));
                }
                if (pctx.containsWitch()) {
                    FeaturePointer oldPlacement = mageWitchCap.getWitchPlacement();
                    mageWitchCap.setWitchPlacement(null);
                    game.post(new NeutralFigureMoveEvent(NeutralFigureMoveEvent.WITCH, getActivePlayer(), oldPlacement, null));
                }
            }
        }
    }

    protected void undeloyMeeple(Meeple m) {
        Feature feature = m.getFeature();
        m.undeploy(false);
        if (m instanceof Wagon && wagonCap != null) {
            wagonCap.wagonScored((Wagon) m, feature);
        }
    }

    private void scoreCastle(Castle castle, int points) {
        List<Meeple> meeples = castle.getMeeples();
        if (meeples.isEmpty()) meeples = castle.getSecondFeature().getMeeples();
        Meeple m = meeples.get(0); //all meeples must share same owner
        m.getPlayer().addPoints(points, PointCategory.CASTLE);
        game.post(new ScoreEvent(m.getFeature(), points, PointCategory.CASTLE, m));
        undeloyMeeple(m);
    }

    private void scoreCompleted(Completable completable, boolean triggerBuilder) {
        CompletableScoreContext ctx = completable.getScoreContext();
        completable.walk(ctx);
        if (triggerBuilder && builderCap != null) {
            for (Meeple m : ctx.getSpecialMeeples()) {
                if (m instanceof Builder && m.getPlayer().equals(getActivePlayer())) {
                    if (!m.at(getTile().getPosition())) {
                        builderCap.useBuilder();
                    }
                    break;
                }
            }
        }
        if (ctx.isCompleted()) {
            Completable master = (Completable) ctx.getMasterFeature();
            if (!alredyScored.contains(master)) {
                alredyScored.add(master);
                game.scoreCompleted(ctx);
                game.scoreCompletableFeature(ctx);
                undeployMeeples(ctx);
                game.post(new FeatureCompletedEvent(getActivePlayer(), master, ctx));
            }
        }
    }

}
