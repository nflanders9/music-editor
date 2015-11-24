package cs3500.music.tests;

import org.junit.Test;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

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
    gNeg2 = new Note(2, 2, Pitch.G, 3);
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

  @Test
  public void testVerticalScrolling() {
    init();

    KeyEvent up = new KeyEvent(new Box(0), 0, 0, 0, 17, ' ');
    KeyEvent down = new KeyEvent(new Box(0), 0, 0, 0, 19, ' ');

    Playable oldHigh = composite.getHighBound().copy();
    Playable oldLow = composite.getLowBound().copy();

    // pan upwards twice
    keyListener.keyPressed(up);
    keyListener.keyPressed(up);
    Playable newHigh = composite.getHighBound();
    Playable newLow = composite.getLowBound();
    assertEquals(Pitch.distance(oldHigh.getPitch(), oldHigh.getOctave(),
            newHigh.getPitch(), newHigh.getOctave()), 2);
    assertEquals(Pitch.distance(oldLow.getPitch(), oldLow.getOctave(),
            newLow.getPitch(), newLow.getOctave()), 2);

    // pan downwards 3 times
    keyListener.keyPressed(down);
    keyListener.keyPressed(down);
    keyListener.keyPressed(down);
    assertEquals(Pitch.distance(oldHigh.getPitch(), oldHigh.getOctave(),
            newHigh.getPitch(), newHigh.getOctave()), -1);
    assertEquals(Pitch.distance(oldLow.getPitch(), oldLow.getOctave(),
            newLow.getPitch(), newLow.getOctave()), -1);
  }

  @Test
  public void testHomeEnd() {
    init();

    KeyEvent home = new KeyEvent(new Box(0), 0, 0, 0, 15, ' ');
    KeyEvent end = new KeyEvent(new Box(0), 0, 0, 0, 14, ' ');

    keyListener.keyPressed(end);
    assertEquals(composite.getViewModel().getCurrentTime(), 80.0, 0.001);
    keyListener.keyPressed(end);
    assertEquals(composite.getViewModel().getCurrentTime(), 80.0, 0.001);

    keyListener.keyPressed(home);
    assertEquals(composite.getViewModel().getCurrentTime(), 0.0, 0.001);
    keyListener.keyPressed(home);
    assertEquals(composite.getViewModel().getCurrentTime(), 0.0, 0.001);
  }

  @Test
  public void testSettingNewNoteLengths() {
    init();

    List<KeyEvent> keys = new ArrayList<KeyEvent>();

    // create KeyEvents for the number keys 1 through 9
    for (int i = 0; i < 9; i++) {
      keys.add(new KeyEvent(new Box(0), 0, 0, 0, 25 + i, ' '));
    }

    // test all numeric key presses in reverse order and make sure that the new note
    // duration changes appropriately
    for (int i = 8; i >= 0; i--) {
      keyListener.keyPressed(keys.get(i));
      assertEquals(composite.getViewModel().getNewNoteDuration(), i + 1);
    }
  }


  @Test
  public void testSettingNewNoteInstrumentID() {
    init();

    KeyEvent pageUp = new KeyEvent(new Box(0), 0, 0, 0, 12, ' ');
    KeyEvent pageDown = new KeyEvent(new Box(0), 0, 0, 0, 13, ' ');

    assertEquals(composite.getViewModel().getNewNoteInstrument(), 1);

    // page up 5 times
    keyListener.keyPressed(pageUp);
    keyListener.keyPressed(pageUp);
    keyListener.keyPressed(pageUp);
    keyListener.keyPressed(pageUp);
    keyListener.keyPressed(pageUp);

    assertEquals(composite.getViewModel().getNewNoteInstrument(), 6);

    // page down 3 times
    keyListener.keyPressed(pageDown);
    keyListener.keyPressed(pageDown);
    keyListener.keyPressed(pageDown);

    assertEquals(composite.getViewModel().getNewNoteInstrument(), 3);
  }

  @Test
  public void testDeletingSelectionWithDeleteKey() {
    init();

    KeyEvent delete = new KeyEvent(new Box(0), 0, 0, 0, 81, ' ');

    composite.getViewModel().select(c4, cs7, a12);
    assertEquals(composite.getViewModel().getSelected().size(), 3);
    assertEquals(composite.getViewModel().getNotes(0).get(0), c4);
    assertEquals(composite.getViewModel().getNotes(9).get(0), cs7);

    keyListener.keyPressed(delete);

    assertEquals(composite.getViewModel().getSelected().size(), 0);
    assertEquals(composite.getViewModel().getNotes(0).size(), 0);
    assertEquals(composite.getViewModel().getNotes(9).size(), 0);
  }

  @Test
  public void testTransposeSelectionVertically() {
    init();

    KeyEvent w = new KeyEvent(new Box(0), 0, 0, 0, 58, ' ');
    KeyEvent s = new KeyEvent(new Box(0), 0, 0, 0, 54, ' ');

    composite.getViewModel().select(c4, cs7);
    assertEquals(c4.getPitch(), Pitch.C);
    assertEquals(c4.getOctave(), 4);
    assertEquals(cs7.getPitch(), Pitch.Cs);
    assertEquals(cs7.getOctave(), 7);

    // transpose up 3 times
    keyListener.keyPressed(w);
    keyListener.keyPressed(w);
    keyListener.keyPressed(w);
    assertEquals(c4.getPitch(), Pitch.Ds);
    assertEquals(c4.getOctave(), 4);
    assertEquals(cs7.getPitch(), Pitch.E);
    assertEquals(cs7.getOctave(), 7);

    // transpose down 4 times
    keyListener.keyPressed(s);
    keyListener.keyPressed(s);
    keyListener.keyPressed(s);
    keyListener.keyPressed(s);
    assertEquals(c4.getPitch(), Pitch.B);
    assertEquals(c4.getOctave(), 3);
    assertEquals(cs7.getPitch(), Pitch.C);
    assertEquals(cs7.getOctave(), 7);
  }

  @Test
  public void testShiftSelectionHorizontally() {
    init();

    KeyEvent a = new KeyEvent(new Box(0), 0, 0, 0, 36, ' ');
    KeyEvent d = new KeyEvent(new Box(0), 0, 0, 0, 39, ' ');

    composite.getViewModel().select(c4, cs7);
    assertEquals(composite.getViewModel().getNotes(5).size(), 0);

    // shift right 5 times
    keyListener.keyPressed(d);
    keyListener.keyPressed(d);
    keyListener.keyPressed(d);
    keyListener.keyPressed(d);
    keyListener.keyPressed(d);
    assertEquals(composite.getViewModel().getNotes(5).get(0), c4.setStart(5));
    assertEquals(composite.getViewModel().getNotes(14).get(1), cs7.setStart(14));

    // shift left 3 times
    keyListener.keyPressed(a);
    keyListener.keyPressed(a);
    keyListener.keyPressed(a);
    assertEquals(composite.getViewModel().getNotes(2).get(1), c4.setStart(2));
    assertEquals(composite.getViewModel().getNotes(11).get(0), cs7.setStart(11));

  }

}