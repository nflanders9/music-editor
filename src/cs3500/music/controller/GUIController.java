package cs3500.music.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import cs3500.music.model.Link;
import cs3500.music.model.LinkImpl;
import cs3500.music.model.Pitch;
import cs3500.music.model.Playable;
import cs3500.music.view.GuiView;
import cs3500.music.view.ViewModel;
import javafx.animation.Timeline;


/**
 * Represents a Controller for the GUIView of the music editor
 */
public class GUIController implements Controller {

  /**
   * Represents the GUI view that this controller is tied to
   */
  private GuiView view;

  /**
   * Represents the KeyboardHandler with actions to call from this
   * class
   */
  private final KeyboardHandler keyHandler;

  /**
   * Constructs a new GUIController with new key and mouse handlers
   */
  public GUIController(GuiView view) {
    this.keyHandler = new KeyboardHandler();
    this.view = view;
  }

  /**
   * Constructs a new GUIController with the given handlers
   * @param keyHandler
   */
  public GUIController(GuiView view, KeyboardHandler keyHandler) {
    this.keyHandler = keyHandler;
    this.view = view;
  }

  @Override
  public KeyListener createKeyListener() {
    double HORIZONTAL_SCROLL_SCALE = 0.12;
    KeyboardHandler kh = new KeyboardHandler();

    // handle space bar
    kh.installKeyPressed(11, () -> {
      Timeline playTimeline;
      if (view.getViewModel().isPlaying()) {
        view.getViewModel().setIsPlaying(false);
        playTimeline = view.getTimeline();
        playTimeline.stop();
      }
      else {
        view.getViewModel().setIsPlaying(true);
        playTimeline = view.play();
      }
    });

    // handle left key
    kh.installKeyPressed(16, () -> {
      if (!view.getViewModel().isPlaying()) {
        view.getViewModel().setCurrentTime(Math.max(0,
                view.getViewModel().getCurrentTime() - HORIZONTAL_SCROLL_SCALE));
        view.getViewModel().resetIteration();
        view.render(view.getViewModel().getCurrentTime());
      }
    });

    // handle right key
    kh.installKeyPressed(18, () -> {
      if (!view.getViewModel().isPlaying()) {
        view.getViewModel().setCurrentTime(
                view.getViewModel().getCurrentTime() + HORIZONTAL_SCROLL_SCALE);
        view.getViewModel().resetIteration();
        view.render(view.getViewModel().getCurrentTime());
      }
    });

    // handle up key
    kh.installKeyPressed(17, () -> {
      Playable currentLow = view.getLowBound();
      Playable currentHigh = view.getHighBound();
      int newLowMidi = Pitch.getMidi(currentLow.getPitch(), currentLow.getOctave()) + 1;
      int newHighMidi = Pitch.getMidi(currentHigh.getPitch(), currentHigh.getOctave()) + 1;

      // mutate the highest and lowest Playables to modify the view window
      view.setLowBound(currentLow.setPitch(Pitch.pitchFromMidi(newLowMidi)).setOctave(
              Pitch.octaveFromMidi(newLowMidi)));
      view.setHighBound(currentHigh.setPitch(Pitch.pitchFromMidi(newHighMidi)).setOctave(
              Pitch.octaveFromMidi(newHighMidi)));

      view.render(view.getViewModel().getCurrentTime());
    });

    // handle down key
    kh.installKeyPressed(19, () -> {
      Playable currentLow = view.getLowBound();
      Playable currentHigh = view.getHighBound();
      int newLowMidi = Pitch.getMidi(currentLow.getPitch(), currentLow.getOctave()) - 1;
      int newHighMidi = Pitch.getMidi(currentHigh.getPitch(), currentHigh.getOctave()) - 1;

      // don't allow for negative MIDI values because those break things
      if (newLowMidi >= 0) {
        // mutate the highest and lowest Playables to modify the view window
        view.setLowBound(currentLow.setPitch(Pitch.pitchFromMidi(newLowMidi)).setOctave(
                Pitch.octaveFromMidi(newLowMidi)));
        view.setHighBound(currentHigh.setPitch(Pitch.pitchFromMidi(newHighMidi)).setOctave(
                Pitch.octaveFromMidi(newHighMidi)));

        view.render(view.getViewModel().getCurrentTime());
      }
    });

    // handle home key
    kh.installKeyPressed(15, () -> {
      ViewModel vm = view.getViewModel();
      if (view.getTimeline() != null) {
        view.getTimeline().stop();
      }
      view.getViewModel().setIsPlaying(false);
      view.getViewModel().setCurrentTime(0);
      view.getViewModel().setIteration(0);
      view.render(view.getViewModel().getCurrentTime());
    });


    // handle end key
    kh.installKeyPressed(14, () -> {
      ViewModel vm = view.getViewModel();
      if (view.getTimeline() != null) {
        view.getTimeline().stop();
      }
      vm.setIsPlaying(false);
      view.getViewModel().setIteration(0);
      vm.setCurrentTime(vm.getLength() * (1.0 / (double) vm.getTempo()) * 60.0);
      view.render(vm.getCurrentTime());
    });


    // handle numeric keys 1 through 9 on the num row and num pad for selecting note duration
    for (int i = 0; i < 9; ++ i) {
      final int finalIndex = i;
      Runnable runnable = () -> {
        view.getViewModel().setNewNoteDuration(finalIndex + 1);
        view.render(view.getViewModel().getCurrentTime());
      };
      kh.installKeyPressed(25 + i, runnable);
      kh.installKeyPressed(66 + i, runnable);
    }


    // handle page up (for incrementing instrument ID
    kh.installKeyPressed(12, () -> {
      view.getViewModel().setNewNoteInstrument(view.getViewModel().getNewNoteInstrument() + 1);
      view.render(view.getViewModel().getCurrentTime());
    });

    // handle page down (for decrementing instrument ID
    kh.installKeyPressed(13, () -> {
      view.getViewModel().setNewNoteInstrument(view.getViewModel().getNewNoteInstrument() - 1);
      view.render(view.getViewModel().getCurrentTime());
    });

    // handle delete key
    kh.installKeyPressed(81, () -> {
      view.render(view.getViewModel().getCurrentTime());
      ViewModel vm = view.getViewModel();
      List<Playable> toRemove = new ArrayList<Playable>(vm.getSelected());
      for (Playable note : toRemove) {
        vm.removeNote(note);
      }
      view.render(view.getViewModel().getCurrentTime());
    });

    // handle w key for transposing selection upwards
    kh.installKeyPressed(58, () -> {
      ViewModel vm = view.getViewModel();
      for (Playable note : vm.getSelected()) {
        int newMidi = Pitch.getMidi(note.getPitch(), note.getOctave()) + 1;
        note.setPitch(Pitch.pitchFromMidi(newMidi));
        note.setOctave(Pitch.octaveFromMidi(newMidi));
      }
      view.render(vm.getCurrentTime());
    });

    // handle s key for transposing selection downward
    kh.installKeyPressed(54, () -> {
      ViewModel vm = view.getViewModel();
      for (Playable note : vm.getSelected()) {
        int newMidi = Pitch.getMidi(note.getPitch(), note.getOctave()) - 1;
        note.setPitch(Pitch.pitchFromMidi(newMidi));
        note.setOctave(Pitch.octaveFromMidi(newMidi));
      }
      view.render(vm.getCurrentTime());
    });

    // handle a key for moving selection left
    kh.installKeyPressed(36, () -> {
      ViewModel vm = view.getViewModel();

      // need to iterate through a copy because selected is modified by vm.moveNote()
      List<Playable> tempList = new ArrayList<Playable>(vm.getSelected());
      for (Playable note : tempList) {
        if (note.getStartBeat() > 0) {
          vm.moveNote(note, -1);
        }
      }
      view.render(vm.getCurrentTime());
    });

    // handle d key for moving selection right
    kh.installKeyPressed(39, () -> {
      ViewModel vm = view.getViewModel();
      // need to iterate through a copy because selected is modified by vm.moveNote()
      List<Playable> tempList = new ArrayList<Playable>(vm.getSelected());
      for (Playable note : tempList) {
        vm.moveNote(note, 1);
      }
      view.render(vm.getCurrentTime());
    });

    // handle l key for creating links
    kh.installKeyPressed(47, () -> {
      ViewModel vm = view.getViewModel();
      int curBeat = (int) Math.round(((vm.getCurrentTime() / 60.0) * vm.getTempo()));
      if (vm.getLinkStart() != null) {
        vm.addLink(new LinkImpl(vm.getLinkStart(),curBeat, 0));
        vm.setLinkStart(null);
      }
      else {
        vm.setLinkStart(curBeat);
      }
      view.render(vm.getCurrentTime());
    });

    // handle c key for clearing all links
    kh.installKeyPressed(38, () -> {
      ViewModel vm = view.getViewModel();
      List<Link> temp = new ArrayList<Link>();
      temp.addAll(vm.getAllLinks());
      for (Link link : temp) {
        vm.removeLink(link);
      }
      view.render(vm.getCurrentTime());
    });


    return kh;
  }

  @Override
  public MouseListener createMouseListener() {
    MouseListener mouseListener = new MouseListener() {
      @Override
      public void mouseClicked(MouseEvent e) {
        view.mouseClick(e.getX(), e.getY(), e.getButton() == 1);
      }

      @Override
      public void mousePressed(MouseEvent e) {

      }

      @Override
      public void mouseReleased(MouseEvent e) {

      }

      @Override
      public void mouseEntered(MouseEvent e) {

      }

      @Override
      public void mouseExited(MouseEvent e) {

      }
    };
    return mouseListener;
  }

  @Override
  public void handleKey(KeyEvent event) {
    if (event.getID() == KeyEvent.KEY_PRESSED) {
      keyHandler.keyPressed(event);
    }
    else if (event.getID() == KeyEvent.KEY_RELEASED) {
      keyHandler.keyReleased(event);
    }
    else if (event.getID() == KeyEvent.KEY_TYPED) {
      keyHandler.keyTyped(event);
    }
  }

  @Override
  public void handleMouse(MouseEvent event) {

  }
}
