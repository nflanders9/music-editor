package cs3500.music.model;

import java.util.Objects;

/**
 * Represents a musical note for use in the music editor
 */
public final class Note implements Playable {
  /**
   * Represents the beat number in the musical piece that this
   * note starts from. Not final because the note can be moved in
   * the editor.
   *
   * Invariant: startBeat >= 0
   */
  private int startBeat;
  /**
   * Represents the number of integral beats that this note lasts
   *
   * Invariant: duration >= 1
   */
  private int duration;
  /**
   * Represents the pitch of this note. Not final because this
   * note can be moved in the editor.
   */
  private Pitch pitch;

  /**
   * Represents the octave number that this Note is in
   *
   * Note:
   */
  private int octave;

  /**
   * Represents an ID for an instrument that is playing this Note
   *
   * Invariant: instrumentID cannot be less than 0
   */
  private int instrumentID;

  /**
   * Represents the volume of this Note
   */
  private int volume;

  /**
   * Constructs a Note with the default instrumentID
   * @param startBeat the beat that the note starts on
   * @param duration  the number of beats that the note lasts
   * @param pitch     the pitch of the note
   * @throws IllegalArgumentException if the given startBeat or duration
   * values are invalid
   * @throws NullPointerException if the given pitch is null
   */
  public Note(int startBeat, int duration, Pitch pitch, int octave) {
    if (startBeat < 0 || duration < 1) {
      throw new IllegalArgumentException("Invalid note duration");
    }
    this.startBeat = startBeat;
    this.duration = duration;
    this.pitch = Objects.requireNonNull(pitch);
    this.octave = octave;
    this.instrumentID = 0;
  }

  /**
   * Constructs a Note with the specified instrumentID
   * @param startBeat the beat that the note starts on
   * @param duration  the number of beats that the note lasts
   * @param pitch     the pitch of the note
   * @throws IllegalArgumentException if the given startBeat, instrumentID,
   * or duration values are invalid
   * @throws NullPointerException if the given pitch is null
   */
  public Note(int startBeat, int duration, Pitch pitch, int octave, int instrumentID, int volume) {
    this(startBeat, duration, pitch, octave);
    if (instrumentID < 0) {
      throw new IllegalArgumentException("Invalid instrument ID");
    }
    this.instrumentID = instrumentID;
    this.volume = volume;
  }

  /**
   * Constructs a Note as a copy of the given Note
   * @param other the Note to be copied
   * @throws  NullPointerException if the given Note is null
   */
  public Note(Note other) {
    Objects.requireNonNull(other);
    this.pitch = Objects.requireNonNull(other.pitch);
    this.duration = other.duration;
    this.startBeat = other.startBeat;
    this.octave = other.octave;
    this.instrumentID = other.instrumentID;
  }

  /**
   * Get the start beat of this Note
   * @return  the start beat of this Note
   */
  @Override
  public int getStartBeat() {
    return startBeat;
  }

  /**
   * Get the duration of this Note
   * @return  the duration of this Note
   */
  @Override
  public int getDuration() {
    return duration;
  }

  /**
   * Get the {@link Pitch} of this Note
   * @return  the Pitch of this Note
   */
  @Override
  public Pitch getPitch() {
    return pitch;
  }

  /**
   * Get the octave of this Note
   * @return  the octave of this Note
   */
  @Override
  public int getOctave() {
    return octave;
  }

  /**
   * Get the instrument ID of this Note
   * @return the instrument ID of this Note
   */
  @Override
  public int getInstrumentID() {
    return this.instrumentID;
  }

  /**
   * Get the volume of this Note
   * @return the volume of this Note
   */
  @Override
  public int getVolume() {
    return this.volume;
  }

  /**
   * Sets this Note's {@link Pitch} and returns a reference to this Note
   * @param pitch the new Pitch of this Note
   * @return      this Note
   * @throws NullPointerException if the given pitch is null
   */
  @Override
  public Playable setPitch(Pitch pitch) {
    this.pitch = Objects.requireNonNull(pitch);
    return this;
  }

  /**
   * Sets this Note's duration and returns a reference to this Note
   * @param duration  the new duration of this Note
   * @return          this Note
   * @throws IllegalArgumentException is the given duration is less than 1
   */
  @Override
  public Playable setDuration(int duration) {
    if (duration < 1) {
      throw new IllegalArgumentException("invalid note duration");
    }
    this.duration = duration;
    return this;
  }

  /**
   * Sets this Note's start beat and returns a reference to this Note
   * @param startBeat the new start beat of this Note
   * @return          this Note
   * @throws IllegalArgumentException if the given start beat is negative
   */
  @Override
  public Playable setStart(int startBeat) {
    if (startBeat < 0) {
      throw new IllegalArgumentException("illegal start beat");
    }
    this.startBeat = startBeat;
    return this;
  }

  /**
   * Sets this Note's octave to the given value
   * @param octave  the octave to set this note to
   * @return        this Note
   */
  @Override
  public Playable setOctave(int octave) {
    this.octave = octave;
    return this;
  }

  /**
   * Sets this Note's instrument ID and returns a reference to this Note
   * @param instrument  the new start beat of this Note
   * @return            this Note
   * @throws IllegalArgumentException if the given start beat is negative
   */
  @Override
  public Playable setInstrument(int instrument) {
    if (instrument < 0) {
      throw new IllegalArgumentException("Illegal instrument ID");
    }
    this.instrumentID = instrument;
    return this;
  }

  /**
   * Sets this Note's volume
   * @param volume  the new volume level
   * @return
   */
  @Override
  public Playable setVolume(int volume) {
    this.volume = volume;
    return this;
  }

  /**
   * Returns a copy of the given Playable
   * @return  a new copy of the given Playable
   */
  @Override
  public Playable copy() {
    return new Note(this);
  }


  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Playable) && other != null) {
      return false;
    }
    Note otherNote = (Note) other;
    return this.pitch == otherNote.pitch &&
            this.startBeat == otherNote.startBeat &&
            this.duration == otherNote.duration &&
            this.octave == otherNote.octave &&
            this.instrumentID == otherNote.instrumentID;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.pitch, this.duration,
            this.duration, this.octave, this.instrumentID);
  }
}
