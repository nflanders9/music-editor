package cs3500.music;

import java.io.FileNotFoundException;
import java.io.FileReader;

import cs3500.music.consoleUI.View;
import cs3500.music.javafxUI.MainGUI;
import cs3500.music.model.MusicEditorModel;
import cs3500.music.model.Song;
import cs3500.music.util.CompositionBuilder;
import cs3500.music.util.MusicReader;

/**
 * Entry point for the Music Editor program
 */
public final class MusicEditor {
  public static void main(String[] args) {
    String filename = "";
    String mode = "";
    boolean blankEditor = true;
    if (args.length >= 2) {
      filename = args[0];
      mode = args[1];
      blankEditor = false;
    }

    try {
      MusicEditorModel song = MusicReader.parseFile(new FileReader(filename), Song.builder());
      View view = new MainGUI(song);
      view.render();
    }
    catch (FileNotFoundException e) {
      System.err.println("Unable to open " + filename);
    }


  }
}
