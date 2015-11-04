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
   * Represents the tempo of this song in beats per minute
   */
  private int tempo;

  /**
   * Represents the number of beats in on measure
   */
  private int beatsPerMeasure;

  /**
   * Construct an empty Song with a default tempo of 120 bpm
   */
  public Song() {
    this.notes = new ArrayList<Note>();
    this.tempo = 120;
    this.beatsPerMeasure = 4;
  }

  /**
   * Construct a Song based on the notes in the given List of Notes
   * @param notes the list containing the Notes to be added to this Song
   * @throws IllegalArgumentException if the tempo or beatsPerMeasure are not positive
   */
  public Song(List<Note> notes, int tempo, int beatsPerMeasure) {
    if (tempo <= 0 || beatsPerMeasure <= 0) {
      throw new IllegalArgumentException("Invalid song construction arguments");
    }
    this.notes = new ArrayList<Note>();
    this.notes.addAll(notes);
    this.tempo = tempo;
    this.beatsPerMeasure = beatsPerMeasure;
  }

  @Override
  public void addNote(Note note) {
    this.notes.add(new Note(note));
  }


  @Override
  public int getTempo() {
    return this.tempo;
  }

  @Override
  public void setTempo(int tempo) {
    if (tempo <= 0) {
      throw new IllegalArgumentException("Tempo must be positive");
    }
    this.tempo = tempo;
  }

  @Override
  public int getBeatsPerMeasure() {
    return this.beatsPerMeasure;
  }

  @Override
  public void setBeatsPerMeasure(int beatsPerMeasure) {
    if (beatsPerMeasure <= 0) {
      throw new IllegalArgumentException("Invalid number of beats per measure");
    }
    this.beatsPerMeasure = beatsPerMeasure;
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
    if (song == null) {
      return;
    }
    ArrayList<Note> newNotes = new ArrayList<Note>();
    for (Note note : song.getNotes()) {
      Note newNote = new Note(note);
      newNote.setStart(newNote.getStartBeat() + this.getLength());
      newNotes.add(newNote);
    }
    this.notes.addAll(newNotes);
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
    if (song == null) {
      return;
    }
    ArrayList<Note> newNotes = new ArrayList<Note>();
    for (Note note : song.getNotes()) {
      this.notes.add(new Note(note));
    }
  }

  @Override
  public String textView() {
    return this.toString();
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
    int width = Math.abs(Pitch.distance(lowestPitch, lowestOctave,
            highestPitch, highestOctave)) + 1;

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
