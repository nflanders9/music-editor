package model;

import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.*;

/**
 * Tests for methods on the Note class
 */
public class NoteTest {

  Note c4;
  Note cs7;
  Note a12;
  Note fs0;
  Note gNeg2;


  /**
   * Initializes all local Notes for testing
   */
  private void init() {
    this.c4 = new Note(0, 1, Pitch.C, 4);
    this.cs7 = new Note(9, 4, Pitch.Cs, 7);
    this.a12 = new Note(13, 5, Pitch.A, 12);
    this.fs0 = new Note(153, 7, Pitch.Fs, 0);
    this.gNeg2 = new Note(2, 2, Pitch.G, -2);
  }

  // tests for constructor exceptions
  @Test(expected = NullPointerException.class)
  public void testNullPitch() {
    new Note(0, 1, null, 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeStartBeatPitch() {
    new Note(-1, 2, Pitch.C, 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testZeroDurationBeatPitch() {
    new Note(2, 0, Pitch.C, 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeDurationBeatPitch() {
    new Note(2, -1, Pitch.C, 5);
  }

  // tests the copy constructor
  @Test
  public void testCopyConstructor() {
    init();

    Note new1 = new Note(this.a12);
    assertEquals(new1, this.a12);
    this.a12.setStart(99);
    assertNotEquals(new1, this.a12);

    Note new2 = new Note(this.c4);
    assertEquals(new2, this.c4);
    this.c4.setPitch(Pitch.As);
    assertNotEquals(new2, this.c4);
  }


  // tests for getters and setters

  @Test
  public void testGetStartBeat() {
    init();
    assertEquals(this.a12.getStartBeat(), 13);
    assertEquals(this.c4.getStartBeat(), 0);
    assertEquals(this.cs7.getStartBeat(), 9);
    assertEquals(this.fs0.getStartBeat(), 153);
    assertEquals(this.gNeg2.getStartBeat(), 2);
  }

  @Test
  public void testGetDuration() {
    init();
    assertEquals(this.a12.getDuration(), 5);
    assertEquals(this.c4.getDuration(), 1);
    assertEquals(this.cs7.getDuration(), 4);
    assertEquals(this.fs0.getDuration(), 7);
    assertEquals(this.gNeg2.getDuration(), 2);

  }

  @Test
  public void testGetPitch() {
    init();
    assertEquals(this.a12.getPitch(), Pitch.A);
    assertEquals(this.c4.getPitch(), Pitch.C);
    assertEquals(this.cs7.getPitch(), Pitch.Cs);
    assertEquals(this.fs0.getPitch(), Pitch.Fs);
    assertEquals(this.gNeg2.getPitch(), Pitch.G);
  }

  @Test
  public void testGetOctave() {
    init();
    assertEquals(this.a12.getOctave(), 12);
    assertEquals(this.c4.getOctave(), 4);
    assertEquals(this.cs7.getOctave(), 7);
    assertEquals(this.fs0.getOctave(), 0);
    assertEquals(this.gNeg2.getOctave(), -2);
  }


  @Test
  public void testSetPitch() {
    init();
    this.c4.setPitch(Pitch.Cs);
    assertEquals(this.c4.getPitch(), Pitch.Cs);
    this.c4.setPitch(Pitch.G);
    assertEquals(this.c4.getPitch(), Pitch.G);
    this.fs0.setPitch(Pitch.Fs);
    assertEquals(this.fs0.getPitch(), Pitch.Fs);
  }

  @Test(expected = NullPointerException.class)
  public void testSetNullPitch() {
    init();
    this.c4.setPitch(null);
  }

  @Test
  public void testSetDuration() {
    init();
    this.c4.setDuration(3);
    assertEquals(this.c4.getDuration(), 3);
    this.c4.setDuration(1000);
    assertEquals(this.c4.getDuration(), 1000);

    this.gNeg2.setDuration(2);
    assertEquals(this.gNeg2.getDuration(), 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalDurationValue0() {
    init();
    this.a12.setDuration(0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalDurationValueNegative() {
    init();
    this.a12.setDuration(-2);
  }


  @Test
  public void testSetStart() {
    init();
    this.a12.setStart(0);
    assertEquals(this.a12.getStartBeat(), 0);
    this.a12.setStart(9999);
    assertEquals(this.a12.getStartBeat(), 9999);

    this.gNeg2.setStart(2);
    assertEquals(this.gNeg2.getStartBeat(), 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalStartValue() {
    init();
    this.c4.setStart(-1);
  }


  @Test
  public void testSetOctave() {
    init();
    this.c4.setOctave(0);
    assertEquals(this.c4.getOctave(), 0);
    this.c4.setOctave(182);
    assertEquals(this.c4.getOctave(), 182);
    this.c4.setOctave(-37);
    assertEquals(this.c4.getOctave(), -37);

    this.fs0.setOctave(0);
    assertEquals(this.fs0.getOctave(), 0);
  }

  // tests for equality and hash code methods

  @Test
  public void testEquals() {
    init();
    Note newNote = new Note(0, 1, Pitch.C, 4);
    assert(this.c4.equals(newNote) && !(newNote == this.c4));
    assert(!this.c4.equals(new Note(1, 1, Pitch.C, 4)));
    assert(!this.c4.equals(new Note(0, 2, Pitch.C, 4)));
    assert(!this.c4.equals(new Note(0, 1, Pitch.Cs, 4)));
    assert(!this.c4.equals(new Note(1, 1, Pitch.C, 5)));
    assert(!this.c4.equals(new Note(11, 13, Pitch.Cs, 5)));
  }

  @Test
  public void testHashCode() {
    init();
    Note newNote = new Note(this.c4);
    assert(newNote.equals(this.c4) && newNote.hashCode() == this.c4.hashCode());
  }
}