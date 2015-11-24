package cs3500.music.view;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cs3500.music.model.MusicEditorModel;
import cs3500.music.model.Note;
import cs3500.music.model.Pitch;
import cs3500.music.model.Playable;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.util.Duration;

/**
 * Represents a javafxUI for displaying a {@link cs3500.music.model.MusicEditorModel} graphically
 */
public class MainGUI implements GuiView {

  /**
   * Represents the model that this instance of MainGUI will render
   */
  private ViewModel model;

  /**
   * Represents the KeyListener that controls the music editor
   */
  private KeyListener keyListener;

  /**
   * Represents the MouseListener that controls the music editor
   */
  private MouseListener mouseListener;

  /**
   * Represents the MouseMotionListener that controls the music editor
   */
  private MouseMotionListener mouseMotionListener;

  /**
   * Represents the highest Playable to render in this GUI view
   */
  private Playable highBound;

  /**
   * Represents the lowest Playable to render in this GUI view
   */
  private Playable lowBound;

  /**
   * Represents the timestamp in seconds that the view is currently rendering
   */
  private double timestamp;

  /**
   * Represents the Canvas to draw this GUI view onto
   */
  private final Canvas canvas;

  /**
   * Represents the GraphicsContext used for rendering onto the Canvas of this GuiView
   */
  private GraphicsContext gc;

  /**
   * Maps the instrument ID numbers to the gradient colors used to render them in this GuiView
   */
  private final Map<Integer, LinearGradient> colors;

  /**
   * Represents the Timeline object used to control the animation of this GuiView
   */
  private Timeline timeline;

  /**
   * Constructs an instance of a MainGUI and sets the class's static model
   * field to whatever value was passed into the Constructor
   * @param model the MusicEditorModel to assign to the MainGUI class
   * @throws NullPointerException if the given model is null
   */
  public MainGUI(MusicEditorModel model) {
    Objects.requireNonNull(model);
    this.canvas = new Canvas(GUIConstants.WINDOW_WIDTH, GUIConstants.WINDOW_HEIGHT);
    this.gc = canvas.getGraphicsContext2D();
    this.model = new MusicEditorViewModel(model);
    this.timestamp = 0;
    this.colors = new HashMap<Integer, LinearGradient>();
    this.timeline = null;
    this.highBound = model.getHighest();
    this.lowBound = model.getLowest();
  }

  /**
   * Renders this View's {@link MusicEditorModel} at the given timestamp in milliseconds
   */
  @Override
  public void render(double timestamp) {
    this.render(this.gc, timestamp, this.colors);
  }

  /**
   * Sets the model associated with this instance of a MainGUI
   * @param model the MusicEditorModel to render with this view
   */
  @Override
  public void setModel(MusicEditorModel model) {
    this.model = new MusicEditorViewModel(model);
  }

  @Override
  public ViewModel getViewModel() {
    return this.model;
  }


  /**
   * Adds the given KeyListener to this GuiView
   * @param listener the KeyListener to add
   * @throws NullPointerException if the given KeyListener is null
   */
  @Override
  public void addKeyListener(KeyListener listener) {
    Objects.requireNonNull(listener);
    this.keyListener = listener;
  }

  @Override
  public void addMouseListener(MouseListener listener) {
    Objects.requireNonNull(listener);
    this.mouseListener = listener;
  }

