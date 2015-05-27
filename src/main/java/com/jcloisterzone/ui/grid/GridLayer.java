package com.jcloisterzone.ui.grid;

import java.awt.Graphics2D;

import com.jcloisterzone.board.Rotation;

public interface GridLayer {

    void paint(Graphics2D g2);

    void zoomChanged(int squareSize);
    void boardRotated(Rotation rotation);

    boolean isVisible();
    void onShow();
    void onHide();
}
