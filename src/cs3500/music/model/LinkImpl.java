package cs3500.music.model;

/**
 * Concrete implementation of the Link interface
 */
public class LinkImpl implements Link {
  /**
   * Represents the iteration number that this link is active during
   */
  private int iterNum;

  /**
   * Represents the beat number that this LinkImpl leads to when active
   */
  private int linkedBeatNum;

  /**
   * Represents the beat number that this LinkImpl is located at
   */
  private int locationBeat;

  /**
   * Constructs a new LinkImpl with the given beat to link to and the given active iteration
   * @param linkedBeatNum the beat to link to when active
   * @param iterNum       the iteration number to make the link active during
   */
  public LinkImpl(int locationBeat, int linkedBeatNum, int iterNum) {
    if (iterNum < 0) {
      throw new IllegalArgumentException("negative iteration number");
    }
    this.locationBeat = locationBeat;
    this.iterNum = iterNum;
    this.linkedBeatNum = linkedBeatNum;
  }


  @Override
  public int getPlayIteration() {
    return this.iterNum;
  }

  @Override
  public void setPlayIteration(int iteration) {
    if (iteration < 0) {
      throw new IllegalArgumentException("negative iteration number");
    }
    else {
      this.iterNum = iteration;
    }
  }

  @Override
  public int getLinkedBeat() {
    return this.linkedBeatNum;
  }

  @Override
  public int getLocationBeat() {
    return this.locationBeat;
  }
}
