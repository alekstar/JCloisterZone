package com.jcloisterzone.wsio;

import com.jcloisterzone.wsio.message.WsMessage;

public interface Connection {

    public void send(WsMessage arg);

    public boolean isClosed();
    public void close();

    public void reconnect(String gameId);
    public void stopReconnecting();

    public String getSessionId();
    public String getNickname();
}
