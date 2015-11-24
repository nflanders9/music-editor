package cs3500.music.tests;

import org.junit.Test;

import java.awt.event.KeyEvent;

import javax.swing.*;

import cs3500.music.controller.KeyboardHandler;

import static org.junit.Assert.*;

/**
 * Tests for the KeyboardHandler class
 */
public class KeyboardHandlerTest {

  @Test
  public void testKeyPressed() {
    // each runnable will flip one of these booleans so they should all be true by
    // the completion of the test
    final boolean[] success = {false, false, false};
    KeyboardHandler kh1 = new KeyboardHandler();

    kh1.installKeyPressed(0, () -> {
      success[0] = true;
    });

    kh1.installKeyPressed(1, () -> {
      success[1] = true;
    });

    kh1.installKeyPressed(2, () -> {
      success[2] = true;
    });

    // simulate key press with code 0
    kh1.keyPressed(new KeyEvent(new Box(0), 0, 0, 0, 0, ' '));
    assert(success[0]);
    assert(!success[1]);
    assert(!success[2]);

    // simulate key press with code 1
    kh1.keyPressed(new KeyEvent(new Box(0), 0, 0, 0, 1, ' '));
    assert(success[0]);
    assert(success[1]);
    assert(!success[2]);

    // simulate key press with code 2
    kh1.keyPressed(new KeyEvent(new Box(0), 0, 0, 0, 2, ' '));
    assert(success[0]);
    assert(success[1]);
    assert(success[2]);
  }

  @Test
  public void testKeyReleased() {
    // each runnable will flip one of these booleans so they should all be true by
    // the completion of the test
    final boolean[] success = {false, false, false};
    KeyboardHandler kh1 = new KeyboardHandler();

    kh1.installKeyReleased(0, () -> {
      success[0] = true;
    });

    kh1.installKeyReleased(1, () -> {
      success[1] = true;
    });

    kh1.installKeyReleased(2, () -> {
      success[2] = true;
    });

    // simulate key press with code 0
    kh1.keyReleased(new KeyEvent(new Box(0), 0, 0, 0, 0, ' '));
    assert(success[0]);
    assert(!success[1]);
    assert(!success[2]);

    // simulate key press with code 1
    kh1.keyReleased(new KeyEvent(new Box(0), 0, 0, 0, 1, ' '));
    assert(success[0]);
    assert(success[1]);
    assert(!success[2]);

    // simulate key press with code 2
    kh1.keyReleased(new KeyEvent(new Box(0), 0, 0, 0, 2, ' '));
    assert(success[0]);
    assert(success[1]);
    assert(success[2]);
  }

  @Test
  public void testKeyTyped() {
    // each runnable will flip one of these booleans so they should all be true by
    // the completion of the test
    final boolean[] success = {false, false, false};
    KeyboardHandler kh1 = new KeyboardHandler();

    kh1.installKeyTyped(0, () -> {
      success[0] = true;
    });

    kh1.installKeyTyped(1, () -> {
      success[1] = true;
    });

    kh1.installKeyTyped(2, () -> {
      success[2] = true;
    });

    // simulate key press with code 0
    kh1.keyTyped(new KeyEvent(new Box(0), 0, 0, 0, 0, ' '));
    assert(success[0]);
    assert(!success[1]);
    assert(!success[2]);

    // simulate key press with code 1
    kh1.keyTyped(new KeyEvent(new Box(0), 0, 0, 0, 1, ' '));
    assert(success[0]);
    assert(success[1]);
    assert(!success[2]);

    // simulate key press with code 2
    kh1.keyTyped(new KeyEvent(new Box(0), 0, 0, 0, 2, ' '));
    assert(success[0]);
    assert(success[1]);
    assert(success[2]);
  }
}