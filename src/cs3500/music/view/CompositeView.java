package cs3500.music.view;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;

import cs3500.music.controller.KeyEventAdapter;
import cs3500.music.controller.KeyboardHandler;
import cs3500.music.model.MusicEditorModel;
import cs3500.music.model.Playable;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.LinearGradient;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Represents a view that is a composition of a MIDI view and a graphical view
 */
public class CompositeView extends Application implements GuiView {

  /**
   * Represents the GUI view contained in this class
   */
  private static MainGUI classGui;

  /**
   * Represents the MIDI view contained in this class
   */
  private static MidiView classMidi;

  /**
   * Represents the GUI view contained in this composite view
   */
  private MainGUI gui;

  /**
   * Represents the MIDI view contained in this composite view
   */
  private MidiView midi;


  private KeyListener keyListener;
  private static KeyListener classKeyListener;
  private MouseListener mouseListener;
  private static MouseListener classMouseListener;
  private MouseMotionListener mouseMotionListener;
  private static MouseMotionListener classMouseMotionListener;

  private Stage primaryStage;
  private Timeline timeline;

  /**
   * Constructs a new CompositeView with the given graphical and MIDI views
   *
   * @param gui  the graphical view to include
   * @param midi the MIDI view to include
   */
  public CompositeView(MainGUI gui, MidiView midi) {
    Objects.requireNonNull(gui);
    Objects.requireNonNull(midi);
    this.gui = gui;
    this.midi = midi;
    this.timeline = null;
  }

  public CompositeView() {
    this.gui = classGui;
    this.midi = classMidi;
    this.timeline = null;
  }

  public static void setViews(MainGUI gui, MidiView midi) {
    classGui = gui;
    classMidi = midi;
  }

  @Override
  public void render(double timestamp) {
    if (this.gui != null && this.midi != null) {
      this.midi.render(timestamp);
      this.gui.render(timestamp);
    }
  }

  @Override
  public void setModel(MusicEditorModel model) {
    this.midi.setModel(model);
    this.gui.setModel(model);
  }

  @Override
  public ViewModel getViewModel() {
    return gui.getViewModel();
  }

  /**
   * The main entry point for all JavaFX applications. The start method is called after the init
   * method has returned, and after the system is ready for the application to begin running.
   *
   * <p> NOTE: This method is called on the JavaFX Application Thread. </p>
   *
   * @param primaryStage the primary stage for this application, onto which the application scene
   *                     can be set. The primary stage will be embedded in the browser if the
   *                     application was launched as an applet. Applications may create other
   *                     stages, if needed, but they will not be primary stages and will not be
   *                     embedded in the browser.
   */
  @Override
  public void start(Stage primaryStage) throws Exception {
    this.primaryStage = primaryStage;
    StackPane root = new StackPane();
    Scene scene = new Scene(root, GUIConstants.WINDOW_WIDTH, GUIConstants.WINDOW_HEIGHT);
    // sets the key listener for the JavaFX application to use Swing KeyEvents
    this.keyListener = classKeyListener;
    this.mouseListener = classMouseListener;
    this.mouseMotionListener = classMouseMotionListener;
    scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent event) {
        if (keyListener != null) {
          if (event.getEventType() == KeyEvent.KEY_PRESSED) {
            System.out.println(new KeyEventAdapter(event).getKeyCode());
            keyListener.keyPressed(new KeyEventAdapter(event));
          } else if (event.getEventType() == KeyEvent.KEY_RELEASED) {
            keyListener.keyReleased(new KeyEventAdapter(event));
          } else if (event.getEventType() == KeyEvent.KEY_TYPED) {
            keyListener.keyTyped(new KeyEventAdapter(event));
          }
        }
      }
    });

    scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        if (mouseListener != null) {
          mouseListener.mouseClicked(
                  new java.awt.event.MouseEvent(new Box(0), 0, 0, 0, (int)event.getX(),
                          (int)event.getY(), 0, false, event.getButton().ordinal()));

        }
      }
    });

    scene.setOnMousePressed(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        if (mouseListener != null) {
          mouseListener.mousePressed(
                  new java.awt.event.MouseEvent(new Box(0), 0, 0, 0, (int) event.getX(),
                          (int) event.getY(), 0, false, event.getButton().ordinal()));
        }
      }
    });

    scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        if (event != null) {
          mouseMotionListener.mouseDragged(
                  new java.awt.event.MouseEvent(new Box(0), 0, 0, 0, (int)event.getX(),
                          (int)event.getY(), 0, false, event.getButton().ordinal()));
        }
      }
    });

    root.getChildren().add(gui.getCanvas());
    primaryStage.setTitle("Music Editor");
    primaryStage.setScene(scene);

    this.render(0);
    primaryStage.show();
  }

  @Override
  public void addKeyListener(KeyListener listener) {
    this.keyListener = listener;
    classKeyListener = listener;
  }

  @Override
  public void addMouseListener(MouseListener listener) {
    this.mouseListener = listener;
    classMouseListener = listener;
  }

  @Override
  public void addMouseMotionListener(MouseMotionListener listener) {
    this.mouseMotionListener = listener;
    classMouseMotionListener = listener;
  }

  @Override
  public Timeline play() {
    Timeline timeline = new Timeline(new KeyFrame(
            Duration.millis(5),
            new EventHandler<ActionEvent>() {
              double time = getViewModel().getCurrentTime();
              @Override
              public void handle(ActionEvent event) {
                if (getViewModel().isPlaying()) {
                  render(time);
                  getViewModel().setCurrentTime(time);
                  time += .005;
                }
              }
            })
    );
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();
    this.timeline = timeline;
    return timeline;
  }

  @Override
  public Timeline getTimeline() {
    return this.timeline;
  }

  @Override
  public void mouseClick(int x, int y, boolean leftButton) {
    gui.mouseClick(x, y, leftButton);
  }

  @Override
  public void mouseDrag(int x, int y) {
    gui.mouseDrag(x, y);
  }

  @Override
  public Playable getHighBound() {
    return gui.getHighBound();
  }

  @Override
  public Playable getLowBound() {
    return gui.getLowBound();
  }

  @Override
  public void setHighBound(Playable note) {
    gui.setHighBound(note);
  }

  @Override
  public void setLowBound(Playable note) {
    gui.setLowBound(note);
  }
}
