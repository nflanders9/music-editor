package model;

import java.util.Objects;

/**
 * Represents a musical note for use in the music editor
 */
public final class Note {
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
   * Constructs a Note
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
  }

  /**
   * Get the start beat of this Note
   * @return  the start beat of this Note
   */
  public int getStartBeat() {
    return startBeat;
  }

  /**
   * Get the duration of this Note
   * @return  the duration of this Note
   */
  public int getDuration() {
    return duration;
  }

  /**
   * Get the {@link Pitch} of this Note
   * @return  the Pitch of this Note
   */
  public Pitch getPitch() {
    return pitch;
  }

  /**
   * Get the octave of this Note
   * @return  the octave of this Note
   */
  public int getOctave() {
    return octave;
  }

  /**
   * Sets this Note's {@link Pitch} and returns a reference to this Note
   * @param pitch the new Pitch of this Note
   * @return      this Note
   * @throws NullPointerException if the given pitch is null
   */
  public Note setPitch(Pitch pitch) {
    this.pitch = Objects.requireNonNull(pitch);
    return this;
  }

  /**
   * Sets this Note's duration and returns a reference to this Note
   * @param duration  the new duration of this Note
   * @return          this Note
   * @throws IllegalArgumentException is the given duration is less than 1
   */
  public Note setDuration(int duration) {
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
  public Note setStart(int startBeat) {
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
  public Note setOctave(int octave) {
    this.octave = octave;
    return this;
  }


  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Note) && other != null) {
      return false;
    }
    Note otherNote = (Note) other;
    return this.pitch == otherNote.pitch &&
            this.startBeat == otherNote.startBeat &&
            this.duration == otherNote.duration &&
            this.octave == otherNote.octave;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.pitch, this.duration, this.duration, this.octave);
  }

  /**
   * Return a negative value if this Note is lower than the given Note, 0 if the
   * two Notes are the same, and a positive value if this Note is higher than the
   * given Note
   * @param other the Note to compare against this Note
   * @return  a negative value if this Note is lower than the given Note, 0 if the
   * two Notes are the same, and a positive value if this Note is higher than the
   * given Note
   * @throws NullPointerException if the other Note is null
   */
  public int compareTo(Note other) {
    Objects.requireNonNull(other);
    if (this.octave <= other.octave) {
      return this.pitch.compareTo(other.pitch);
    }
    else {
      return this.octave - other.octave;
    }
  }


}
