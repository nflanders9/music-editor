package cs3500.music.view;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.Objects;

import javax.swing.*;

import cs3500.music.controller.KeyEventAdapter;
import cs3500.music.model.Link;
import cs3500.music.model.MusicEditorModel;
import cs3500.music.model.Playable;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Represents a view that is a composition of a MIDI view and a graphical view
 */
public class CompositeView extends Application implements GuiView {

  /**
   * Represents the GUI view contained in this class
   */
  private static GuiView classGui;

  /**
   * Represents the MIDI view contained in this class
   */
  private static View classMidi;

  /**
   * Represents the GUI view contained in this composite view
   */
  private GuiView gui;

  /**
   * Represents the MIDI view contained in this composite view
   */
  private View midi;

  /**
   * Represents a KeyListener for this composite view
   */
  private KeyListener keyListener;

  /**
   * Represents a KeyListener for new instances of this class
   */
  private static KeyListener classKeyListener;

  /**
   * Represents a MouseListener for this composite view
   */
  private MouseListener mouseListener;

  /**
   * Represents a MouseListener for new instances of this class
   */
  private static MouseListener classMouseListener;


  /**
   * Represents the main stage of this JavaFX application
   */
  private Stage primaryStage;

  /**
   * Represents the timeline that is currently being used to animate the rendering of
   * this CompositeView
   */
  private Timeline timeline;

  /**
   * Constructs a new CompositeView with the given graphical and MIDI views
   *
   * @param gui  the graphical view to include
   * @param midi the MIDI view to include
   */
  public CompositeView(GuiView gui, View midi) {
    Objects.requireNonNull(gui);
    Objects.requireNonNull(midi);
    this.gui = gui;
    this.midi = midi;
    this.timeline = null;
  }

  /**
   * Construct a new CompositeView with the default class parameters as this is required by
   * the JavaFX Application class
   */
  public CompositeView() {
    this.gui = classGui;
    this.midi = classMidi;
    this.timeline = null;
  }

  /**
   * Set the views contained within this CompositeView
   * @param gui   the GuiView to contain in this CompositeView
   * @param midi  the MIDI View to contain in this CompositeView
   */
  public static void setViews(GuiView gui, View midi) {
    classGui = gui;
    classMidi = midi;
  }

  @Override
  public void render(double timestamp) {
    if (this.gui != null) {
      this.gui.render(timestamp);
    }
    if (this.midi != null) {
      this.midi.render(timestamp);
    }
  }

  @Override
  public void setModel(MusicEditorModel model) {
    if (this.midi != null) {
      this.midi.setModel(model);
    }
    if (this.gui != null) {
      this.gui.setModel(model);
    }
  }

  @Override
  public ViewModel getViewModel() {
    if (this.gui != null) {
      return gui.getViewModel();
    }
    else {
      return midi.getViewModel();
    }
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
    scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent event) {
        if (keyListener != null) {
          if (event.getEventType() == KeyEvent.KEY_PRESSED) {
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
  public Timeline play() {
    Timeline timeline = new Timeline(new KeyFrame(
            Duration.millis(5),
            new EventHandler<ActionEvent>() {
              double time = getViewModel().getCurrentTime();
              @Override
              public void handle(ActionEvent event) {
                if (getViewModel().isPlaying()) {
                  getViewModel().setCurrentTime(time);
                  int beatNum = (int) Math.round(((time / 60.0) * getViewModel().getTempo()));
                  for (Link link : getViewModel().getLinks(beatNum)) {
                    if (link.getPlayIteration() == getViewModel().getIteration()) {
                      getViewModel().setCurrentTime(
                              link.getLinkedBeat() * 60.0 / getViewModel().getTempo() - 0.005);
                      getViewModel().setIteration(getViewModel().getIteration() + 1);
                    }
                  }
                  render(getViewModel().getCurrentTime());
                  time = getViewModel().getCurrentTime() + 0.005;
                }
              }
            })
    );
    // run the timeline indefinitely unless explicitly stopped
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

  @Override
  public Canvas getCanvas() {
    return gui.getCanvas();
  }
}
