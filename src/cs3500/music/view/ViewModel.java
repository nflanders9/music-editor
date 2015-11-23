package cs3500.music.view;

import java.awt.*;
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

  /**
   * Return true if the view is currently playing
   */
  boolean isPlaying();

  /**
   * Set whether or not the view is currently playing
   */
  void setIsPlaying(boolean isPlaying);

  /**
   * Return the current time in milliseconds of the musical composition
   */
  double getCurrentTime();

  /**
   * Set the current time in milliseconds of the musical composition
   */
  void setCurrentTime(double milliseconds);

  /**
   * Return the duration for adding new notes
   * @return  the duration for adding new notes
   */
  int getNewNoteDuration();

  /**
   * Sets the duration for new notes
   */
  void setNewNoteDuration(int duration);

  /**
   * Return the current instrument ID being edited
   */
  int getNewNoteInstrument();

  /**
   * Sets the current instrumnet ID being edited
   */
  void setNewNoteInstrument(int id);

  /**
   * Get the origin of a mouse drag event
   * @return  the origin of the mouse drag event or null if the mouse is not dragging
   */
  Point getDragOrigin();

  /**
   * Sets the origin of a mouse drag event
   * @param origin  the origin of a mouse drag event
   */
  void setDragOrigin(Point origin);

}
