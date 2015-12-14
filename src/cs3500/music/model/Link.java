package cs3500.music.model;

/**
 * Represents a link connection between two separate times in a music piece
 */
public interface Link {
  /**
   * Returns the iteration number that this Link affects playback of the piece during
   * Note: 0-indexed, so 0 means that the link is active the first time it is reached,
   * 1 means the second, etc...
   * @return  the iteration number that this Link affects playback of the piece during
   */
  int getPlayIteration();

  /**
   * Sets the active iteration of this Link to the given value
   * @param iteration   the new iteration number at which to activate this link
   */
  void setPlayIteration(int iteration);

  /**
   * Returns the beat number that this link leads to
   * @return  the beat number that this link leads to
   */
  int getLinkedBeat();

  /**
   * Returns the beat number that this link is located at
   * @return the beat number that this link is located at
   */
  int getLocationBeat();
}
