package cs3500.music.model;

/**
 * Represents the twelve musical pitches
 * Note: sharps are represented using "s" instead of the traditional "#"
 */
public enum Pitch {
  C, Cs, D, Ds, E, F, Fs, G, Gs, A, As, B;

  @Override
  public String toString() {
    return this.name().replace("s", "#");
  }

  /**
   * Return the number of notes between the given Pitches and octaves
   * @param p1    the Pitch of the first note
   * @param oct1  the octave of the first note
   * @param p2    the Pitch of the second note
   * @param oct2  the octave of the second note
   * @return      the distance from the first note to the second note
   */
  public static int distance(Pitch p1, int oct1, Pitch p2, int oct2) {
    return (Pitch.values().length * (oct2 - oct1)) + (p2.ordinal() - p1.ordinal());
  }

  public static int getMidi(Pitch pitch, int octave) {
    return 60 + Pitch.distance(pitch, octave, Pitch.C, 4);
  }

  public static Pitch pitchFromMidi(int midi) {
    return Pitch.values()[midi % Pitch.values().length];
  }

  public static int octaveFromMidi(int midi) {
    return (midi / Pitch.values().length) - 1;
  }
}
