package model;

import java.util.List;

/**
 * Represents a model for a music editor application
 */
public interface MusicEditorModel {
  /**
   * Adds a copy of the given note to this MusicEditorModel
   * @param note  the new Note to be added
   */
  public void addNote(Note note);

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
   * Returns a list of all Notes in this MusicEditorModel that are playing at the given beat number
   * @return  List of all Notes in this MusicEditorModel
   * @throws IllegalArgumentException if the given beat number is negative
   */
  public List<Note> getNotes(int beatNum);

  /**
   * Removes the note that is exactly the given Note from this MusicEditorModel
   * @param note  the Note to be removed
   * @return boolean indicating whether the given Note was successfully found and
   * removed from this MusicEditorModel
   * @throws NullPointerException if the given Note is null
   */
  public boolean removeNote(Note note);

  /**
   * Appends copies of the Notes in the given MusicEditorModel to the conclusion of
   * this MusicEditorModel. If the given MusicEditorModel is null, nothing is copied
   * @param song  the MusicEditorModel containing Notes that will be appended to the
   *              end of this MusicEditorModel maintaining their relative order and
   *              durations but changing all of their start durations by an amount
   *              equal to the length of this MusicEditorModel
   */
  public void append(MusicEditorModel song);

  /**
   * Returns the length of this MusicEditorModel as a number of beats until the last
   * Note in this MusicEditorModel stops sounding
   * @return  the length of this MusicEditorModel
   */
  public int getLength();

  /**
   * Copies the Notes from the given MusicEditorModel to this MusicEditorModel. If the
   * given MusicEditorModel is null, nothing new is copied
   * @param song  the MusicEditorModel containing Notes that will be copied into
   *              this MusicEditorModel
   */
  public void overlay(MusicEditorModel song);

  /**
   * Return a String containing the textual representation of this MusicEditorModel
   * @return the textual representation of this MusicEditorModel
   */
  public String textView();
}
