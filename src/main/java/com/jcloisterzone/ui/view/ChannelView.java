package com.jcloisterzone.ui.view;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import com.jcloisterzone.ui.ChannelController;
import com.jcloisterzone.ui.Client;
import com.jcloisterzone.ui.MenuBar;
import com.jcloisterzone.ui.MenuBar.MenuItem;
import com.jcloisterzone.ui.controls.chat.ChatPanel;
import com.jcloisterzone.ui.panel.ChannelPanel;

public class ChannelView extends AbstractUiView {

	private final ChannelController cc;

	private ChannelPanel channelPanel;
	private ChatPanel chatPanel;

	public ChannelView(Client client, ChannelController cc) {
		super(client);
		this.cc = cc;
	}

	@Override
	public void show(Container pane, Object ctx) {
		channelPanel = new ChannelPanel(client, cc);
        pane.add(channelPanel);

        chatPanel = channelPanel.getChatPanel();
        cc.setChannelPanel(channelPanel);

        MenuBar menu = client.getJMenuBar();
        menu.setItemActionListener(MenuItem.DISCONNECT, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cc.getConnection().close();
				client.getJMenuBar().setItemEnabled(MenuItem.DISCONNECT, false);
				client.mountView(new StartView(client));
			}
		});
        menu.setItemEnabled(MenuItem.LOAD, false);
		menu.setItemEnabled(MenuItem.NEW_GAME, false);
		menu.setItemEnabled(MenuItem.CONNECT_P2P, false);
		menu.setItemEnabled(MenuItem.PLAY_ONLINE, false);
		menu.setItemEnabled(MenuItem.DISCONNECT, true);
	}

	@Override
	public void hide(UiView nextView, Object nextCtx) {
		MenuBar menu = client.getJMenuBar();
		if (!(nextView instanceof GameSetupView)) {
			menu.setItemEnabled(MenuItem.LOAD, true);
			menu.setItemEnabled(MenuItem.NEW_GAME, true);
			menu.setItemEnabled(MenuItem.CONNECT_P2P, true);
			menu.setItemEnabled(MenuItem.PLAY_ONLINE, true);
		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		if (chatPanel.getInput().hasFocus()) return false;
		if (e.getID() == KeyEvent.KEY_PRESSED) {
            if (e.getKeyChar() == '`' || e.getKeyChar() == ';') {
                e.consume();
                chatPanel.activateChat();
                return true;
            }
		}
		return false;
	}

}
