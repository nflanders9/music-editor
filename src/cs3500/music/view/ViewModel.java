package cs3500.music.view;

import com.sun.istack.internal.Nullable;

import java.lang.reflect.Array;
import java.util.List;

import cs3500.music.model.Link;
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
   * Return the current iteration number for the current time of the ViewModel
   */
  int getIteration();

  /**
   * Sets the current iteration number for the current time of the ViewModel
   * @param iterationNum  the iteration number to set this ViewModel to
   */
  void setIteration(int iterationNum);

  /**
   * Return a list of all Links in this view model
   * @return  a List of all Links in this ViewModel
   */
  List<Link> getAllLinks();

  /**
   * Resets the iteration number to the lowest possible value for the current beat
   */
  void resetIteration();

  /**
   * Get the current link start state in this ViewModel
   * @return  the current link start beat in this ViewModel
   */
  @Nullable
  Integer getLinkStart();

  /**
   * Sets the current beat that a Link will start from in this ViewModel
   * @param beat  the beat that a Link will start from in this ViewModel - can be null
   */
  void setLinkStart(Integer beat);

}
