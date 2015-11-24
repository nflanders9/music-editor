package cs3500.music;

import java.io.FileNotFoundException;
import java.io.FileReader;

import cs3500.music.view.CompositeView;
import cs3500.music.view.View;
import cs3500.music.model.MusicEditorModel;
import cs3500.music.model.Song;
import cs3500.music.util.MusicReader;
import cs3500.music.view.ViewFactory;
import javafx.application.Application;

/**
 * Entry point for the Music Editor program
 */
public final class MusicEditor {
  public static void main(String[] args) {
    String filename = "";
    String mode = "";
    if (args.length >= 2) {
      filename = args[0];
      mode = args[1];
    }
    else if (args.length == 1) {
      filename = args[0];
    }

    try {
      MusicEditorModel song = MusicReader.parseFile(new FileReader(filename), Song.builder());
      View view = ViewFactory.makeView(mode, song);
      if (view instanceof CompositeView) {
        Application.launch(CompositeView.class, new String[0]);
      }
      else {
        view.render(0);
      }
    }
    catch (FileNotFoundException e) {
      System.err.println("Unable to open " + filename);
    }


  }
}
