package cs3500.music.tests;

import org.junit.Test;


import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import cs3500.music.model.MusicEditorModel;
import cs3500.music.model.Note;
import cs3500.music.model.Pitch;
import cs3500.music.model.Playable;
import cs3500.music.model.Song;
import cs3500.music.view.ConsoleView;
import cs3500.music.view.View;
import static org.junit.Assert.*;

/**
 * Tests for the console view
 */
public class ConsoleViewTest {

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
  OutputStream mockOutput;

  /**
   * initializes data for testing
   */
  public void init() {
    capture = new StringBuilder();
    mockOutput = new OutputStream() {
      @Override
      public void write(int b) throws IOException {
        capture.append((char) b);
      }
    };

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
  public void testConsoleView() throws Exception {
    init();
    View view0 = new ConsoleView(empty, new PrintStream(mockOutput));
    view0.render();
    System.out.println(capture.toString());
    assertEquals(capture.toString(), "");

    init();
    View view1 = new ConsoleView(song, new PrintStream(mockOutput));
    view1.render();
    assertEquals(capture.toString(),
            "  F#2 G2G#2 A2A#2 B2 C3C#3 D3D#3 E3 F3F#3 G3G#3 A3A#3 B3 C4C#4 D4D#4 E4 F4F#4" +
                    " G4G#4 A4A#4 B4 C5C#5 D5D#5 E5 F5F#5 G5G#5 A5A#5 B5\n" +
                    " 0                                                       X             " +
                    "                                                         \n" +
                    " 1                                                       |              " +
                    "                        X                               \n" +
                    " 2                                                       |             " +
                    "                         |                               \n" +
                    " 3                                                       |             " +
                    "                         |                               \n" +
                    " 4                                                                     " +
                    "                                                         \n" +
                    " 5                                                                    " +
                    "        X                                                 \n" +
                    " 6 X                                                                   " +
                    "       |                                               X \n" +
                    " 7 |                                                                     " +
                    "     |                                               | \n" +
                    " 8 |                                            X                        " +
                    "                                                     | \n" +
                    " 9 |                                                                    " +
                    "                                                      | \n" +
                    "10 |                                                                     " +
                    "                                                     | \n" +
                    "11 |                                                                     " +
                    "                                                     | \n" +
                    "12 |                                                                    " +
                    "                                                      | \n" +
                    "13 |                                                                    " +
                    "                                                        \n" +
                    "14 |                                                                     " +
                    "                                                       \n" +
                    "15 |                                                                    " +
                    "                                                        \n" +
                    "16 |                                                                     " +
                    "                                                       \n" +
                    "17 |                                                                     " +
                    "                                                       \n" +
                    "18 |                                                                    " +
                    "                                                        \n" +
                    "19 |                                                                      " +
                    "                                                      \n" +
                    "20 |                                                                     " +
                    "                                                       \n" +
                    "21 |                                                                     " +
                    "                                                       \n");

    init();
    View view2 = new ConsoleView(chord, new PrintStream(mockOutput));
    view2.render();
    assertEquals(capture.toString(),
                    "  C4C#4 D4D#4 E4 F4F#4 G4\n" +
                    "0 X           X        X \n" +
                    "1 |           |        | \n" +
                    "2 |           |        | \n" +
                    "3 |           |        | \n");


    init();
    View view3 = new ConsoleView(arpeggio, new PrintStream(mockOutput));
    view3.render();
    assertEquals(capture.toString(),
                    "  C4C#4 D4D#4 E4 F4F#4 G4\n" +
                    "0 X                      \n" +
                    "1 |                      \n" +
                    "2 |           X          \n" +
                    "3 |           |          \n" +
                    "4             |        X \n" +
                    "5             |        | \n" +
                    "6                      | \n" +
                    "7                      | \n");
  }
}