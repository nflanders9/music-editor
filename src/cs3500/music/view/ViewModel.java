package cs3500.music.view;

import java.util.List;

import cs3500.music.model.MusicEditorModel;
import cs3500.music.model.Playable;

/**
 * Represents a bridge between a MusicEditorModel and a View which allows additional
 * state information to be tracked, such as the selected Playables
 */
public interface ViewModel extends MusicEditorModel {
  /**
   * Get the list of Playables that are currently selected
   * @return  the list of currently selected Playables
   */
  List<Playable> getSelected();

  /**
   * Selects the given Playables in this ViewModel
   */
  void select(Playable... playables);
}
