package cs3500.music.view;

import cs3500.music.model.MusicEditorModel;

/**
 * Represents an object that can be used to javafxUI a MusicEditorModel
 */
public interface View {

  /**
   * Renders this View's {@link cs3500.music.model.MusicEditorModel}
   */
  void render();

  /**
   * Sets the model that this view corresponds to
   * @param model the MusicEditorModel to use for this view
   */
  void setModel(MusicEditorModel model);
}
