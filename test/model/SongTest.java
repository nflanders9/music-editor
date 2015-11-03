package model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for the Song class which implements the MusicEditorModel interface
 */
public class SongTest {

  @Test
  public void testSong() {
    MusicEditorModel song = new Song();
    song.addNote(new Note(2, 4, Pitch.C, 4));
    song.addNote(new Note(8, 3, Pitch.A, 4));
    song.addNote(new Note(0, 8, Pitch.G, 4));
    song.addNote(new Note(13, 5, Pitch.Fs, 5));
    System.out.println(song);
  }
}

