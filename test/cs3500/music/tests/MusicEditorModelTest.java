package cs3500.music.tests;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import cs3500.music.view.MidiView;
import cs3500.music.view.View;
import cs3500.music.model.MusicEditorModel;
import cs3500.music.model.Note;
import cs3500.music.model.Pitch;
import cs3500.music.model.Playable;
import cs3500.music.model.Song;
import cs3500.music.util.MusicReader;

import static org.junit.Assert.*;

/**
 * Tests for implementations of the MusicEditorModel
 */
public class MusicEditorModelTest {

  MusicEditorModel m0;
  MusicEditorModel m1;
  MusicEditorModel m2;
  MusicEditorModel m3;

  Playable n0;
  Playable n1;
  Playable n2;
  Playable n3;
  Playable n4;
  Playable n5;

  /**
   * Initialize all MusicEditorModels in this test class with appropriate testing data
   */
  private void init() {
    n0 = new Note(0, 4, Pitch.C, 4);
    n1 = new Note(1, 3, Pitch.Cs, 5);
    n2 = new Note(8, 1, Pitch.A, 3);
    n3 = new Note(6, 16, Pitch.Fs, 2);
    n4 = new Note(5, 3, Pitch.G, 4, 2, 100);
    n5 = new Note(6, 7, Pitch.B, 5, 0, 100);

    m0 = new Song();
    m1 = new Song(new ArrayList<Playable>(), 150, 4);
    m2 = new Song(Arrays.asList(n0, n1, n2, n3), 120, 2);
    m3 = new Song(Arrays.asList(n1, n2, n3, n4, n5), 120, 3);
  }

