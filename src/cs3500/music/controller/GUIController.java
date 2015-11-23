package cs3500.music.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import cs3500.music.model.Pitch;
import cs3500.music.model.Playable;
import cs3500.music.view.GuiView;
import cs3500.music.view.ViewModel;
import javafx.animation.Timeline;
import jdk.nashorn.internal.runtime.regexp.joni.ApplyCaseFoldArg;


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
    double HORIZONTAL_SCROLL_SCALE = 0.06;
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
        view.render(view.getViewModel().getCurrentTime());
      }
    });

    // handle right key
    kh.installKeyPressed(18, () -> {
      if (!view.getViewModel().isPlaying()) {
        view.getViewModel().setCurrentTime(
                view.getViewModel().getCurrentTime() + HORIZONTAL_SCROLL_SCALE);
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

      // mutate the highest and lowest Playables to modify the view window
      view.setLowBound(currentLow.setPitch(Pitch.pitchFromMidi(newLowMidi)).setOctave(
              Pitch.octaveFromMidi(newLowMidi)));
      view.setHighBound(currentHigh.setPitch(Pitch.pitchFromMidi(newHighMidi)).setOctave(
              Pitch.octaveFromMidi(newHighMidi)));

      view.render(view.getViewModel().getCurrentTime());
    });

    // handle home key
    kh.installKeyPressed(15, () -> {
      ViewModel vm = view.getViewModel();
      if (view.getTimeline() != null) {
        view.getTimeline().stop();
      }
      view.getViewModel().setIsPlaying(false);
      view.getViewModel().setCurrentTime(0);
      view.render(view.getViewModel().getCurrentTime());
    });


    // handle end key
    kh.installKeyPressed(14, () -> {
      ViewModel vm = view.getViewModel();
      if (view.getTimeline() != null) {
        view.getTimeline().stop();
      }
      vm.setIsPlaying(false);
      vm.setCurrentTime(vm.getLength() * (1.0 / (double) vm.getTempo()) * 60.0);
      view.render(vm.getCurrentTime());
    });


    // handle numeric keys 1 through 8 on the num row and num pad for selecting note duration
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
        view.getViewModel().setDragOrigin(e.getPoint());
      }

      @Override
      public void mouseReleased(MouseEvent e) {
        view.getViewModel().setDragOrigin(null);
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
  public MouseMotionListener createMouseMotionListener() {
    MouseMotionListener mouseMotionListener = new MouseMotionListener() {
      @Override
      public void mouseDragged(MouseEvent e) {
        view.mouseDrag(e.getX(), e.getY());
      }

      @Override
      public void mouseMoved(MouseEvent e) {

      }
    };
    return mouseMotionListener;
  }

  @Override
  public void handleKey(KeyEvent event) {
    System.out.println(event.getKeyCode());
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
