package cs3500.music.consoleUI;

import cs3500.music.model.MusicEditorModel;

/**
 * Represents an object that can be used to javafxUI a MusicEditorModel
 */
public interface View {

  /**
   * Renders this View's {@link cs3500.music.model.MusicEditorModel}
   */
  void render();
}
