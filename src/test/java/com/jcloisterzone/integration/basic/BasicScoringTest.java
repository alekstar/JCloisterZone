package com.jcloisterzone.integration.basic;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.jcloisterzone.PlayerScore;
import com.jcloisterzone.PointCategory;
import com.jcloisterzone.game.state.GameState;
import com.jcloisterzone.integration.IntegrationTest;

import io.vavr.collection.Array;

public class BasicScoringTest extends IntegrationTest {

    /**
     *  - road, city (both completed and final) scoring
     *  - farm scoring
     */
    @Test
    public void testBasicScoring() {
        GameState state = createGameState("saved-games/basic/scoring.jcz");

        Array<PlayerScore> score = state.getPlayers().getScore();
        PlayerScore alice = score.get(0);
        PlayerScore bob = score.get(1);

        assertEquals(14, alice.getPoints());
        assertEquals(6, alice.getStats().get(PointCategory.ROAD).getOrElse(0).intValue());
        assertEquals(0, alice.getStats().get(PointCategory.CITY).getOrElse(0).intValue());
        assertEquals(5, alice.getStats().get(PointCategory.CLOISTER).getOrElse(0).intValue());
        assertEquals(3, alice.getStats().get(PointCategory.FARM).getOrElse(0).intValue());

        assertEquals(11, bob.getPoints());
        assertEquals(0, bob.getStats().get(PointCategory.ROAD).getOrElse(0).intValue());
        assertEquals(11, bob.getStats().get(PointCategory.CITY).getOrElse(0).intValue());
        assertEquals(0, bob.getStats().get(PointCategory.CLOISTER).getOrElse(0).intValue());
        assertEquals(0, bob.getStats().get(PointCategory.FARM).getOrElse(0).intValue());
    }

    /**
     * 	- features (road and city) with two parts on same tiles (should be score for one points only)
     *  - completed cloister
     *  - if two followers are on one feature - player still gets points once
     */
    @Test
    public void testScoringMultitileFeatures() {
        GameState state = createGameState("saved-games/basic/scoringMultiTiles.jcz");

        Array<PlayerScore> score = state.getPlayers().getScore();
        PlayerScore alice = score.get(0);
        PlayerScore bob = score.get(1);

        assertEquals(28, alice.getPoints());
        assertEquals(0, alice.getStats().get(PointCategory.ROAD).getOrElse(0).intValue());
        assertEquals(28, alice.getStats().get(PointCategory.CITY).getOrElse(0).intValue());
        assertEquals(0, alice.getStats().get(PointCategory.CLOISTER).getOrElse(0).intValue());
        assertEquals(0, alice.getStats().get(PointCategory.FARM).getOrElse(0).intValue());

        assertEquals(13, bob.getPoints());
        assertEquals(4, bob.getStats().get(PointCategory.ROAD).getOrElse(0).intValue());
        assertEquals(0, bob.getStats().get(PointCategory.CITY).getOrElse(0).intValue());
        assertEquals(9, bob.getStats().get(PointCategory.CLOISTER).getOrElse(0).intValue());
        assertEquals(0, bob.getStats().get(PointCategory.FARM).getOrElse(0).intValue());
    }

    /**
     * 	- farm scoring, one city on multiple farms
     *  - tie on one farm
     */
    @Test
    public void testFarmScoring() {
        GameState state = createGameState("saved-games/basic/scoringFarms.jcz");

        Array<PlayerScore> score = state.getPlayers().getScore();
        PlayerScore alice = score.get(0);
        PlayerScore bob = score.get(1);

        assertEquals(9, alice.getPoints());
        assertEquals(0, alice.getStats().get(PointCategory.ROAD).getOrElse(0).intValue());
        assertEquals(0, alice.getStats().get(PointCategory.CITY).getOrElse(0).intValue());
        assertEquals(0, alice.getStats().get(PointCategory.CLOISTER).getOrElse(0).intValue());
        assertEquals(9, alice.getStats().get(PointCategory.FARM).getOrElse(0).intValue());

        assertEquals(15, bob.getPoints());
        assertEquals(0, bob.getStats().get(PointCategory.ROAD).getOrElse(0).intValue());
        assertEquals(0, bob.getStats().get(PointCategory.CITY).getOrElse(0).intValue());
        assertEquals(0, bob.getStats().get(PointCategory.CLOISTER).getOrElse(0).intValue());
        assertEquals(15, bob.getStats().get(PointCategory.FARM).getOrElse(0).intValue());
    }

}
