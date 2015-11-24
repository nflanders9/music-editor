package cs3500.music.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.TreeMap;

import cs3500.music.util.CompositionBuilder;

/**
 * Represents a song to be edited in a music editor
 */
public final class Song implements MusicEditorModel {
  /**
   * Represents the Playables the comprise this song as a map where the beat number is the key and
   * the values are the lists of Playables that are either beginning or sustaining during that beat
   */
  private TreeMap<Integer, List<Playable>> notes;

  /**
   * Represents the tempo of this song in beats per minute
   */
  private int tempo;

  /**
   * Represents the number of beats in on measure
   */
  private int beatsPerMeasure;

  /**
   * Construct an empty Song with a default tempo of 120 bpm
   */
  public Song() {
    this.notes = new TreeMap<Integer, List<Playable>>();
    this.tempo = 120;
    this.beatsPerMeasure = 4;
  }

  private Song(TreeMap<Integer, List<Playable>> notes, int tempo, int beatsPerMeasure) {
    this.notes = (TreeMap<Integer, List<Playable>>) notes.clone();
    this.tempo = tempo;
    this.beatsPerMeasure = beatsPerMeasure;
  }

  /**
   * Construct a Song based on the notes in the given List of Playables
   * @param notes the list containing the Notes to be added to this Song
   * @throws IllegalArgumentException if the tempo or beatsPerMeasure are not positive
   */
  public Song(List<Playable> notes, int tempo, int beatsPerMeasure) {
    if (tempo <= 0 || beatsPerMeasure <= 0) {
      throw new IllegalArgumentException("Invalid song construction arguments");
    }
    this.notes = new TreeMap<Integer, List<Playable>>();
    for (Playable note : notes) {
      this.addNote(note);
    }
    this.tempo = tempo;
    this.beatsPerMeasure = beatsPerMeasure;
  }

  @Override
  public void addNote(Playable note) {
    int endBeat = note.getStartBeat() + note.getDuration();
    Playable copy = note.copy();
    for (int beat = note.getStartBeat(); beat < endBeat; ++ beat) {
      this.ensureInit(beat);
      this.notes.get(beat).add(copy);
    }
  }

  /**
   * Checks that the notes map containing this Song's notes at the given beat is
   * already initialized and if not, it initializes it to a new empty list of Playables
   * @param beatNum the number of the beat to ensure exists in this Song
   * @throws IllegalArgumentException if the given beat number is negative
   */
  private void ensureInit(int beatNum) {
    if (beatNum < 0) {
      throw new IllegalArgumentException("Illegal beat number");
    }
    if (!this.notes.containsKey(beatNum)) {
      this.notes.put(beatNum, new ArrayList<Playable>());
    }
  }


  @Override
  public int getTempo() {
    return this.tempo;
  }

  @Override
  public void setTempo(int tempo) {
    if (tempo <= 0) {
      throw new IllegalArgumentException("Tempo must be positive");
    }
    this.tempo = tempo;
  }

  @Override
  public int getBeatsPerMeasure() {
    return this.beatsPerMeasure;
  }

  @Override
  public void setBeatsPerMeasure(int beatsPerMeasure) {
    if (beatsPerMeasure <= 0) {
      throw new IllegalArgumentException("Invalid number of beats per measure");
    }
    this.beatsPerMeasure = beatsPerMeasure;
  }

  @Override
  public List<Playable> getNotes(int beatNum) {
    if (beatNum < 0) {
      throw new IllegalArgumentException("Illegal beat number");
    }
    // note: don't use ensureInit here as that causes lots of performance
    // issues in the GUI if it puts a large key in the map
    if (this.notes.containsKey(beatNum)) {
      return this.notes.get(beatNum);
    }
    else {
      return new ArrayList<Playable>();
    }
  }


  @Override
  public boolean removeNote(Playable playable) {
    Objects.requireNonNull(playable);
    boolean success = true;
    int endBeat = playable.getStartBeat() + playable.getDuration();
    for (int beat = playable.getStartBeat(); beat < endBeat; ++ beat) {
      this.ensureInit(beat);
      success &= this.notes.get(beat).remove(playable);
    }
    return success;
  }

  @Override
  public void append(MusicEditorModel song) {
    if (song == null) {
      return;
    }
    int offset = this.getLength();
    int otherLength = song.getLength();
    for (int beat = 0; beat < otherLength; ++ beat) {
      for (Playable note : song.getNotes(beat)) {
        // make sure to only copy notes that start at a given beat
        if (note.getStartBeat() == beat) {
          this.ensureInit(beat + offset);
          Playable newPlayable = note.copy();
          newPlayable.setStart(newPlayable.getStartBeat() + offset);
          this.addNote(newPlayable);
        }
      }
    }
  }

