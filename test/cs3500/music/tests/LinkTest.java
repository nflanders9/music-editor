package cs3500.music.tests;

import org.junit.Test;

import cs3500.music.model.Link;
import cs3500.music.model.LinkImpl;

import static org.junit.Assert.*;

/**
 * Tests for the Link interface
 */
public class LinkTest {

  @Test
  public void testPlayIteration() {
    Link l1 = new LinkImpl(0, 20, 2);
    Link l2 = new LinkImpl(10, 0, 0);

    assertEquals(l1.getLinkedBeat(), 20);
    assertEquals(l1.getLocationBeat(), 0);
    assertEquals(l1.getPlayIteration(), 2);
    l1.setPlayIteration(3);
    assertEquals(l1.getPlayIteration(), 3);

    assertEquals(l2.getLinkedBeat(), 0);
    assertEquals(l2.getLocationBeat(), 10);
    assertEquals(l2.getPlayIteration(), 0);
    l2.setPlayIteration(1000);
    assertEquals(l2.getPlayIteration(), 1000);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testIllegalPlayIteration1() {
    Link l1 = new LinkImpl(0, 20, -1);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testIllegalPlayIteration2() {
    Link l1 = new LinkImpl(0, 20, 2);
    l1.setPlayIteration(-2);
  }
}