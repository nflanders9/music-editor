package cs3500.music.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cs3500.music.model.MusicEditorModel;
import cs3500.music.model.Pitch;
import cs3500.music.model.Playable;

/**
 * Represents a view for a music editor that displays information in a static
 * vertical text representation
 */
public class ConsoleView implements View {
  /**
   * Represents the model to be rendered
   */
  private MusicEditorModel model;

  /**
   * Constructs a ConsoleView with the given MusicEditorModel
   * @param model the MusicEditorModel to display
   * @throws NullPointerException if the given model is null
   */
  public ConsoleView(MusicEditorModel model) {
    Objects.requireNonNull(model);
    this.model = model;
  }

  /**
   * Return a string representation of the contents of this ConsoleView's model
   * @return  string representation of the contents of this ConsoleView's model
   */
  /*
  private String buildString() {
    int length = this.getLength();
    if (length == 0) {
      return "";
    }

    // find the highest and lowest notes in this Song
    Playable lowest = null;
    Playable highest = null;
    for (List<Playable> row : this.notes.values()) {
      for (Playable playable : row) {
        if (lowest == null || playable.compareTo(lowest) < 0) {
          lowest = playable;
        }
        else if (highest == null || playable.compareTo(highest) > 0) {
          highest = playable;
        }
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
    for (List<Playable> row : this.notes.values()) {
      for (Playable playable : row) {
        int startBeat = playable.getStartBeat();
        int endBeat = startBeat + playable.getDuration();

        // finds the column to print the symbol in
        int noteIndex = Pitch.values().length * (playable.getOctave() - lowestOctave) +
                (playable.getPitch().ordinal() - lowestPitch.ordinal());

        // iterate through every beat that the note sustains for and add the appropriate symbol
        for (int beat = startBeat; beat < endBeat; beat++) {
          String symbol = (beat == startBeat) ? "X" : "|";
          // only overwrite the symbol that is currently there if it is not an "X"
          if (output.get(beat)[noteIndex] != "X") {
            output.get(beat)[noteIndex] = symbol;
          }
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

  /**
   * Renders this View's {@link MusicEditorModel}
   */
  @Override
  public void render() {

  }
}
