package model;

import java.util.List;
import java.util.ArrayList;

/**
 * Represents a song to be edited in a music editor
 */
public final class Song implements MusicEditorModel {
  /**
   * Represents the list of Notes that comprise this Song
   */
  private ArrayList<Note> notes;

  /**
   * Construct an empty Song
   */
  public Song() {
    this.notes = new ArrayList<Note>();
  }

  /**
   * Construct a Song based on the notes in the given List of Notes
   * @param notes the list containing the Notes to be added to this Song
   */
  public Song(List<Note> notes) {
    this.notes = new ArrayList<Note>();
    this.notes.addAll(notes);
  }

  @Override
  public void addNote(Note note) {
    this.notes.add(note);
  }

  @Override
  public List<Note> getNotes() {
    return this.notes;
  }

  @Override
  public boolean removeNote(Note note) {
    return this.notes.remove(note);
  }

  @Override
  public void append(MusicEditorModel song) {
    ArrayList<Note> newNotes = new ArrayList<Note>();
    for (Note note : song.getNotes()) {
      Note newNote = new Note(note);
      newNote.setStart(newNote.getStartBeat() + this.getLength());
      newNotes.add(newNote);
    }
  }

  @Override
  public int getLength() {
    int maxBeat = 0;
    for (Note note : this.notes) {
      maxBeat = Math.max(maxBeat, note.getStartBeat() + note.getDuration());
    }
    return maxBeat;
  }

  @Override
  public void overlay(MusicEditorModel song) {
    ArrayList<Note> newNotes = new ArrayList<Note>();
    for (Note note : song.getNotes()) {
      this.notes.add(new Note(note));
    }
  }

  @Override
  public String toString() {
    int length = this.getLength();
    if (length == 0) {
      return "";
    }

    // find the highest and lowest notes in this Song
    Note lowest = this.notes.get(0);
    Note highest = lowest;
    for (Note note : this.notes) {
      if (note.compareTo(lowest) < 0) {
        lowest = note;
      }
      else if (note.compareTo(highest) > 0) {
        highest = note;
      }
    }

    // get the pitches and octaves of the highest and lowest notes
    int lowestOctave = lowest.getOctave();
    Pitch lowestPitch = lowest.getPitch();
    int highestOctave = highest.getOctave();
    Pitch highestPitch = highest.getPitch();

    // computes the width of the console needed to display the notes in this song
    int width = Pitch.values().length * (highestOctave - lowestOctave) +
            (highestPitch.ordinal() - lowestPitch.ordinal()) + 1;

    // initialize a List of Arrays containing the characters to print for each line
    List<String[]> output = new ArrayList<String[]>();
    for (int i = 0; i <= this.getLength(); i++) {
      output.add(new String[width]);
    }


    // mark each of the arrays where a Note is with either a "X" or a "|"
    for (Note note : this.notes) {
      int startBeat = note.getStartBeat();
      int endBeat = startBeat + note.getDuration();

      // finds the column to print the symbol in
      int noteIndex = Pitch.values().length * (note.getOctave() - lowestOctave) +
              (note.getPitch().ordinal() - lowestPitch.ordinal());

      // iterate through every beat that the note sustains for and add the appropriate symbol
      for (int beat = startBeat; beat < endBeat; beat++) {
        String symbol = (beat == startBeat) ? "X" : "|";
        // only overwrite the symbol that is currently there if it is not an "X"
        if (output.get(beat)[noteIndex] != "X") {
          output.get(beat)[noteIndex] = symbol;
        }
      }
    }

    // construct the String to be returned based on the symbols in the List of Arrays
    StringBuilder outputString = new StringBuilder("Beat ");
    Pitch[] pitches = Pitch.values();

    // add every necessary label to the top of the output string
    for (int i = 0; i < width; i++) {
      String label = "";
      label += pitches[(i + lowestPitch.ordinal()) % pitches.length].toString();
      label += Integer.toString(lowestOctave + (i / pitches.length));
      // add a leading space to extend labels of length 2 to be 3 characters long
      if (label.length() == 2) {
        label = " " + label;
      }

      outputString.append(" " + label + " ");
    }
    outputString.append("\n");

    // add each beat number followed by the appropriate symbols for that row to the output string
    for (int rowIndex = 0; rowIndex < output.size(); rowIndex++) {
      String[] row = output.get(rowIndex);
      outputString.append(String.format("%4s", Integer.toString(rowIndex)) + " ");
      for (String str : row) {
        if (str == null) {
          str = " ";
        }
        outputString.append("  " + str + "  ");
      }
      outputString.append("\n");
    }
    return outputString.toString();
  }
}
