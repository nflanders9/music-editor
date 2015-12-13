package cs3500.music.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a controller that will handle key events
 */
public class KeyboardHandler implements KeyListener {
  /**
   * Maps key event values to the Runnable function object that are associated
   * with that key typed event
   */
  private Map<Integer, Runnable> keyTyped;

  /**
   * Maps key event values to the Runnable function object that are associated
   * with that key pressed event
   */
  private Map<Integer, Runnable> keyPressed;

  /**
   * Maps key event values to the Runnable function object that are associated
   * with that key released event
   */
  private Map<Integer, Runnable> keyReleased;

  /**
   * Construct a KeyboardHandler with no {@link Runnable}s to call
   */
  public KeyboardHandler() {
    this.keyTyped = new HashMap<Integer, Runnable>();
    this.keyReleased = new HashMap<Integer, Runnable>();
    this.keyPressed = new HashMap<Integer, Runnable>();
  }

  /**
   * Installs the given Runnable as a result of the given key typed event code
   * @param keyCode   the key code to associate the Runnable with
   * @param runnable  the Runnable to call on the given key event
   * @throws  NullPointerException if the given Runnable is null
   */
  public void installKeyTyped(int keyCode, Runnable runnable) {
    Objects.requireNonNull(runnable);
    this.keyTyped.put(keyCode, runnable);
  }

  /**
   * Installs the given Runnable as a result of the given key pressed event code
   * @param keyCode   the key code to associate the Runnable with
   * @param runnable  the Runnable to call on the given key event
   * @throws  NullPointerException if the given Runnable is null
   */
  public void installKeyPressed(int keyCode, Runnable runnable) {
    Objects.requireNonNull(runnable);
    this.keyPressed.put(keyCode, runnable);
  }

  /**
   * Installs the given Runnable as a result of the given key released event code
   * @param keyCode   the key code to associate the Runnable with
   * @param runnable  the Runnable to call on the given key event
   * @throws  NullPointerException if the given Runnable is null
   */
  public void installKeyReleased(int keyCode, Runnable runnable) {
    Objects.requireNonNull(runnable);
    this.keyReleased.put(keyCode, runnable);
  }

  /**
   * Invoked when a key has been typed. See the class description for {@link KeyEvent} for a
   * definition of a key typed event.
   */
  public void keyTyped(KeyEvent e) {
    Objects.requireNonNull(e);
    if (keyTyped.containsKey(e.getKeyCode())) {
      keyTyped.get(e.getKeyCode()).run();
    }
  }

  /**
   * Invoked when a key has been pressed. See the class description for {@link KeyEvent} for a
   * definition of a key pressed event.
   */
  public void keyPressed(KeyEvent e) {
    Objects.requireNonNull(e);
    System.out.println(e.getKeyCode()); // TODO remove this when finished debugging
    if (keyPressed.containsKey(e.getKeyCode())) {
      keyPressed.get(e.getKeyCode()).run();
    }
  }

  /**
   * Invoked when a key has been released. See the class description for {@link KeyEvent} for a
   * definition of a key released event.
   */
  public void keyReleased(KeyEvent e) {
    Objects.requireNonNull(e);
    if (keyReleased.containsKey(e.getKeyCode())) {
      keyReleased.get(e.getKeyCode()).run();
    }
  }
}
