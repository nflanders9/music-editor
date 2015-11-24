package cs3500.music.tests;

import org.junit.Test;

import cs3500.music.model.Note;
import cs3500.music.model.Pitch;
import cs3500.music.model.Playable;

import static org.junit.Assert.*;

/**
 * Tests for methods on the Note class and the Pitch enum
 */
public class NoteTest {

  Note c4;
  Playable cs7;
  Note a12;
  Playable fs0;
  Playable gNeg2;


  /**
   * Initializes all local Notes for testing
   */
  private void init() {
    this.c4 = new Note(0, 1, Pitch.C, 4, 0, 100);
    this.cs7 = new Note(9, 4, Pitch.Cs, 7, 3, 100);
    this.a12 = new Note(13, 5, Pitch.A, 12, 1, 100);
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

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeInstrumentID() {
    new Note(1, 1, Pitch.C, 4, -1, 100);
  }

  // tests the copy constructor
  @Test
  public void testCopyConstructor() {
    init();

    Playable new1 = new Note(this.a12);
    assertEquals(new1, this.a12);
    this.a12.setStart(99);
    assertNotEquals(new1, this.a12);

    Playable new2 = new Note(this.c4);
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
  public void testGetInstrument() {
    init();
    assertEquals(this.a12.getInstrumentID(), 1);
    assertEquals(this.c4.getInstrumentID(), 0);
    assertEquals(this.cs7.getInstrumentID(), 3);
    assertEquals(this.fs0.getInstrumentID(), 0);
    assertEquals(this.gNeg2.getInstrumentID(), 0);
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

  @Test
  public void testSetInstrument() {
    init();
    this.c4.setInstrument(5);
    assertEquals(this.c4.getInstrumentID(), 5);
    this.c4.setInstrument(0);
    assertEquals(this.c4.getInstrumentID(), 0);

    this.cs7.setInstrument(3);
    assertEquals(this.cs7.getInstrumentID(), 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalSetInstrument() {
    init();
    this.c4.setInstrument(-1);
  }

  // tests for equality and hash code methods

  @Test
  public void testEquals() {
    init();
    Playable newPlayable = new Note(0, 1, Pitch.C, 4);
    assert(this.c4.equals(newPlayable) && !(newPlayable == this.c4));
    assert(!this.c4.equals(new Note(1, 1, Pitch.C, 4)));
    assert(!this.c4.equals(new Note(0, 2, Pitch.C, 4)));
    assert(!this.c4.equals(new Note(0, 1, Pitch.Cs, 4)));
    assert(!this.c4.equals(new Note(1, 1, Pitch.C, 5)));
    assert(!this.c4.equals(new Note(11, 13, Pitch.Cs, 5)));
    assert(!this.c4.equals(new Note(11, 13, Pitch.Cs, 5, 6, 100)));
  }

  @Test
  public void testHashCode() {
    init();
    Playable newPlayable = new Note(this.c4);
    assert(newPlayable.equals(this.c4) && newPlayable.hashCode() == this.c4.hashCode());
  }

  @Test
  public void testCompareTo() {
    init();
    assertEquals(this.c4.compareTo(this.c4), 0);
    assertEquals(this.c4.compareTo(this.a12), -105);
    assertEquals(this.a12.compareTo(this.c4), 105);
    assertEquals(this.gNeg2.compareTo(this.cs7), -102);
  }

  @Test
  public void testPitchToString() {
    assertEquals(Pitch.A.toString(), "A");
    assertEquals(Pitch.C.toString(), "C");
    assertEquals(Pitch.Cs.toString(), "C#");
    assertEquals(Pitch.Gs.toString(), "G#");
  }

  @Test
  public void testPitchDistance() {
    assertEquals(Pitch.distance(Pitch.C, 4, Pitch.C, 4), 0);
    assertEquals(Pitch.distance(Pitch.C, 4, Pitch.C, 5), 12);
    assertEquals(Pitch.distance(Pitch.B, 4, Pitch.C, 4), -11);
    assertEquals(Pitch.distance(Pitch.B, 3, Pitch.C, 4), 1);
    assertEquals(Pitch.distance(Pitch.As, 4, Pitch.A, 7), 35);
  }

  @Test
  public void testMidiFunctions() {
    init();
    assertEquals(Pitch.getMidi(Pitch.C, 4), 60);
    assertEquals(Pitch.getMidi(Pitch.Cs, 5), 73);
    assertEquals(Pitch.octaveFromMidi(60), 4);
    assertEquals(Pitch.octaveFromMidi(59), 3);
    assertEquals(Pitch.octaveFromMidi(49), 3);
    assertEquals(Pitch.octaveFromMidi(71), 4);
    assertEquals(Pitch.pitchFromMidi(60), Pitch.C);
    assertEquals(Pitch.pitchFromMidi(61), Pitch.Cs);
    assertEquals(Pitch.pitchFromMidi(59), Pitch.B);
    assertEquals(Pitch.pitchFromMidi(72), Pitch.C);
  }
}