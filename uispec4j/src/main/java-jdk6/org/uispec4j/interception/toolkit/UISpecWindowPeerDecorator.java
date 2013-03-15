package org.uispec4j.interception.toolkit;

import java.awt.*;

public class UISpecWindowPeerDecorator extends Empty.WindowPeeer {

    private final UISpecWindowPeer uiSpecWindowPeer;
    private final Window window;

    public UISpecWindowPeerDecorator(Window window) {
        this.uiSpecWindowPeer = new UISpecWindowPeer(window);
        this.window = window;
    }

    public void show() {
        uiSpecWindowPeer.show();
    }

    public Toolkit getToolkit() {
        return uiSpecWindowPeer.getToolkit();
    }
}