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
   * Constructs a new LinkImpl with the given beat to link to and the given active iteration
   * @param linkedBeatNum the beat to link to when active
   * @param iterNum       the iteration number to make the link active during
   */
  public LinkImpl(int linkedBeatNum, int iterNum) {
    this.iterNum = iterNum;
    this.linkedBeatNum = linkedBeatNum;
  }


  @Override
  public int getPlayIteration() {
    return this.iterNum;
  }

  @Override
  public int getLinkedBeat() {
    return this.linkedBeatNum;
  }
}
