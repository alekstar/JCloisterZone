package com.jcloisterzone.wsio.message;

import com.jcloisterzone.wsio.WsMessageCommand;

@WsMessageCommand("TOGGLE_CLOCK")
public class ToggleClockMessage extends AbstractWsMessage implements WsInGameMessage {

    private String gameId;
    private Integer run;

    public ToggleClockMessage() {
    }

    public ToggleClockMessage(Integer run) {
        this.run = run;
    }

    @Override
    public String getGameId() {
        return gameId;
    }

    @Override
    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public Integer getRun() {
        return run;
    }

    public void setRun(Integer run) {
        this.run = run;
    }
}
