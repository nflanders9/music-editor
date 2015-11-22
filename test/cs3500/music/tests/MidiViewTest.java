package cs3500.music.tests;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;

import javax.sound.midi.Receiver;
import javax.sound.midi.Synthesizer;

import cs3500.music.model.MusicEditorModel;
import cs3500.music.model.Note;
import cs3500.music.model.Pitch;
import cs3500.music.model.Playable;
import cs3500.music.model.Song;
import cs3500.music.util.MusicReader;
import cs3500.music.view.MidiView;
import cs3500.music.view.View;

import static org.junit.Assert.*;

/**
 * Tests for the MidiView
 */
public class MidiViewTest {
  Playable n0;
  Playable n1;
  Playable n2;
  Playable n3;
  Playable n4;
  Playable n5;

  MusicEditorModel song;
  MusicEditorModel empty;
  MusicEditorModel chord;
  MusicEditorModel arpeggio;

  StringBuilder capture;
  Receiver receiver;
  Synthesizer synth;

  /**
   * initializes data for testing
   */
  public void init() {
    capture = new StringBuilder();

    n0 = new Note(0, 4, Pitch.C, 4);
    n1 = new Note(1, 3, Pitch.Cs, 5);
    n2 = new Note(8, 1, Pitch.A, 3);
    n3 = new Note(6, 16, Pitch.Fs, 2);
    n4 = new Note(5, 3, Pitch.G, 4, 2, 100);
    n5 = new Note(6, 7, Pitch.B, 5, 0, 100);

    empty = new Song();
    song = new Song(Arrays.asList(n0, n1, n2, n3, n4, n5), 120, 3);
    chord = new Song();
    chord.addNote(n0);
    chord.addNote(n0.copy().setPitch(Pitch.E));
    chord.addNote(n0.copy().setPitch(Pitch.G));

    arpeggio = new Song();
    arpeggio.addNote(n0);
    arpeggio.addNote(n0.copy().setPitch(Pitch.E).setStart(2));
    arpeggio.addNote(n0.copy().setPitch(Pitch.G).setStart(4));
  }

