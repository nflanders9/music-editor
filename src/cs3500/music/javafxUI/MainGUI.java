package cs3500.music.javafxUI;

import java.awt.*;
import java.util.List;
import java.util.Objects;

import cs3500.music.consoleUI.View;
import cs3500.music.model.MusicEditorModel;
import cs3500.music.model.Note;
import cs3500.music.model.Pitch;
import cs3500.music.model.Playable;
import cs3500.music.model.Song;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
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
   * @param gc      the GraphicsContext to render to
   * @param seconds the timestamp of the song to render
   * @throws NullPointerException if gc is null
   */
  private void render(GraphicsContext gc, double seconds) {
    gc.setFill(Color.LIGHTGREY);
    gc.fillRect(0, 0, JavaFXConstants.WINDOW_WIDTH, JavaFXConstants.WINDOW_HEIGHT);

    Objects.requireNonNull(gc);
    double beat = model.getTempo() * seconds / 60.0;


    // determine the fraction of a measure that this time corresponds to
    double measureFracOffset = 0;
    int minMeasure = 0;
    double minBeat = 0;
    if ((beat / (double) model.getBeatsPerMeasure()) > JavaFXConstants.MAX_BAR_LOCATION) {
      measureFracOffset = (beat / model.getBeatsPerMeasure()) % 1.0;
      minMeasure = (int) beat / model.getBeatsPerMeasure() - (int) JavaFXConstants.MAX_BAR_LOCATION;
      minBeat = (beat / (double) model.getBeatsPerMeasure() - JavaFXConstants.MAX_BAR_LOCATION)
              * model.getBeatsPerMeasure();
    }


    // determines position of beat tracking bar
    double xPos = Math.min((double) JavaFXConstants.GRID_PADDING_LEFT + beat *
                    ((double) JavaFXConstants.MEASURE_WIDTH / (double) model.getBeatsPerMeasure()),
            JavaFXConstants.MEASURE_WIDTH * JavaFXConstants.MAX_BAR_LOCATION +
                    JavaFXConstants.GRID_PADDING_LEFT);


    // find the highest and lowest notes in this Song
    Playable lowest = model.getLowest();
    Playable highest = model.getHighest();

    // get the pitches and octaves of the highest and lowest notes
    int lowestOctave;
    Pitch lowestPitch;
    int highestOctave;
    Pitch highestPitch;
    try {
      lowestOctave = lowest.getOctave();
      lowestPitch = lowest.getPitch();
      highestOctave = highest.getOctave();
      highestPitch = highest.getPitch();
    }
    catch (NullPointerException e) {
      lowestOctave = 4;
      lowestPitch = Pitch.C;
      highestOctave = 5;
      highestPitch = Pitch.C;
    }


    // computes the width of the console needed to display the notes in this song
    int width = Math.abs(Pitch.distance(lowestPitch, lowestOctave,
            highestPitch, highestOctave)) + 1;


    // draw horizontal grid lines and label them with a pitch
    Pitch[] pitches = Pitch.values();
    gc.setTextBaseline(VPos.CENTER);
    gc.setTextAlign(TextAlignment.CENTER);
    gc.setFill(Color.BLACK);
    gc.setStroke(Color.BLACK);
    gc.setLineWidth(2);
    for (int i = 0; i < width + 1; ++ i) {
      gc.strokeLine(JavaFXConstants.GRID_PADDING_LEFT,
              i * JavaFXConstants.GRID_SPACING_VERT + JavaFXConstants.GRID_PADDING_TOP,
              JavaFXConstants.WINDOW_WIDTH,
              i * JavaFXConstants.GRID_SPACING_VERT + JavaFXConstants.GRID_PADDING_TOP);

      // only label the pitch if it is between two grid lines
      if (i < width) {
        Pitch pitch = pitches[(i + lowestPitch.ordinal()) % pitches.length];
        gc.fillText(pitch.toString() + Integer.toString(lowestOctave + (i / pitches.length)),
                JavaFXConstants.LABEL_PADDING_LEFT,
                (width - i) * JavaFXConstants.GRID_SPACING_VERT +
                        JavaFXConstants.GRID_PADDING_TOP -
                        JavaFXConstants.LABEL_OFFSET);
      }
    }

    // draw notes at all visible beats
    for (int curBeat = (int) minBeat;
         curBeat < (int) beat + model.getBeatsPerMeasure() * JavaFXConstants.MAX_MEASURES_ON_SCREEN;
         curBeat++) {
      List<Playable> notes = model.getNotes(curBeat);

      // draw every note at the given beat
      for (Playable note : notes) {
        // set the color of the block based on whether the note is starting or sustaining
        if (note.getStartBeat() == curBeat) {
          gc.setFill(JavaFXConstants.NOTE_START_COLOR);
        }
        else {
          gc.setFill(JavaFXConstants.NOTE_SUSTAIN_COLOR);
        }

        int pitchNum = Pitch.distance(lowestPitch, lowestOctave, note.getPitch(), note.getOctave());

        // calculate the position and width to use to draw the note
        double start = (curBeat - minBeat) *
                JavaFXConstants.MEASURE_WIDTH / model.getBeatsPerMeasure()
                + JavaFXConstants.GRID_PADDING_LEFT;

        double displayStart = Math.max(start, JavaFXConstants.GRID_PADDING_LEFT);
        double displayWidth = (JavaFXConstants.MEASURE_WIDTH /model.getBeatsPerMeasure()) -
                (displayStart - start);

        gc.fillRect(displayStart, JavaFXConstants.GRID_PADDING_TOP +
                        JavaFXConstants.GRID_SPACING_VERT * (width - pitchNum - 1),
                displayWidth,
                JavaFXConstants.GRID_SPACING_VERT);
      }
    }


    // draw vertical measure lines
    gc.setFill(Color.BLACK);
    for (int measure = minMeasure;
         measure <= minMeasure + JavaFXConstants.MAX_MEASURES_ON_SCREEN;
         measure++) {
      double linePos = JavaFXConstants.GRID_PADDING_LEFT + JavaFXConstants.MEASURE_WIDTH *
              (measure - minMeasure - measureFracOffset);

      if (linePos >= JavaFXConstants.GRID_PADDING_LEFT) {
        gc.strokeLine(linePos,
                JavaFXConstants.GRID_PADDING_TOP,
                linePos,
                JavaFXConstants.GRID_SPACING_VERT * (width + 2));
        gc.fillText(Integer.toString(measure * 4),
                linePos,
                JavaFXConstants.MEASURE_LABEL_PADDING);
      }
    }
    gc.strokeLine(JavaFXConstants.GRID_PADDING_LEFT, JavaFXConstants.GRID_PADDING_TOP,
            JavaFXConstants.GRID_PADDING_LEFT, JavaFXConstants.GRID_SPACING_VERT * (width + 2));

    // draw current beat line
    gc.setStroke(Color.RED);
    gc.setLineWidth(4);
    gc.strokeLine(xPos, JavaFXConstants.GRID_PADDING_TOP, xPos,
            JavaFXConstants.GRID_PADDING_TOP + width * JavaFXConstants.GRID_SPACING_VERT);

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
      classModel.addNote(new Note(8, 4, Pitch.E, 4));
      classModel.addNote(new Note(16, 8, Pitch.G, 4));
      classModel.addNote(new Note(28, 4, Pitch.D, 5));
      classModel.addNote(new Note(12, 2, Pitch.Gs, 4));
      classModel.addNote(new Note(2, 4, Pitch.B, 3));
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
        // elapsed and max time are in seconds
        double elapsed = (currentNanoTime - startNanoTime) / 1000000000.0;
        double maxTime =
                ((double) parent.model.getLength()) / ((double) parent.model.getTempo()) * 60;
        parent.render(gc, Math.min(elapsed, maxTime));
      }
    }.start();

    primaryStage.show();
  }

}
