package com.jcloisterzone.wsio.message;

import com.jcloisterzone.wsio.WsMessageCommand;

@WsMessageCommand("LEAVE_SLOT")
public class LeaveSlotMessage extends AbstractWsMessage implements WsInGameMessage {

    private String gameId;
    private int number;

    public LeaveSlotMessage() {
    }

    public LeaveSlotMessage(int number) {
        this.number = number;
    }

    @Override
    public String getGameId() {
        return gameId;
    }

    @Override
    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
