package cs3500.music.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;

import cs3500.music.view.GuiView;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;


/**
 * Represents a Controller for the GUIView of the music editor
 */
public class GUIController implements Controller {

  /**
   * Represents the GUI view that this controller is tied to
   */
  private GuiView view;

  /**
   * Represents the KeyboardHandler with actions to call from this
   * class
   */
  private final KeyboardHandler keyHandler;

  /**
   * Constructs a new GUIController with new key and mouse handlers
   */
  public GUIController(GuiView view) {
    this.keyHandler = new KeyboardHandler();
    this.view = view;
  }

  /**
   * Constructs a new GUIController with the given handlers
   * @param keyHandler
   */
  public GUIController(GuiView view, KeyboardHandler keyHandler) {
    this.keyHandler = keyHandler;
    this.view = view;
  }

  @Override
  public KeyListener createKeyListener() {
    KeyboardHandler kh = new KeyboardHandler();
    kh.installKeyPressed(11, () -> {
      Timeline playTimeline;
      if (view.getViewModel().isPlaying()) {
        view.getViewModel().setIsPlaying(false);
        playTimeline = view.getTimeline();
        playTimeline.stop();
      }
      else {
        view.getViewModel().setIsPlaying(true);
        playTimeline = view.play();
      }
    });
    return kh;
  }

  @Override
  public void handleKey(KeyEvent event) {
    System.out.println(event.getKeyCode());
    if (event.getID() == KeyEvent.KEY_PRESSED) {
      keyHandler.keyPressed(event);
    }
    else if (event.getID() == KeyEvent.KEY_RELEASED) {
      keyHandler.keyReleased(event);
    }
    else if (event.getID() == KeyEvent.KEY_TYPED) {
      keyHandler.keyTyped(event);
    }
  }

  @Override
  public void handleMouse(MouseEvent event) {

  }
}
