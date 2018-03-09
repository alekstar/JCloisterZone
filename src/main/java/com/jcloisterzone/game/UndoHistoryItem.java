package com.jcloisterzone.game;

import java.io.Serializable;

import com.jcloisterzone.Immutable;
import com.jcloisterzone.game.state.GameState;
import com.jcloisterzone.wsio.message.WsReplayableMessage;

import io.vavr.collection.List;

@Immutable
public class UndoHistoryItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private final GameState state;
    private final List<WsReplayableMessage> replay;

    public UndoHistoryItem(GameState state, List<WsReplayableMessage> replay) {
        this.state = state;
        this.replay = replay;
    }

    public GameState getState() {
        return state;
    }

    public List<WsReplayableMessage> getReplay() {
        return replay;
    }
}
