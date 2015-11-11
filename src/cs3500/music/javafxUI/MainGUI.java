package cs3500.music.javafxUI;

import java.util.Objects;

import cs3500.music.consoleUI.View;
import cs3500.music.model.MusicEditorModel;
import cs3500.music.model.Note;
import cs3500.music.model.Pitch;
import cs3500.music.model.Playable;
import cs3500.music.model.Song;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Represents a javafxUI for displaying a {@link cs3500.music.model.MusicEditorModel} graphically
 */
public class MainGUI extends Application implements View {

  /**
   * Entry point for the JavaFX UI view
   * @param args
   */
  public static void main(String[] args) {
    launch(args);
  }

  /**
   * Updates the static MusicEditorModel that is associated with this class
   * @param model the model to associate with the MainGUI class
   */
  public static void setModel(MusicEditorModel model) {
    MainGUI.classModel = model;
  }

  /**
   * Represents the model that this instance of MainGUI will render
   */
  private MusicEditorModel model;

  /**
   * Represents the model that this MainGUI will default to
   */
  private static MusicEditorModel classModel;

  /**
   * Constructs a MainGUI with the default configuration
   */
  public MainGUI() {
    this.model = classModel;
  }

  /**
   * Constructs an instance of a MainGUI and sets the class's static model
   * field to whatever value was passed into the Constructor
   * @param model the MusicEditorModel to assign to the MainGUI class
   * @throws NullPointerException if the given model is null
   */
  public MainGUI(MusicEditorModel model) {
    Objects.requireNonNull(model);
    MainGUI.classModel = model;
    this.model = model;
  }

  /**
   * Render the model at the given beat to the given GraphicsContext
   * @param gc    the GraphicsContext to render to
   * @param beat  the beat number to render the model at
   * @throws NullPointerException if either of the arguments are null
   */
  private void render(GraphicsContext gc, int beat) {
    if (model.getLength() == 0) {
      return;
    }
    Objects.requireNonNull(gc);
    Objects.requireNonNull(beat);
    // find the highest and lowest notes in this Song
    Playable lowest = model.getLowest();
    Playable highest = model.getHighest();


    // get the pitches and octaves of the highest and lowest notes
    int lowestOctave = lowest.getOctave();
    Pitch lowestPitch = lowest.getPitch();
    int highestOctave = highest.getOctave();
    Pitch highestPitch = highest.getPitch();

    // computes the width of the console needed to display the notes in this song
    int width = Math.abs(Pitch.distance(lowestPitch, lowestOctave,
            highestPitch, highestOctave)) + 1;


    // draw horizontal gridlines and label them with a pitch
    Pitch[] pitches = Pitch.values();
    for (int i = 0; i < width + 1; ++ i) {
      gc.strokeLine(JavaFXConstants.GRID_PADDING_LEFT,
              i * JavaFXConstants.GRID_SPACING_VERT + JavaFXConstants.GRID_PADDING_TOP,
              JavaFXConstants.WINDOW_WIDTH,
              i * JavaFXConstants.GRID_SPACING_VERT + JavaFXConstants.GRID_PADDING_TOP);

      if (i < width) {
        Pitch pitch = pitches[(i + lowestPitch.ordinal()) % pitches.length];
        gc.fillText(pitch.toString() + Integer.toString(lowestOctave + (i / pitches.length)),
                JavaFXConstants.LABEL_PADDING_LEFT,
                i * JavaFXConstants.GRID_SPACING_VERT +
                        JavaFXConstants.GRID_PADDING_TOP +
                        JavaFXConstants.LABEL_OFFSET);
      }

    }


  }

  /**
   * Renders this View's {@link MusicEditorModel}
   */
  @Override
  public void render() {
    String[] args = new String[0];
    launch(args);
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
    if (classModel == null) {
      classModel = new Song();
      classModel.addNote(new Note(0, 4, Pitch.C, 4));
      classModel.addNote(new Note(0, 4, Pitch.E, 4));
      classModel.addNote(new Note(0, 4, Pitch.G, 4));
      classModel.addNote(new Note(2, 4, Pitch.D, 5));
      classModel.addNote(new Note(4, 2, Pitch.Gs, 4));
      classModel.addNote(new Note(2, 4, Pitch.B, 4));
    }
    this.model = classModel;
    StackPane root = new StackPane();
    Scene scene = new Scene(root, JavaFXConstants.WINDOW_WIDTH, JavaFXConstants.WINDOW_HEIGHT);
    Canvas canvas = new Canvas(JavaFXConstants.WINDOW_WIDTH, JavaFXConstants.WINDOW_HEIGHT);
    GraphicsContext gc = canvas.getGraphicsContext2D();
    root.getChildren().add(canvas);
    primaryStage.setTitle("Music Editor");
    primaryStage.setScene(scene);

    final long startNanoTime = System.nanoTime();
    MainGUI parent = this;
    new AnimationTimer() {
      public void handle(long currentNanoTime) {
        double elapsed = (currentNanoTime - startNanoTime) / 1000000000.0;
        parent.render(gc, (int) elapsed / model.getTempo());
      }
    }.start();

    primaryStage.show();
  }

}
