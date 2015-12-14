package cs3500.music.view;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import cs3500.music.model.Link;
import cs3500.music.model.LinkImpl;
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
   * Represents the duration of notes to add to this model
   */
  private int newNoteDuration;

  /**
   * Represent the instrument ID of notes to add to this model
   */
  private int newNoteInstrumentID;

  /**
   * Represents the iteration number associated with the current time of this model
   */
  private int iteration;

  /**
   * Represent the list of Links contained within this ViewModel sorted by the play iteration
   */
  private List<Link> sortedLinkList;

  /**
   * Represents the beat number that new Links will start from in this ViewModel
   */
  private Integer linkStart;

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
    this.newNoteDuration = 2;
    this.newNoteInstrumentID = 1;
    this.iteration = 0;
    this.sortedLinkList = new ArrayList<Link>();
    for (int beat = 0; beat < model.getLength(); beat++) {
      this.sortedLinkList.addAll(getLinks(beat));
    }
    sortLinkList();
    this.linkStart = null;
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
  public int getNewNoteDuration() {
    return this.newNoteDuration;
  }

  @Override
  public void setNewNoteDuration(int duration) {
    this.newNoteDuration = duration;
  }

  @Override
  public int getNewNoteInstrument() {
    return this.newNoteInstrumentID;
  }

  @Override
  public void setNewNoteInstrument(int id) {
    this.newNoteInstrumentID = id;
  }

  @Override
  public int getIteration() {
    return this.iteration;
  }

  @Override
  public void setIteration(int iterationNum) {
    this.iteration = iterationNum;
  }

  @Override
  public List<Link> getAllLinks() {
    return this.sortedLinkList;
  }

  @Override
  public void resetIteration() {
    int curBeat = (int) Math.round(((currentTime / 60.0) * getTempo()));
    for (Link link : sortedLinkList) {
      if (link.getLocationBeat() >= curBeat) {
        setIteration(link.getPlayIteration());
        return;
      }
      else if (sortedLinkList.indexOf(link) == sortedLinkList.size() - 1) {
        setIteration(link.getPlayIteration() + 1);
      }
    }
  }

  @Override
  public Integer getLinkStart() {
    return linkStart;
  }

  @Override
  public void setLinkStart(Integer beat) {
    this.linkStart = beat;
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
    selected.remove(note);
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

  @Override
  public Playable moveNote(Playable note, int steps) {
    this.selected.remove(note);
    Playable newNote = model.moveNote(note, steps);
    this.selected.add(newNote);
    return newNote;
  }

  @Override
  public List<Link> getLinks(int beat) {
    return model.getLinks(beat);
  }

  @Override
  public void addLink(Link link) {
    for (Link oldLink : sortedLinkList) {
      oldLink.setPlayIteration(oldLink.getPlayIteration() + 1);
    }
    model.addLink(link);
    this.sortedLinkList.add(link);
    sortLinkList();
  }

  @Override
  public boolean removeLink(Link link) {
    this.sortedLinkList.remove(link);
    return model.removeLink(link);
  }

  /**
   * Sort this ViewModel's sortedLinkList by the play iteration
   */
  private void sortLinkList() {
    Collections.sort(this.sortedLinkList, new Comparator<Link>() {
      @Override
      public int compare(Link o1, Link o2) {
        return o1.getPlayIteration() - o2.getPlayIteration();
      }
    });
  }
}
