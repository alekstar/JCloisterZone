package com.jcloisterzone.wsio.message;

import java.util.Set;

import com.jcloisterzone.Expansion;
import com.jcloisterzone.game.Capability;
import com.jcloisterzone.game.CustomRule;
import com.jcloisterzone.wsio.WsMessageCommand;

@WsMessageCommand("GAME_SETUP")
public class GameSetupMessage implements WsMessage, WsInGameMessage	 {
    private String gameId;
    private Set<CustomRule> rules;
    private Set<Expansion> expansions;
    private Set<Class<? extends Capability>> capabilityClasses;

    public GameSetupMessage(String gameId, Set<CustomRule> rules, Set<Expansion> expansions,
            Set<Class<? extends Capability>> capabilityClasses) {
        this.gameId = gameId;
        this.rules = rules;
        this.expansions = expansions;
        this.capabilityClasses = capabilityClasses;
    }

    @Override
	public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public Set<CustomRule> getRules() {
		return rules;
	}

	public void setRules(Set<CustomRule> rules) {
		this.rules = rules;
	}

	public Set<Expansion> getExpansions() {
        return expansions;
    }

    public void setExpansions(Set<Expansion> expansions) {
        this.expansions = expansions;
    }

    public Set<Class<? extends Capability>> getCapabilityClasses() {
        return capabilityClasses;
    }

    public void setCapabilityClasses(Set<Class<? extends Capability>> capabilityClasses) {
        this.capabilityClasses = capabilityClasses;
    }

}
