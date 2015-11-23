package cs3500.music.view;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import cs3500.music.model.Playable;
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
   * @throws NullPointerException if the given MouseListener is null
   */
  void addMouseListener(MouseListener listener);

  /**
   * Adds the given MouseMotionListener to this GuiView
   * @param listener the MouseMotionListener to add
   * @throws NullPointerException if the given MouseMotionListener is null
   */
  void addMouseMotionListener(MouseMotionListener listener);

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
  void mouseClick(int x, int y, boolean leftButton);

  /**
   * Handles a mouse drag event where the mouse is being dragged at the given x and y coordinates
   * @param x the x coordinate of the mouse being dragged
   * @param y the y coordinate of the mouse being dragged
   */
  void mouseDrag(int x, int y);

  /**
   * Get the highest Playable to render in this GUI view
   * @return  the highest Playable to render in this GUI view
   */
  Playable getHighBound();

  /**
   * Get the lowest Playable to render in this GUI view
   * @return  the lowest Playable to render in this GUI view
   */
  Playable getLowBound();

  /**
   * Set the highest note to render in this GUI view
   * @param note  the highest Playable to render in this GUI view
   * @throws NullPointerException if the given Playable is null
   */
  void setHighBound(Playable note);

  /**
   * Set the lowest note to render in this GUI view
   * @param note  the lowest Playable to render in this GUI view
   * @throws NullPointerException if the given Playable is null
   */
  void setLowBound(Playable note);
}
