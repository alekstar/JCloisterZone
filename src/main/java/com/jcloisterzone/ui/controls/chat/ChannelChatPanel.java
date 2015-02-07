package com.jcloisterzone.ui.controls.chat;

import java.awt.Color;

import com.jcloisterzone.event.ChatEvent;
import com.jcloisterzone.ui.ChannelController;
import com.jcloisterzone.ui.Client;
import com.jcloisterzone.wsio.message.PostChatMessage;

public class ChannelChatPanel extends ChatPanel {

	private final ChannelController cc;

	public ChannelChatPanel(Client client, ChannelController cc) {
		super(client);
		this.cc = cc;
	}

	@Override
	protected ReceivedChatMessage createReceivedMessage(ChatEvent ev) {
		if (ev.getRemoteClient() == null) {
			return new ReceivedChatMessage(ev, "* play.jcz *", new Color(0, 140, 0));
		} else {
			boolean isMe = cc.getConnection().getSessionId().equals(ev.getRemoteClient().getSessionId());
			return new ReceivedChatMessage(ev, ev.getRemoteClient().getName(), isMe ? Color.BLUE : Color.BLACK);
		}
	}

	@Override
	protected PostChatMessage createPostChatMessage(String msg) {
		PostChatMessage pcm = new PostChatMessage(msg);
		pcm.setChannel(cc.getChannel().getName());
		return pcm;
	}

}
