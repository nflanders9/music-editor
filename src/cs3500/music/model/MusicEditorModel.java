package cs3500.music.model;

import java.util.List;

/**
 * Represents a model for a music editor application
 */
public interface MusicEditorModel {
  /**
   * Adds a copy of the given Playable to this MusicEditorModel
   * @param note  the new Playable to be added
   */
  public void addNote(Playable note);

  /**
   * Sets the tempo of this MusicEditorModel to the given value
   * @param tempo the tempo to be set
   * @throws IllegalArgumentException if the tempo is not positive
   */
  public void setTempo(int tempo);

  /**
   * Gets the tempo of this MusicEditorModel
   * @return  the tempo of this MusicEditorModel in beats per minute
   */
  public int getTempo();

  /**
   * Sets the number of beats in one measure in this MusicEditorModel
   * @param   beatsPerMeasure
   * @throws  IllegalArgumentException if the given beats per measure is not positive
   */
  public void setBeatsPerMeasure(int beatsPerMeasure);

  /**
   * Gets the number of beats in one measure of this MusicEditorModel
   * @return  the number of beats in one measure
   */
  public int getBeatsPerMeasure();

  /**
   * Returns a list of all Playables in this MusicEditorModel that are playing at the
   * given beat number
   * @return  List of all Playables in this MusicEditorModel
   * @throws IllegalArgumentException if the given beat number is negative
   */
  public List<Playable> getNotes(int beatNum);

  /**
   * Removes the Playable that is exactly the given Playable from this MusicEditorModel
   * @param note  the Playable to be removed
   * @return boolean indicating whether the given Playable was successfully found and
   * removed from this MusicEditorModel
   * @throws NullPointerException if the given Playable is null
   */
  public boolean removeNote(Playable note);

  /**
   * Appends copies of the Playables in the given MusicEditorModel to the conclusion of
   * this MusicEditorModel. If the given MusicEditorModel is null, nothing is copied
   * @param song  the MusicEditorModel containing Playables that will be appended to the
   *              end of this MusicEditorModel maintaining their relative order and
   *              durations but changing all of their start durations by an amount
   *              equal to the length of this MusicEditorModel
   */
  public void append(MusicEditorModel song);

  /**
   * Returns the length of this MusicEditorModel as a number of beats until the last
   * Playable in this MusicEditorModel stops sounding
   * @return  the length of this MusicEditorModel
   */
  public int getLength();

  /**
   * Copies the Playables from the given MusicEditorModel to this MusicEditorModel. If the
   * given MusicEditorModel is null, nothing new is copied
   * @param song  the MusicEditorModel containing Playables that will be copied into
   *              this MusicEditorModel
   */
  public void overlay(MusicEditorModel song);

  /**
   * Returns the Playable that is the highest in the song if highest is true or the lowest
   * in the song otherwise
   * @param highest boolean selector between highest and lowest values
   * @return  Playable instance on one extreme of this song, or null if the song is empty
   */
  public Playable getExtreme(boolean highest);

  /**
   * Return a String containing the textual representation of this MusicEditorModel
   * @return the textual representation of this MusicEditorModel
   */
  public String textView();
}