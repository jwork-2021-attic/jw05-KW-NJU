package com.game.com.anish.screen;

import java.awt.event.KeyEvent;

import com.game.asciiPanel.AsciiPanel;

public interface Screen {

    public void displayOutput(AsciiPanel terminal);

    public Screen respondToUserInput(KeyEvent key);
}
