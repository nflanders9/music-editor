package model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for implementations of the MusicEditorModel
 */
public class MusicEditorModelTest {

  MusicEditorModel m0;
  MusicEditorModel m1;
  MusicEditorModel m2;
  MusicEditorModel m3;

  Note n0;
  Note n1;
  Note n2;
  Note n3;
  Note n4;
  Note n5;

  /**
   * Initialize all MusicEditorModels in this test class with
   * appropriate testing data
   */
  private void init() {
    n0 = new Note(0, 4, Pitch.C, 4);
    n1 = new Note(1, 3, Pitch.Cs, 5);
    n2 = new Note(8, 1, Pitch.A, 3);
    n3 = new Note(6, 16, Pitch.Fs, 2);
    n4 = new Note(5, 3, Pitch.G, 4, 2);
    n5 = new Note(6, 7, Pitch.B, 5, 0);

    m0 = new Song();
    m1 = new Song(new ArrayList<Note>(), 150, 4);
    m2 = new Song(Arrays.asList(n0, n1, n2, n3), 120, 2);
    m3 = new Song(Arrays.asList(n1, n2, n3, n4, n5), 120, 3);
  }

  // tests for constructor exceptions

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalTempo() {
    new Song(new ArrayList<Note>(), 0, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalBeatsPerMeasure() {
    new Song(new ArrayList<Note>(), 120, -1);
  }


  @Test
  public void testAddNote() {
    init();
    assertEquals(m0.getNotes().size(), 0);
    m0.addNote(n0);
    assertEquals(m0.getNotes().size(), 1);
    assertEquals(m0.getNotes().get(0), n0);

    assertEquals(m3.getNotes().size(), 5);
    m3.addNote(n1);
    assertEquals(m3.getNotes().size(), 6);
    //check that the node is copied and not aliased
    assertEquals(m3.getNotes().get(0), m3.getNotes().get(5));
    assert(!(m3.getNotes().get(0) == m3.getNotes().get(5)));

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
    assertEquals(m0.getNotes().size(), 0);
    assertEquals(m3.getNotes().get(0), n1);
    assertEquals(m3.getNotes().get(1), n2);
    assertEquals(m3.getNotes().get(2), n3);
    assertEquals(m3.getNotes().get(3), n4);
    assertEquals(m3.getNotes().get(4), n5);
    assertEquals(m3.getNotes().size(), 5);
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
    assertEquals(m3.getNotes().size(), 5);
    assert(m3.getNotes().contains(n1));
    assertEquals(m3.removeNote(n1), true);
    assertEquals(m3.getNotes().size(), 4);
    assert(!m3.getNotes().contains(n1));
    assertEquals(m3.removeNote(n0), false);
    assertEquals(m3.getNotes().size(), 4);
  }

  @Test
  public void testAppend() {
    init();
    m0.append(m0);
    assertEquals(m0.getNotes().size(), 0);
    m3.append(m3);
    assertEquals(m3.getNotes().size(), 10);
    // ensure that only the note's start beat was changed
    assertNotEquals(m3.getNotes().get(0), m3.getNotes().get(5));
    m3.getNotes().get(0).setStart(m3.getNotes().get(5).getStartBeat());
    assertEquals(m3.getNotes().get(0), m3.getNotes().get(5));

    assertEquals(m2.getNotes().size(), 4);
    m2.append(m0);
    assertEquals(m2.getNotes().size(), 4);

    m2.append(null);
    assertEquals(m2.getNotes().size(), 4);
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
    assertEquals(m0.getNotes().size(), 0);
    m0.overlay(m0);
    assertEquals(m0.getNotes().size(), 0);

    m0.overlay(m3);
    assertEquals(m0.getNotes().size(), 5);
    m0.overlay(m3);
    assertEquals(m0.getNotes().size(), 10);
    // the first and fifth notes should be identical
    assertEquals(m0.getNotes().get(0), m0.getNotes().get(5));

    m3.overlay(m2);
    assertEquals(m3.getNotes(), Arrays.asList(n1, n2, n3, n4, n5, n0, n1, n2, n3));
    m3.overlay(null);
    assertEquals(m3.getNotes(), Arrays.asList(n1, n2, n3, n4, n5, n0, n1, n2, n3));
    // ensure that copies of the notes were made instead of aliasing
    assertEquals(m3.getNotes().get(5), m2.getNotes().get(0));
    assert(!(m3.getNotes().get(5) == m2.getNotes().get(0)));
  }

  @Test
  public void testTextView() {
    init();
    assertEquals(m0.textView(), "");
    m0.addNote(new Note(0, 4, Pitch.C, 4));
    m0.addNote(new Note(0, 4, Pitch.E, 4));
    m0.addNote(new Note(0, 4, Pitch.G, 4));
    m0.addNote(new Note(2, 4, Pitch.D, 4));
    m0.addNote(new Note(4, 2, Pitch.Gs, 4));
    m0.addNote(new Note(2, 4, Pitch.B, 4));
    assertEquals(m0.textView(),
            "Beat   C4  C#4   D4  D#4   E4   F4  F#4   G4  G#4   A4  A#4   B4 \n" +
                    "   0   X                   X              X                      \n" +
                    "   1   |                   |              |                      \n" +
                    "   2   |         X         |              |                   X  \n" +
                    "   3   |         |         |              |                   |  \n" +
                    "   4             |                             X              |  \n" +
                    "   5             |                             |              |  \n" +
                    "   6                                                             \n");

  }
}