package model;

import java.util.List;
import java.util.ArrayList;

/**
 * Represents a song to be edited in a music editor
 */
public final class Song implements MusicEditorModel {
  /**
   * Represents the list of Notes that comprise this Song
   */
  private ArrayList<Note> notes;

  /**
   * Construct an empty Song
   */
  public Song() {
    this.notes = new ArrayList<Note>();
  }

  /**
   * Construct a Song based on the notes in the given List of Notes
   * @param notes the list containing the Notes to be added to this Song
   */
  public Song(List<Note> notes) {
    this.notes = new ArrayList<Note>();
    this.notes.addAll(notes);
  }

  @Override
  public void addNote(Note note) {
    this.notes.add(note);
  }

  @Override
  public List<Note> getNotes() {
    return this.notes;
  }

  @Override
  public boolean removeNote(Note note) {
    return this.notes.remove(note);
  }

  @Override
  public void append(MusicEditorModel song) {
    ArrayList<Note> newNotes = new ArrayList<Note>();
    for (Note note : song.getNotes()) {
      Note newNote = new Note(note);
      newNote.setStart(newNote.getStartBeat() + this.getLength());
      newNotes.add(newNote);
    }
  }

  @Override
  public int getLength() {
    int maxBeat = 0;
    for (Note note : this.notes) {
      maxBeat = Math.max(maxBeat, note.getStartBeat() + note.getDuration());
    }
    return maxBeat;
  }

  @Override
  public void overlay(MusicEditorModel song) {
    ArrayList<Note> newNotes = new ArrayList<Note>();
    for (Note note : song.getNotes()) {
      this.notes.add(new Note(note));
    }
  }
}
