package cs3500.music.view;

import java.util.Random;

import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;

/**
 * Static storage for constants used by the JavaFX UI
 */
final class GUIConstants {
  private GUIConstants() { } // prevents instantiation

  static final int WINDOW_HEIGHT = 1000;
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
  static final double MAX_BAR_LOCATION = 3;

  static final Color SELECT_COLOR = Color.LIGHTSEAGREEN;

  static final LinearGradient NOTE_START_COLOR =
          new LinearGradient(0, GUIConstants.GRID_PADDING_TOP,
          0, GUIConstants.GRID_PADDING_TOP + (GUIConstants.GRID_SPACING_VERT / 2),
          false, CycleMethod.REFLECT, new Stop(0, Color.BLACK), new Stop(1, Color.DARKGRAY));

  static final LinearGradient getNewColor() {
    Random rand = new Random();
    int red = 30 + rand.nextInt(100);
    int green = 30 + rand.nextInt(100);
    int blue = 30 + rand.nextInt(100);
    return new LinearGradient(0,
            GUIConstants.GRID_PADDING_TOP,
            0,
            GUIConstants.GRID_PADDING_TOP + (GUIConstants.GRID_SPACING_VERT / 2),
            false,
            CycleMethod.REFLECT,
            new Stop(0, Color.rgb(red, green, blue)),
            new Stop(1, Color.rgb(red + 125, green + 125, blue + 125)));
  }


}