  @Override
  public int getLength() {
    // ensure that any lists of notes at the end of the piece are not included in the
    // length if they are empty lists
    if (this.notes.size() == 0) {
      return 0;
    }
    int lastBeat = this.notes.lastKey();
    ensureInit(lastBeat);
    while (this.notes.get(lastBeat).size() == 0) {
      -- lastBeat;
      ensureInit(lastBeat);
      if (lastBeat <= 0) {
        return 0;
      }
    }
    return lastBeat + 1;
  }

  @Override
  public void overlay(MusicEditorModel song) {
    if (song == null) {
      return;
    }
    int length = Math.max(this.getLength(), song.getLength());
    for (int beat = 0; beat < length; ++ beat) {
      for (Playable note : song.getNotes(beat)) {
        // make sure to only copy notes that start at a given beat
        if (note.getStartBeat() == beat) {
          Playable newPlayable = note.copy();
          this.ensureInit(beat);
          this.addNote(newPlayable);
        }
      }
    }
  }

  @Override
  public Playable getHighest() {
    return this.getExtremePlayable(true);
  }

  @Override
  public Playable getLowest() {
    return this.getExtremePlayable(false);
  }

  @Override
  public Playable moveNote(Playable note, int steps) {
    int startBeat = note.getStartBeat();
    if (startBeat + steps < 0) {
      throw new IllegalArgumentException("invalid number of steps");
    }

    int duration = note.getDuration();

    boolean found = false;
    for (int i = startBeat; i < startBeat + duration; i ++) {
      found |= this.notes.get(i).remove(note);
    }
    if (!found) {
      throw new IllegalArgumentException("note does not exist in the music editor model");
    }
    note.setStart(note.getStartBeat() + steps);
    for (int i = startBeat + steps; i < startBeat + steps + duration; i ++ ) {
      ensureInit(i);
      this.notes.get(i).add(note);
    }
    return note;
  }

  /**
   * Returns the Playable that is the highest in the song if highest is true or the lowest
   * in the song otherwise
   * @param highest boolean selector between highest and lowest values
   * @return  Playable instance on one extreme of this song, or null if the song is empty
   */
  public Playable getExtremePlayable(boolean highest) {
    Playable output = null;
    for (int beat = 0; beat < this.getLength(); ++ beat) {
      for (Playable note : this.getNotes(beat)) {
        // updates the output if the current note is on the appropriate side of
        // the current output based whether we're looking for the highest or lowest
        if (output == null || (highest == (note.compareTo(output) >= 0))) {
          output = note.copy();
        }
      }
    }
    return output;
  }

  /**
   * Return a builder for Songs
   * @return  a Builder for Songs
   */
  public static Builder builder() {
    return new Builder();
  }
  /**
   * Represents a builder class for Song objects
   */
  public static final class Builder implements CompositionBuilder<MusicEditorModel> {

    /**
     * Represents the Playables the comprise this song as a map where the beat number is the
     * key and the values are the lists of Playables that are either beginning or sustaining
     * during that beat
     */
    private List<Playable> notes;

    /**
     * Represents the tempo of this song in beats per minute
     */
    private int tempo;

    /**
     * Represents the number of beats in on measure
     */
    private int beatsPerMeasure;

    /**
     * Constructs a new Song Builder object
     */
    public Builder() {
      this.notes = new ArrayList<Playable>();
      this.tempo = 120;
      this.beatsPerMeasure = 4;
    }

    /**
     * Constructs an actual composition, given the notes that have been added
     *
     * @return The new composition
     */
    @Override
    public MusicEditorModel build() {
      return new Song(this.notes, this.tempo, this.beatsPerMeasure);
    }

    /**
     * Sets the tempo of the piece
     *
     * @param tempo The speed, in microseconds per beat
     * @return This builder
     */
    @Override
    public CompositionBuilder<MusicEditorModel> setTempo(int tempo) {
      int bpm = (int) (60000000.0 / tempo);
      this.tempo = bpm;
      return this;
    }

    /**
     * Adds a new note to the piece
     *
     * @param start      The start time of the note, in beats
     * @param end        The end time of the note, in beats
     * @param instrument The instrument number (to be interpreted by MIDI)
     * @param pitch      The pitch (in the range [0, 127], where 60 represents C4,
     *                   the middle-C on a piano)
     * @param volume     The volume (in the range [0, 127])
     */
    @Override
    public CompositionBuilder<MusicEditorModel> addNote(int start, int end, int instrument,
                                                        int pitch, int volume) {
      this.notes.add(new Note(start, end - start, Pitch.pitchFromMidi(pitch),
              Pitch.octaveFromMidi(pitch), instrument, volume));
      return this;
    }
  }
}
