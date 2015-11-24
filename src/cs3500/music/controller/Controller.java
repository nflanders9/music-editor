package cs3500.music.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


/**
 * Represents a controller for a view of the music editor
 */
public interface Controller {
  /**
   * Handle the given key event based on the behavior contained within
   * this controller
   * @param event the KeyEvent to handle
   */
  void handleKey(KeyEvent event);

  /**
   * Handle the mouse event based on the behavior contained within
   * this controller
   * @param event the MouseEvent to handle
   */
  void handleMouse(MouseEvent event);

  /**
   * Creates a new KeyListener with callback actions associated with it
   * @return
   */
  KeyListener createKeyListener();

  /**
   * Creates a new MouseListener with actions associated with it
   */
  MouseListener createMouseListener();

}
