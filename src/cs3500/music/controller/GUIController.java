package cs3500.music.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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
    double HORIZONTAL_SCROLL_SCALE = 0.06;
    KeyboardHandler kh = new KeyboardHandler();

    // handle space bar
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

    // handle left key
    kh.installKeyPressed(16, () -> {
      if (!view.getViewModel().isPlaying()) {
        view.getViewModel().setCurrentTime(Math.max(0,
                view.getViewModel().getCurrentTime() - HORIZONTAL_SCROLL_SCALE));
        view.render(view.getViewModel().getCurrentTime());
      }
    });

    // handle right key
    kh.installKeyPressed(18, () -> {
      if (!view.getViewModel().isPlaying()) {
        view.getViewModel().setCurrentTime(
                view.getViewModel().getCurrentTime() + HORIZONTAL_SCROLL_SCALE);
        view.render(view.getViewModel().getCurrentTime());
      }
    });

    return kh;
  }

  @Override
  public MouseListener createMouseListener() {
    MouseListener mouseListener = new MouseListener() {
      @Override
      public void mouseClicked(MouseEvent e) {
        view.mouseClick(e.getX(), e.getY());
      }

      @Override
      public void mousePressed(MouseEvent e) {

      }

      @Override
      public void mouseReleased(MouseEvent e) {

      }

      @Override
      public void mouseEntered(MouseEvent e) {

      }

      @Override
      public void mouseExited(MouseEvent e) {

      }
    };
    return mouseListener;
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
