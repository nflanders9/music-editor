package model;

/**
 * Represents the twelve musical pitches
 * model.Note: sharps are represented using "s" instead of the traditional "#"
 */
public enum Pitch {
  C, Cs, D, Ds, E, F, Fs, G, Gs, A, As, B;

  @Override
  public String toString() {
    return this.name().replace("s", "#");
  }
}
