package model;

import java.util.List;

/**
 * Represents a model for a music editor application
 */
public interface MusicEditorModel {
  /**
   * Adds a new note to this MusicEditorModel
   * @param note  the new Note to be added
   */
  public void addNote(Note note);

  /**
   * Returns a list of all Notes in this MusicEditorModel
   * @return  List of all Notes in this MusicEditorModel
   */
  public List<Note> getNotes();

  /**
   * Removes the note that is exactly the given Note from this MusicEditorModel
   * @param note  the Note to be removed
   * @return boolean indicating whether the given Note was successfully found and
   * removed from this MusicEditorModel
   */
  public boolean removeNote(Note note);

  /**
   * Appends copies of the Notes in the given MusicEditorModel to the conclusion of
   * this MusicEditorModel
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
   * Copies the Notes from the given MusicEditorModel to this MusicEditorModel
   * @param song  the MusicEditorModel containing Notes that will be copied into
   *              this MusicEditorModel
   */
  public void overlay(MusicEditorModel song);

}
