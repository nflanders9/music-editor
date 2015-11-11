package cs3500.music.javafxUI;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

/**
 * Static storage for constants used by the JavaFX UI
 */
final class JavaFXConstants {
  private JavaFXConstants() { } // prevents instantiation

  static final int WINDOW_HEIGHT = 600;
  static final int WINDOW_WIDTH = 800;
  static final int GRID_SPACING_VERT = 20;
  static final int GRID_PADDING_LEFT = 40;
  static final int GRID_PADDING_TOP = 40;
  static final int LABEL_OFFSET = GRID_SPACING_VERT / 2;
  static final int LABEL_PADDING_LEFT = GRID_PADDING_LEFT / 2;
  static final int MEASURE_WIDTH = 60;
  static final int MAX_MEASURES_ON_SCREEN = WINDOW_WIDTH / MEASURE_WIDTH + 1;
  static final int MEASURE_LABEL_PADDING = 30;

  // represents the maximum number of measures to move the playing bar from the left side
  // of the screen before moving the notes underneath the bar instead
  static final double MAX_BAR_LOCATION = 4;

  static final LinearGradient NOTE_START_COLOR =
          new LinearGradient(0, JavaFXConstants.GRID_PADDING_TOP,
          0, JavaFXConstants.GRID_PADDING_TOP + (JavaFXConstants.GRID_SPACING_VERT / 2),
          false, CycleMethod.REFLECT, new Stop(0, Color.BLACK), new Stop(1, Color.DARKGRAY));

  static final LinearGradient NOTE_SUSTAIN_COLOR =
          new LinearGradient(0, JavaFXConstants.GRID_PADDING_TOP,
                  0, JavaFXConstants.GRID_PADDING_TOP + (JavaFXConstants.GRID_SPACING_VERT / 2),
          false, CycleMethod.REFLECT, new Stop(0, Color.GREEN), new Stop(1, Color.GREENYELLOW));

}
