package cs3500.music.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import cs3500.music.model.MusicEditorModel;
import cs3500.music.model.Playable;

/**
 * Represents a view model for use in the GUI view
 */
public class MusicEditorViewModel implements ViewModel {

  /**
   * Represents the MusicEditorModel that this ViewModel adapts to the
   * ViewModel interface
   */
  MusicEditorModel model;

  /**
   * Represents the list of currently selected Playables in this ViewModel
   */
  List<Playable> selected;

  /**
   * True if the view is currently playing the model
   */
  private boolean isPlaying;

  /**
   * Represents the current time in the musical composition
   */
  private double currentTime;

  /**
   * Constructs a new MusicEditorViewModel based on the given MusicEditorModel
   * @param model the MusicEditorModel to adapt to the ViewModel interface
   * @throws NullPointerException if the given MusicEditorModel is null
   */
  public MusicEditorViewModel(MusicEditorModel model) {
    Objects.requireNonNull(model);
    this.model = model;
    this.isPlaying = false;
    this.selected = new ArrayList<Playable>();
    this.currentTime = 0;
  }

  @Override
  public List<Playable> getSelected() {
    return this.selected;
  }


  @Override
  public void select(Playable... playables) {
    this.selected.addAll(Arrays.asList(playables));
  }

  @Override
  public boolean isPlaying() {
    return this.isPlaying;
  }

  @Override
  public void setIsPlaying(boolean isPlaying) {
    this.isPlaying = isPlaying;
  }

  @Override
  public double getCurrentTime() {
    return this.currentTime;
  }

  @Override
  public void setCurrentTime(double milliseconds) {
    this.currentTime = milliseconds;
  }


  @Override
  public void addNote(Playable note) {
    model.addNote(note);
  }


  @Override
  public void setTempo(int tempo) {
    model.setTempo(tempo);
  }


  @Override
  public int getTempo() {
    return model.getTempo();
  }

  @Override
  public void setBeatsPerMeasure(int beatsPerMeasure) {
    model.setBeatsPerMeasure(beatsPerMeasure);
  }

  @Override
  public int getBeatsPerMeasure() {
    return model.getBeatsPerMeasure();
  }


  @Override
  public List<Playable> getNotes(int beatNum) {
    return model.getNotes(beatNum);
  }

  @Override
  public boolean removeNote(Playable note) {
    return model.removeNote(note);
  }

  @Override
  public void append(MusicEditorModel song) {
    model.append(song);
  }

  @Override
  public int getLength() {
    return model.getLength();
  }


  @Override
  public void overlay(MusicEditorModel song) {
    model.overlay(song);
  }


  @Override
  public Playable getHighest() {
    return model.getHighest();
  }


  @Override
  public Playable getLowest() {
    return model.getLowest();
  }
}
