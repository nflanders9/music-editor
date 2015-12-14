package cs3500.music.tests;

import org.junit.Test;

import cs3500.music.model.Link;
import cs3500.music.model.LinkImpl;
import cs3500.music.model.MusicEditorModel;
import cs3500.music.model.Note;
import cs3500.music.model.Pitch;
import cs3500.music.model.Playable;
import cs3500.music.model.Song;
import cs3500.music.view.MusicEditorViewModel;
import cs3500.music.view.ViewModel;

import static org.junit.Assert.*;

/**
 * Tests for the ViewModel interface
 */
public class ViewModelTest {

  ViewModel vm1;
  Playable c4;
  Playable cs7;
  Playable a12;
  Playable fs0;
  Playable gNeg2;

  /**
   * Initialize the testing data
   */
  private void init() {
    MusicEditorModel model = new Song();
    c4 = new Note(0, 1, Pitch.C, 4, 0, 100);
    cs7 = new Note(9, 4, Pitch.Cs, 7, 3, 100);
    a12 = new Note(13, 5, Pitch.A, 12, 1, 100);
    fs0 = new Note(153, 7, Pitch.Fs, 0);
    gNeg2 = new Note(2, 2, Pitch.G, -2);
    model.addNote(c4);
    model.addNote(cs7);
    model.addNote(a12);
    model.addNote(fs0);
    model.addNote(gNeg2);
    vm1 = new MusicEditorViewModel(model);
  }

  @Test
  public void testSelect() {
    init();
    assertEquals(vm1.getSelected().size(), 0);
    vm1.select(c4);
    assertEquals(vm1.getSelected().size(), 1);
    assertEquals(vm1.getSelected().get(0), c4);
    vm1.select(cs7);
    vm1.select(gNeg2);
    assertEquals(vm1.getSelected().size(), 3);
    vm1.getSelected().clear();
    assertEquals(vm1.getSelected().size(), 0);
  }

  @Test
  public void testIsPlaying() {
    init();
    assertEquals(vm1.isPlaying(), false);
    vm1.setIsPlaying(true);
    assert(vm1.isPlaying());
    vm1.setIsPlaying(false);
    assert(!vm1.isPlaying());
  }

  @Test
  public void testCurrentTime() {
    init();
    assertEquals(vm1.getCurrentTime(), 0, 0.01);
    vm1.setCurrentTime(23.123);
    assertEquals(vm1.getCurrentTime(), 23.123, 0.01);
  }

  @Test
  public void testNewNoteConfiguration() {
    init();
    assertEquals(vm1.getNewNoteInstrument(), 1);
    assertEquals(vm1.getNewNoteDuration(), 2);
    vm1.setNewNoteInstrument(123);
    vm1.setNewNoteDuration(6);
    assertEquals(vm1.getNewNoteInstrument(), 123);
    assertEquals(vm1.getNewNoteDuration(), 6);
  }

  @Test
  public void testLinkStart() {
    init();
    assertNull(vm1.getLinkStart());
    vm1.setLinkStart(6);
    assertEquals(vm1.getLinkStart(), new Integer(6));
    vm1.setLinkStart(null);
    assertNull(vm1.getLinkStart());
  }

  @Test
  public void testIteration() {
    init();
    assertEquals(vm1.getIteration(), 0);
    vm1.setIteration(4);
    assertEquals(vm1.getIteration(), 4);
    vm1.resetIteration();
    assertEquals(vm1.getIteration(), 0);
  }

  @Test
  public void testLinks() {
    init();
    Link l2 = new LinkImpl(4, 0, 1);
    Link l3 = new LinkImpl(0, 0, 3);
    Link l1 = new LinkImpl(8, 0, 0);
    vm1.addLink(l2);
    vm1.addLink(l1);
    vm1.addLink(l3);
    assertEquals(vm1.getAllLinks().size(), 3);
    // ensure that the links are being properly sorted by their iteration number
    assertEquals(vm1.getAllLinks().get(0), l1);
  }

}