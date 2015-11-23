package cs3500.music.view;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

import javafx.animation.Timeline;

/**
 * Represents a graphical view of a MusicEditorModel
 */
public interface GuiView extends View {
  /**
   * Adds the given KeyListener to this GuiView
   * @param listener the KeyListener to add
   * @throws NullPointerException if the given KeyListener is null
   */
  void addKeyListener(KeyListener listener);

  /**
   * Adds the given MouseListener to this GuiView
   * @param listener the MouseListener to add
   * @throws NullPointerException if the given KeyListener is null
   */
  void addMouseListener(MouseListener listener);

  /**
   * Plays the given view from the current time and returns the Timeline object
   */
  Timeline play();

  /**
   * Returns the Timeline object that is playing the music if there is one, null otherwise
   */
  Timeline getTimeline();

  /**
   * Handles a mouse click at the given x and y coordinates
   */
  void mouseClick(int x, int y);
}
