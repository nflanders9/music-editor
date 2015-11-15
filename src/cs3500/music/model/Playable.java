package cs3500.music.model;

import java.util.Objects;

/**
 * Represents an object that can be played in a music composition
 */
public interface Playable extends Comparable {
  /**
   * Get the start beat of this Playable
   * @return  the start beat of this Playable
   */
  int getStartBeat();

  /**
   * Get the duration of this Playable
   * @return  the duration of this Playable
   */
  int getDuration();

  /**
   * Get the {@link Pitch} of this Playable
   * @return  the Pitch of this Playable
   */
  Pitch getPitch();

  /**
   * Get the octave of this Playable
   * @return  the octave of this Playable
   */
  int getOctave();

  /**
   * Get the instrument ID of this Playable
   * @return the instrument ID of this Playable
   */
  int getInstrumentID();

  /**
   * Returns this Playable's volume
   * @return  this Playable's volume
   */
  int getVolume();

  /**
   * Sets this Playable's {@link Pitch} and returns a reference to this Playable
   * @param pitch the new Pitch of this Playable
   * @return      this Playable
   * @throws NullPointerException if the given pitch is null
   */
  Playable setPitch(Pitch pitch);

  /**
   * Sets this Playable's duration and returns a reference to this Playable
   * @param duration  the new duration of this Playable
   * @return          this Playable
   * @throws IllegalArgumentException is the given duration is less than 1
   */
  Playable setDuration(int duration);

  /**
   * Sets this Playable's start beat and returns a reference to this Playable
   * @param startBeat the new start beat of this Playable
   * @return          this Playable
   * @throws IllegalArgumentException if the given start beat is negative
   */
  Playable setStart(int startBeat);

  /**
   * Sets this Playable's octave to the given value
   * @param octave  the octave to set this Playable to
   * @return        this Playable
   */
  Playable setOctave(int octave);

  /**
   * Sets this Playable's instrument ID and returns a reference to this Playable
   * @param instrument  the new start beat of this Playable
   * @return            this Playable
   * @throws IllegalArgumentException if the given start beat is negative
   */
  Playable setInstrument(int instrument);

  /**
   * Sets this Playable's volume and returns a reference to this Playable
   * @param volume  the new volume level
   * @return        this Playable
   */
  Playable setVolume(int volume);

  /**
   * Returns a copy of the given Playable
   * @return  a new copy of the given Playable
   */
  Playable copy();

  /**
   * Compares this Playable with the specified object for order.  Returns a negative integer, zero,
   * or a positive integer as this object is less than, equal to, or greater than the specified
   * object.

   * @param o the object to be compared.
   * @return a negative integer, zero, or a positive integer as this object is less than, equal to,
   * or greater than the specified object.
   * @throws NullPointerException if the specified object is null
   * @throws ClassCastException   if the specified object's type prevents it from being compared to
   *                              this object.
   */
  @Override
  default int compareTo(Object o) {
    Objects.requireNonNull(o);
    if (!(o instanceof Playable)) {
      throw new ClassCastException("Unable to compare given object to a Playable");
    }
    else {
      Playable other = (Playable) o;
      return Pitch.distance(other.getPitch(), other.getOctave(),
              this.getPitch(), this.getOctave());
    }
  }
}