  @Test
  public void testMidiView() {
    init();
    MidiView view0 = new MidiView(empty, new MockSynth(capture));
    view0.playAll();
    assertEquals(capture.toString(), "");

    init();
    try {
      MusicEditorModel song = MusicReader.parseFile(
              new FileReader("mary-little-lamb.txt"),Song.builder());
      MidiView view = new MidiView(song, new MockSynth(capture));
      view.playAll();
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {

      }
    } catch (FileNotFoundException e) {
      System.err.println("Unable to open");
    }
    assertEquals(capture.toString().substring(0, 990),
                    "note 144 0 64 100\n" +
                    "note 144 0 55 100\n" +
                    "note 128 0 64 100\n" +
                    "note 144 0 62 100\n" +
                    "note 128 0 62 100\n" +
                    "note 144 0 60 100\n" +
                    "note 128 0 60 100\n" +
                    "note 128 0 55 100\n" +
                    "note 144 0 62 100\n" +
                    "note 128 0 62 100\n" +
                    "note 144 0 55 100\n" +
                    "note 144 0 64 100\n" +
                    "note 128 0 64 100\n" +
                    "note 144 0 64 100\n" +
                    "note 128 0 64 100\n" +
                    "note 144 0 64 100\n" +
                    "note 128 0 55 100\n" +
                    "note 128 0 64 100\n" +
                    "note 144 0 55 100\n" +
                    "note 144 0 62 100\n" +
                    "note 128 0 62 100\n" +
                    "note 144 0 62 100\n" +
                    "note 128 0 62 100\n" +
                    "note 144 0 62 100\n" +
                    "note 128 0 55 100\n" +
                    "note 128 0 62 100\n" +
                    "note 144 0 55 100\n" +
                    "note 144 0 64 100\n" +
                    "note 128 0 55 100\n" +
                    "note 128 0 64 100\n" +
                    "note 144 0 67 100\n" +
                    "note 128 0 67 100\n" +
                    "note 144 0 67 100\n" +
                    "note 128 0 67 100\n" +
                    "note 144 0 55 100\n" +
                    "note 144 0 64 100\n" +
                    "note 128 0 64 100\n" +
                    "note 144 0 62 100\n" +
                    "note 128 0 62 100\n" +
                    "note 144 0 60 100\n" +
                    "note 128 0 60 100\n" +
                    "note 144 0 62 100\n" +
                    "note 128 0 55 100\n" +
                    "note 128 0 62 100\n" +
                    "note 144 0 55 100\n" +
                    "note 144 0 64 100\n" +
                    "note 128 0 64 100\n" +
                    "note 144 0 64 100\n" +
                    "note 128 0 64 100\n" +
                    "note 144 0 64 100\n" +
                    "note 128 0 64 100\n" +
                    "note 144 0 64 100\n" +
                    "note 128 0 55 100\n" +
                    "note 128 0 64 100\n" +
                    "note 144 0 55 100\n");

    init();
    try {
      MusicEditorModel song = MusicReader.parseFile(
              new FileReader("mystery-1.txt"), Song.builder());
      MidiView view = new MidiView(song, new MockSynth(capture));
      view.playAll();
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {

      }
    } catch (FileNotFoundException e) {
      System.err.println("Unable to open");
    }
    assertEquals(capture.toString().substring(0, 990),
                    "note 144 0 76 100\n" +
                    "note 144 1 66 100\n" +
                    "note 144 2 50 100\n" +
                    "note 144 9 42 100\n" +
                    "note 128 0 76 100\n" +
                    "note 128 1 66 100\n" +
                    "note 128 2 50 100\n" +
                    "note 144 0 76 100\n" +
                    "note 144 1 66 100\n" +
                    "note 144 2 50 100\n" +
                    "note 128 9 42 100\n" +
                    "note 128 0 76 100\n" +
                    "note 128 1 66 100\n" +
                    "note 128 2 50 100\n" +
                    "note 144 9 42 100\n" +
                    "note 128 9 42 100\n" +
                    "note 144 0 76 100\n" +
                    "note 144 1 66 100\n" +
                    "note 144 2 50 100\n" +
                    "note 144 9 42 100\n" +
                    "note 128 0 76 100\n" +
                    "note 128 1 66 100\n" +
                    "note 128 2 50 100\n" +
                    "note 128 9 42 100\n" +
                    "note 144 0 72 100\n" +
                    "note 144 1 66 100\n" +
                    "note 144 2 50 100\n" +
                    "note 144 9 42 100\n" +
                    "note 128 0 72 100\n" +
                    "note 128 1 66 100\n" +
                    "note 128 2 50 100\n" +
                    "note 128 9 42 100\n" +
                    "note 144 0 76 100\n" +
                    "note 144 1 66 100\n" +
                    "note 144 2 50 100\n" +
                    "note 144 9 42 100\n" +
                    "note 128 0 76 100\n" +
                    "note 128 1 66 100\n" +
                    "note 128 2 50 100\n" +
                    "note 128 9 42 100\n" +
                    "note 144 0 79 100\n" +
                    "note 144 1 71 100\n" +
                    "note 144 2 67 100\n" +
                    "note 144 9 42 100\n" +
                    "note 128 0 79 100\n" +
                    "note 128 1 71 100\n" +
                    "note 128 2 67 100\n" +
                    "note 128 9 42 100\n" +
                    "note 144 9 42 100\n" +
                    "note 144 1 67 100\n" +
                    "note 144 2 55 100\n" +
                    "note 128 9 42 100\n" +
                    "note 128 1 67 100\n" +
                    "note 128 2 55 100\n" +
                    "note 144 9 42 100\n");


    init();
    try {
      MusicEditorModel song = MusicReader.parseFile(
              new FileReader("mystery-2.txt"), Song.builder());
      MidiView view = new MidiView(song, new MockSynth(capture));
      view.playAll();
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {

      }
    } catch (FileNotFoundException e) {
      System.err.println("Unable to open");
    }
    assertEquals(capture.toString().substring(0, 990),
            "note 144 0 76 100\n" +
                    "note 144 1 71 100\n" +
                    "note 144 4 40 100\n" +
                    "note 128 4 40 100\n" +
                    "note 144 4 52 100\n" +
                    "note 144 9 42 100\n" +
                    "note 128 0 76 100\n" +
                    "note 128 1 71 100\n" +
                    "note 128 4 52 100\n" +
                    "note 128 9 42 100\n" +
                    "note 144 0 71 100\n" +
                    "note 144 1 68 100\n" +
                    "note 144 4 40 100\n" +
                    "note 128 0 71 100\n" +
                    "note 128 1 68 100\n" +
                    "note 128 4 40 100\n" +
                    "note 144 0 72 100\n" +
                    "note 144 1 69 100\n" +
                    "note 144 4 52 100\n" +
                    "note 144 9 42 100\n" +
                    "note 128 0 72 100\n" +
                    "note 128 1 69 100\n" +
                    "note 128 4 52 100\n" +
                    "note 128 9 42 100\n" +
                    "note 144 0 74 100\n" +
                    "note 144 1 71 100\n" +
                    "note 144 4 40 100\n" +
                    "note 128 0 74 100\n" +
                    "note 128 4 40 100\n" +
                    "note 144 0 76 100\n" +
                    "note 128 0 76 100\n" +
                    "note 144 4 52 100\n" +
                    "note 144 9 42 100\n" +
                    "note 128 9 42 100\n" +
                    "note 144 0 74 100\n" +
                    "note 128 0 74 100\n" +
                    "note 128 1 71 100\n" +
                    "note 128 4 52 100\n" +
                    "note 144 9 42 100\n" +
                    "note 128 9 42 100\n" +
                    "note 144 0 72 100\n" +
                    "note 144 1 69 100\n" +
                    "note 144 4 40 100\n" +
                    "note 128 0 72 100\n" +
                    "note 128 1 69 100\n" +
                    "note 128 4 40 100\n" +
                    "note 144 0 71 100\n" +
                    "note 144 1 68 100\n" +
                    "note 144 4 52 100\n" +
                    "note 144 9 42 100\n" +
                    "note 128 0 71 100\n" +
                    "note 128 1 68 100\n" +
                    "note 128 4 52 100\n" +
                    "note 128 9 42 100\n" +
                    "note 144 0 69 100\n");
  }

}

