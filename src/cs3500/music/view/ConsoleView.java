package cs3500.music.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cs3500.music.MusicEditor;
import cs3500.music.model.MusicEditorModel;
import cs3500.music.model.Pitch;
import cs3500.music.model.Playable;

/**
 * Represents a javafxUI for a music editor that displays information in a static
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
   * Constructs a view with a null model that must be set before rendering
   */
  public ConsoleView() {
    this.model = null;
  }

  @Override
  public void setModel(MusicEditorModel model) {
    this.model = model;
  }


  /**
   * Return a string representation of the contents of this ConsoleView's model
   * @return  string representation of the contents of this ConsoleView's model
   */
  public String buildString() {
    int length = model.getLength();
    if (length == 0) {
      return "";
    }

    // find the highest and lowest notes in this Song
    Playable lowest = model.getLowest();
    Playable highest = model.getHighest();


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
    for (int i = 0; i < model.getLength(); i++) {
      output.add(new String[width]);
    }


    // mark each of the arrays where a Note is with either a "X" or a "|"
    for (int beat = 0; beat < length; ++ beat) {
      List<Playable> notes = model.getNotes(beat);
      for (Playable playable : notes) {
        int startBeat = playable.getStartBeat();
        int endBeat = startBeat + playable.getDuration();

        // finds the column to print the symbol in
        int noteIndex = Pitch.values().length * (playable.getOctave() - lowestOctave) +
                (playable.getPitch().ordinal() - lowestPitch.ordinal());

        // iterate through every beat that the note sustains for and add the appropriate symbol
        for (int noteBeat = startBeat; noteBeat < endBeat; noteBeat++) {
          String symbol = (noteBeat == startBeat) ? "X" : "|";
          // only overwrite the symbol that is currently there if it is not an "X"
          if (output.get(noteBeat)[noteIndex] != "X") {
            output.get(noteBeat)[noteIndex] = symbol;
          }
        }
      }
    }

    // find the number of spaces that each line number must take up
    int lineNumberLength = Integer.toString(model.getLength()).length();

    // construct the String to be returned based on the symbols in the List of Arrays
    StringBuilder outputString = new StringBuilder();
    outputString.append(String.format("%" + Integer.toString(lineNumberLength) + "s", ""));
    Pitch[] pitches = Pitch.values();

    // track the width of each column in order to print notes with the correct spacing
    // in case one column must be labeled with 4 characters
    List<Integer> spaces = new ArrayList<Integer>();

    // add every necessary label to the top of the output string
    for (int i = 0; i < width; i++) {
      StringBuilder label = new StringBuilder();
      Pitch pitch = pitches[(i + lowestPitch.ordinal()) % pitches.length];
      label.append(pitch.toString());
      label.append(Integer.toString(lowestOctave + (i / pitches.length)));
      // add a leading space to extend labels of length 2 to be 3 characters long
      if (label.length() == 2) {
        label.insert(0, " ");
      }
      else if (label.length() == 4) {
        spaces.add(4);
        continue;
      }
      spaces.add(3);

      outputString.append(label.toString());
    }
    outputString.append("\n");

    // add each beat number followed by the appropriate symbols for that row to the output string
    for (int rowIndex = 0; rowIndex < output.size(); rowIndex++) {
      String[] row = output.get(rowIndex);
      outputString.append(String.format("%" + Integer.toString(lineNumberLength) + "s",
              Integer.toString(rowIndex)));
      for (int col = 0; col < row.length; ++ col) {
        String str = row[col];
        if (str == null) {
          str = "";
        }
        outputString.append(this.centerString(str, spaces.get(col)));
      }
      outputString.append("\n");
    }
    return outputString.toString();
  }

  /**
   * Return a string containing the given string centered and with the given length
   * @param str     the String to be centered
   * @param length  the length of the resulting String
   * @return  a string containing the given string centered and with the given length or
   * the original string if its length is greater than or equal to the given length
   */
  private static String centerString(String str, int length) {
    int origLength = str.length();
    int trailingSpaces = (length - origLength) / 2;
    int leadingSpaces = length - trailingSpaces - origLength;
    return String.format("%" + Integer.toString(leadingSpaces) + "s", " ") +
            str + String.format("%" + Integer.toString(trailingSpaces) + "s", " ");
  }

  /**
   * Renders this View's {@link MusicEditorModel}
   */
  @Override
  public void render() {
    System.out.println(this.buildString());
  }
}