  // tests for constructor exceptions

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalTempo() {
    new Song(new ArrayList<Playable>(), 0, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalBeatsPerMeasure() {
    new Song(new ArrayList<Playable>(), 120, -1);
  }


  @Test
  public void testAddNote() {
    init();
    assertEquals(m0.getNotes(0).size(), 0);
    m0.addNote(n0);
    assertEquals(m0.getNotes(0).size(), 1);
    assertEquals(m0.getNotes(1).size(), 1);
    assertEquals(m0.getNotes(2).size(), 1);
    assertEquals(m0.getNotes(3).size(), 1);
    assertEquals(m0.getNotes(4).size(), 0);
    assertEquals(m0.getNotes(0).get(0), n0);

    assertEquals(m3.getNotes(1).size(), 1);
    m3.addNote(n1);
    assertEquals(m3.getNotes(1).size(), 2);
    //check that the node is copied and not aliased
    assertEquals(m3.getNotes(1).get(1), n1);
    assert (!(m3.getNotes(1).get(1) == n1));

  }

  @Test
  public void testSetTempo() {
    init();
    assertEquals(m0.getTempo(), 120);
    m0.setTempo(155);
    assertEquals(m0.getTempo(), 155);
    m0.setTempo(1);
    assertEquals(m0.getTempo(), 1);

    m1.setTempo(999);
    assertEquals(m1.getTempo(), 999);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalSetTempo() {
    init();
    m0.setTempo(0);
  }

  @Test
  public void testGetTempo() {
    init();
    assertEquals(m0.getTempo(), 120);
    assertEquals(m1.getTempo(), 150);
    assertEquals(m2.getTempo(), 120);
    assertEquals(m3.getTempo(), 120);
  }


  @Test
  public void testGetNotes() {
    init();
    assertEquals(m0.getNotes(0).size(), 0);
    assert (m3.getNotes(1).contains(n1));
    assert (m3.getNotes(5).contains(n4));
    assert (m3.getNotes(6).containsAll(Arrays.asList(n3, n4, n5)));
    assert (m3.getNotes(15).contains(n3));
    assert (m3.getNotes(99).size() == 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalGetNotes() {
    init();
    m0.getNotes(-1);
  }

  @Test
  public void testSetBeatsPerMeasure() {
    init();
    assertEquals(m0.getBeatsPerMeasure(), 4);
    m0.setBeatsPerMeasure(5);
    assertEquals(m0.getBeatsPerMeasure(), 5);
    m0.setBeatsPerMeasure(1000);
    assertEquals(m0.getBeatsPerMeasure(), 1000);

    m3.setBeatsPerMeasure(3);
    assertEquals(m3.getBeatsPerMeasure(), 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalSetBeatsPerMeasure() {
    init();
    m0.setBeatsPerMeasure(0);
  }

  @Test
  public void testGetBeatsPerMeasure() {
    init();
    assertEquals(m0.getBeatsPerMeasure(), 4);
    assertEquals(m1.getBeatsPerMeasure(), 4);
    assertEquals(m2.getBeatsPerMeasure(), 2);
    assertEquals(m3.getBeatsPerMeasure(), 3);
  }

  @Test
  public void testRemoveNote() {
    init();
    assertEquals(m0.removeNote(n0), false);
    assert (m3.getNotes(1).contains(n1));
    assert (m3.getNotes(2).contains(n1));
    assert (m3.getNotes(3).contains(n1));
    assertEquals(m3.removeNote(n1), true);
    assert (!m3.getNotes(1).contains(n1));
    assert (!m3.getNotes(2).contains(n1));
    assert (!m3.getNotes(3).contains(n1));

    assert (!m3.getNotes(0).contains(n1));
    assertEquals(m3.removeNote(n0), false);
  }

  @Test
  public void testAppend() {
    init();
    m0.append(m0);
    assertEquals(m0.getNotes(0).size(), 0);

    assertEquals(m3.getLength(), 22);
    m3.append(m3);
    assertEquals(m3.getLength(), 44);

    // ensure that only the note's start beat was changed
    assertNotEquals(m3.getNotes(1).get(0), m3.getNotes(23).get(0));
    m3.getNotes(1).get(0).setStart(m3.getNotes(23).get(0).getStartBeat());
    assertEquals(m3.getNotes(1).get(0), m3.getNotes(23).get(0));

    assertEquals(m2.getNotes(5).size(), 0);
    m2.append(m3);
    assertEquals(m2.getNotes(5).size(), 0);

    assertEquals(m2.getNotes(28).size(), 3);

    assertEquals(m2.getLength(), 66);
    m2.append(m0);
    assertEquals(m2.getLength(), 66);
    m2.append(null);
    assertEquals(m2.getLength(), 66);
  }

  @Test
  public void testGetLength() {
    init();
    assertEquals(m0.getLength(), 0);
    assertEquals(m1.getLength(), 0);
    assertEquals(m2.getLength(), 22);
    assertEquals(m3.getLength(), 22);
    m0.addNote(new Note(100, 200, Pitch.C, 99));
    assertEquals(m0.getLength(), 300);
  }

  @Test
  public void testOverlay() {
    init();
    assertEquals(m0.getNotes(0).size(), 0);
    m0.overlay(m0);
    assertEquals(m0.getNotes(0).size(), 0);

    m0.overlay(m3);
    assert (m0.getNotes(8).containsAll(Arrays.asList(n3, n5, n2)));
    assertEquals(m0.getLength(), 22);
    m0.overlay(m3);
    assert (m0.getNotes(8).containsAll(Arrays.asList(n2, n3, n5, n2, n3, n5)));
    assertEquals(m0.getLength(), 22);

    m3.overlay(m2);
    assert (m3.getNotes(3).containsAll(Arrays.asList(n1, n0, n1)));
    m3.overlay(null);
    assert (m3.getNotes(3).containsAll(Arrays.asList(n1, n0, n1)));
    // ensure that copies of the notes were made instead of aliasing
    assertEquals(m3.getNotes(3).get(1), m2.getNotes(3).get(0));
    assert (!(m3.getNotes(3).get(1) == m2.getNotes(3).get(0)));
  }

  @Test
  public void testGetExtreme() {
    init();
    assertEquals(m0.getHighest(), null);
    assertEquals(m0.getLowest(), null);
    assertEquals(m2.getHighest(), n1);
    assertEquals(m2.getLowest(), n3);
    assertEquals(m3.getHighest(), n5);
    assertEquals(m3.getLowest(), n3);
  }

}