  @Override
  public Timeline play() {
    double time = getViewModel().getCurrentTime();
    Timeline timeline = new Timeline(new KeyFrame(
            Duration.millis(5),
            new EventHandler<ActionEvent>() {
              double time = 0.0;
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

  /**
   * Return a dummy Playable value with the Pitch, octave, and start beat that was clicked on
   * @param x x coordinate of the mouse click
   * @param y y coordinate of the mouse click
   * @return  a new Playable at the click location
   */
  private Playable getNewClickedNote(int x, int y) {
    // account for bezels around grid where there are no notes
    y -= GUIConstants.GRID_PADDING_TOP;
    x -= GUIConstants.GRID_PADDING_LEFT;

    Playable highest = getHighBound();

    // find the beat that is located at the clicked location
    double leftmostBeat = Math.max((model.getTempo() * model.getCurrentTime() / 60.0)
            - GUIConstants.MAX_BAR_LOCATION * model.getBeatsPerMeasure(), 0);

    int beatNum = x / (GUIConstants.MEASURE_WIDTH / model.getBeatsPerMeasure());
    double beat = beatNum + leftmostBeat;

    int pitchNum = y / GUIConstants.GRID_SPACING_VERT;

    // finds the pitch that is located at the clicked location
    Pitch pitch = Pitch.pitchFromMidi(
            Pitch.getMidi(highest.getPitch(), highest.getOctave()) - pitchNum);

    // finds the octave that is located at the clicked location
    int octave = Pitch.octaveFromMidi(
            Pitch.getMidi(highest.getPitch(), highest.getOctave()) - pitchNum);



    return new Note((int) Math.round(beat), 1, pitch, octave);
  }

  /**
   * Return the Playable that was clicked or null if no Playable was clicked
   * @param x x coordinate of the mouse click
   * @param y y coordinate of the mouse click
   * @return  the Playable that was clicked or null if no Playable was clicked
   */
  private Playable getClicked(int x, int y) {
    // account for bezels around grid where there are no notes
    y -= GUIConstants.GRID_PADDING_TOP;
    x -= GUIConstants.GRID_PADDING_LEFT;

    Playable highest = getHighBound();
    double leftmostBeat = Math.max((model.getTempo() * model.getCurrentTime() / 60.0)
            - GUIConstants.MAX_BAR_LOCATION * model.getBeatsPerMeasure(), 0);


    int beatNum = x / (GUIConstants.MEASURE_WIDTH / model.getBeatsPerMeasure());
    int pitchNum = y / GUIConstants.GRID_SPACING_VERT;


    // finds the pitch that is located at the clicked location
    Pitch pitch = Pitch.pitchFromMidi(
            Pitch.getMidi(highest.getPitch(), highest.getOctave()) - pitchNum);

    // finds the octave that is located at the clicked location
    int octave = Pitch.octaveFromMidi(
            Pitch.getMidi(highest.getPitch(), highest.getOctave()) - pitchNum);

    // find the beat that is located at the clicked location
    double exactBeat = beatNum + leftmostBeat;
    int beat = (int) Math.round(exactBeat);

    for (Playable note : model.getNotes(beat)) {
      if (note.getPitch() == pitch && note.getOctave() == octave) {
        return note;
      }
    }
    return null;
  }

  @Override
  public void mouseClick(int x, int y, boolean leftButton) {
    Playable clickedNote = getClicked(x, y);
    if (clickedNote != null && leftButton) {
      model.select(clickedNote);
    }
    else if (clickedNote != null && !leftButton) {
      model.removeNote(clickedNote);
    }
    else if (clickedNote == null && !leftButton) {
      model.getSelected().clear();
    }
    else {
      Playable newNote = getNewClickedNote(x, y);
      newNote.setDuration(model.getNewNoteDuration());
      newNote.setInstrument(model.getNewNoteInstrument());
      model.addNote(newNote);
    }
    render(model.getCurrentTime());
  }


  @Override
  public Playable getHighBound() {
    return this.highBound;
  }

  @Override
  public Playable getLowBound() {
    return this.lowBound;
  }

  @Override
  public void setHighBound(Playable note) {
    Objects.requireNonNull(note);
    this.highBound = note;
  }

  @Override
  public void setLowBound(Playable note) {
    Objects.requireNonNull(note);
    this.lowBound = note;
  }


  /**
   * Render the model at the given beat to the given GraphicsContext
   * @param gc      the GraphicsContext to render to
   * @param seconds the timestamp of the song to render
   * @throws NullPointerException if gc is null
   */
  private void render(GraphicsContext gc, double seconds, Map<Integer, LinearGradient> colors) {
    gc.setFill(Color.LIGHTGREY);
    gc.fillRect(0, 0, GUIConstants.WINDOW_WIDTH, GUIConstants.WINDOW_HEIGHT);

    Objects.requireNonNull(gc);
    double beat = model.getTempo() * seconds / 60.0;


    // determine the fraction of a measure that this time corresponds to
    double measureFracOffset = 0;
    int minMeasure = 0;
    double minBeat = 0;
    if ((beat / (double) model.getBeatsPerMeasure()) > GUIConstants.MAX_BAR_LOCATION) {
      measureFracOffset = (beat / model.getBeatsPerMeasure()) % 1.0;
      minMeasure = (int) beat / model.getBeatsPerMeasure() - (int) GUIConstants.MAX_BAR_LOCATION;
      minBeat = (beat / (double) model.getBeatsPerMeasure() - GUIConstants.MAX_BAR_LOCATION)
              * model.getBeatsPerMeasure();
    }


    // determines position of beat tracking bar
    double xPos = Math.min((double) GUIConstants.GRID_PADDING_LEFT + beat *
                    ((double) GUIConstants.MEASURE_WIDTH / (double) model.getBeatsPerMeasure()),
            GUIConstants.MEASURE_WIDTH * GUIConstants.MAX_BAR_LOCATION +
                    GUIConstants.GRID_PADDING_LEFT);


    // find the highest and lowest notes in this view
    Playable lowest = getLowBound();
    Playable highest = getHighBound();

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
      gc.strokeLine(GUIConstants.GRID_PADDING_LEFT,
              i * GUIConstants.GRID_SPACING_VERT + GUIConstants.GRID_PADDING_TOP,
              GUIConstants.WINDOW_WIDTH,
              i * GUIConstants.GRID_SPACING_VERT + GUIConstants.GRID_PADDING_TOP);

      // only label the pitch if it is between two grid lines
      if (i < width) {
        Pitch pitch = pitches[(i + lowestPitch.ordinal()) % pitches.length];
        gc.fillText(pitch.toString() + Integer.toString(
                        Pitch.octaveFromMidi(Pitch.getMidi(lowestPitch, lowestOctave) + i)),
                GUIConstants.LABEL_PADDING_LEFT,
                (width - i) * GUIConstants.GRID_SPACING_VERT +
                        GUIConstants.GRID_PADDING_TOP -
                        GUIConstants.LABEL_OFFSET);
      }
    }

    // draw highlighting behind every selected note
    for (Playable note : model.getSelected()) {
      // don't draw the note if it is above or below the bounds of the window being rendered
      if (note.compareTo(getHighBound()) > 0 || note.compareTo(getLowBound()) < 0) {
        continue;
      }
      int pitchNum = Pitch.distance(lowestPitch, lowestOctave,
              note.getPitch(), note.getOctave());

      // calculate the position and width to use to draw the note
      double start = (note.getStartBeat() - minBeat) *
              GUIConstants.MEASURE_WIDTH / model.getBeatsPerMeasure()
              + GUIConstants.GRID_PADDING_LEFT;

      double displayStart = Math.max(start, GUIConstants.GRID_PADDING_LEFT);
      double displayWidth = (GUIConstants.MEASURE_WIDTH /model.getBeatsPerMeasure()) -
              (displayStart - start);
      gc.setFill(GUIConstants.SELECT_COLOR);
      gc.fillRect(displayStart - 4, GUIConstants.GRID_PADDING_TOP +
                      GUIConstants.GRID_SPACING_VERT * (width - pitchNum - 1) - 4,
              displayWidth + 8,
              GUIConstants.GRID_SPACING_VERT + 8);
    }

    // draw notes at all visible beats
    for (int curBeat = (int) minBeat;
         curBeat < (int) beat + model.getBeatsPerMeasure() * GUIConstants.MAX_MEASURES_ON_SCREEN;
         curBeat++) {
      List<Playable> notes = model.getNotes(curBeat);

      // draw every note at the given beat

      for (Playable note : notes) {
        // don't draw the note if it is above or below the bounds of the window being rendered
        if (note.compareTo(getHighBound()) > 0 || note.compareTo(getLowBound()) < 0) {
          continue;
        }

        // set the color of the block based on whether the note is starting or sustaining
        if (!colors.containsKey(note.getInstrumentID())) {
          colors.put(note.getInstrumentID(), GUIConstants.getNewColor());
        }
        if (note.getStartBeat() == curBeat) {
          gc.setFill(GUIConstants.NOTE_START_COLOR);
        }
        else {
          gc.setFill(colors.get(note.getInstrumentID()));
        }

        int pitchNum = Pitch.distance(lowestPitch, lowestOctave,
                note.getPitch(), note.getOctave());

        // calculate the position and width to use to draw the note
        double start = (curBeat - minBeat) *
                GUIConstants.MEASURE_WIDTH / model.getBeatsPerMeasure()
                + GUIConstants.GRID_PADDING_LEFT;

        double displayStart = Math.max(start, GUIConstants.GRID_PADDING_LEFT);
        double displayWidth = (GUIConstants.MEASURE_WIDTH /model.getBeatsPerMeasure()) -
                (displayStart - start);

        gc.fillRect(displayStart, GUIConstants.GRID_PADDING_TOP +
                        GUIConstants.GRID_SPACING_VERT * (width - pitchNum - 1),
                displayWidth,
                GUIConstants.GRID_SPACING_VERT);
      }
    }


    // draw vertical measure lines
    gc.setFill(Color.BLACK);
    for (int measure = minMeasure;
         measure <= minMeasure + GUIConstants.MAX_MEASURES_ON_SCREEN;
         measure++) {
      double linePos = GUIConstants.GRID_PADDING_LEFT + GUIConstants.MEASURE_WIDTH *
              (measure - minMeasure - measureFracOffset);

      if (linePos >= GUIConstants.GRID_PADDING_LEFT) {
        gc.strokeLine(linePos,
                GUIConstants.GRID_PADDING_TOP,
                linePos,
                GUIConstants.GRID_SPACING_VERT * (width + 2));
        gc.fillText(Integer.toString(measure * model.getBeatsPerMeasure()),
                linePos,
                GUIConstants.MEASURE_LABEL_PADDING);
      }
    }
    gc.strokeLine(GUIConstants.GRID_PADDING_LEFT, GUIConstants.GRID_PADDING_TOP,
            GUIConstants.GRID_PADDING_LEFT, GUIConstants.GRID_SPACING_VERT * (width + 2));

    // draw current beat line
    gc.setStroke(Color.RED);
    gc.setLineWidth(4);
    gc.strokeLine(xPos, GUIConstants.GRID_PADDING_TOP, xPos,
            GUIConstants.GRID_PADDING_TOP + width * GUIConstants.GRID_SPACING_VERT);

    // draw label for the current editor configuration
    gc.fillText("Instrument being edited: " +
            Integer.toString(model.getNewNoteInstrument()), 80, 12);
    gc.fillText("Selected note duration: " +
            Integer.toString(model.getNewNoteDuration()), 260, 12);

  }

  @Override
  public Canvas getCanvas() {
    return this.canvas;
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


}
