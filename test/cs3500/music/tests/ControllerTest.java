package cs3500.music.tests;

import org.junit.Test;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

import javax.swing.*;

import cs3500.music.controller.Controller;
import cs3500.music.controller.GUIController;
import cs3500.music.model.MusicEditorModel;
import cs3500.music.model.Note;
import cs3500.music.model.Pitch;
import cs3500.music.model.Playable;
import cs3500.music.model.Song;
import cs3500.music.view.CompositeView;
import cs3500.music.view.GuiView;
import cs3500.music.view.MainGUI;
import cs3500.music.view.MidiView;
import cs3500.music.view.MusicEditorViewModel;
import cs3500.music.view.View;
import cs3500.music.view.ViewModel;
import javafx.animation.Timeline;

import static org.junit.Assert.*;

/**
 * Tests for Controller actions on the GUI view
 */
public class ControllerTest {

  ViewModel vm;
  Playable c4;
  Playable cs7;
  Playable a12;
  Playable fs0;
  Playable gNeg2;
  Controller c;
  GuiView gui;
  View midi;
  GuiView composite;
  KeyListener keyListener;
  MouseListener mouseListener;

  /**
   * Initialize the testing data
   */
  private void init() {
    MusicEditorModel model = new Song();
    c4 = new Note(0, 1, Pitch.C, 4, 0, 100);
    cs7 = new Note(9, 4, Pitch.Cs, 7, 3, 100);
    a12 = new Note(13, 5, Pitch.A, 12, 1, 100);
    fs0 = new Note(153, 7, Pitch.Fs, 0);
    gNeg2 = new Note(2, 2, Pitch.G, -2);
    model.addNote(c4);
    model.addNote(cs7);
    model.addNote(a12);
    model.addNote(fs0);
    model.addNote(gNeg2);
    vm = new MusicEditorViewModel(model);
    gui = new MainGUI(vm);
    midi = new MidiView(vm);
    composite = new CompositeView(gui, midi);
    c = new GUIController(composite);
    keyListener = c.createKeyListener();
    mouseListener = c.createMouseListener();
    composite.addKeyListener(keyListener);
    composite.addMouseListener(mouseListener);
  }

  @Test
  public void testPlayPause() {
    init();
    KeyEvent space = new KeyEvent(new Box(0), 0, 0, 0, 11, ' ');
    assertEquals(composite.getTimeline(), null);
    assert(!composite.getViewModel().isPlaying());

    // should begin playing on space key
    keyListener.keyPressed(space);
    Timeline t = composite.getTimeline();
    assert(composite.getViewModel().isPlaying());

    // should stop playing on space key, but the timeline should be the same
    keyListener.keyPressed(space);
    assertEquals(composite.getTimeline(), t);
    assert(!composite.getViewModel().isPlaying());

    // resume playing using a different timeline for animation
    keyListener.keyPressed(space);
    assertNotEquals(composite.getTimeline(), t);
    assert(composite.getViewModel().isPlaying());
  }

  @Test
  public void testHorizontalScrolling() {
    init();
    KeyEvent left = new KeyEvent(new Box(0), 0, 0, 0, 16, ' ');
    KeyEvent right = new KeyEvent(new Box(0), 0, 0, 0, 18, ' ');

    assert(!composite.getViewModel().isPlaying());

    // make sure that it cannot scroll past the 0 time mark
    keyListener.keyPressed(left);
    assertEquals(composite.getViewModel().getCurrentTime(), 0, 0.001);

    // scroll to the right 3 times
    keyListener.keyPressed(right);
    keyListener.keyPressed(right);
    keyListener.keyPressed(right);
    assertEquals(composite.getViewModel().getCurrentTime(), 0.18, 0.001);

    // scroll to the left 2 times
    keyListener.keyPressed(left);
    keyListener.keyPressed(left);
    assertEquals(composite.getViewModel().getCurrentTime(), 0.06, 0.001);
  }

}