package cs3500.music.view;

import java.awt.event.KeyListener;

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
}
