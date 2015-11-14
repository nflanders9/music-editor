package cs3500.music;

import java.io.FileNotFoundException;
import java.io.FileReader;

import cs3500.music.view.View;
import cs3500.music.model.MusicEditorModel;
import cs3500.music.model.Song;
import cs3500.music.util.MusicReader;
import cs3500.music.view.ViewFactory;

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

    try {
      MusicEditorModel song = MusicReader.parseFile(new FileReader(filename), Song.builder());
      View view = ViewFactory.makeView(mode, song);
      view.render();
      try {
        Thread.sleep(song.getLength() * (60000000 / song.getTempo()));
      } catch (InterruptedException e) {

      }
    }
    catch (FileNotFoundException e) {
      System.err.println("Unable to open " + filename);
    }


  }
}
