package cs3500.music.view;

import cs3500.music.controller.Controller;
import cs3500.music.controller.GUIController;
import cs3500.music.model.MusicEditorModel;

/**
 * Factory object for constructing various types of views for the given model
 */
public final class ViewFactory {
  public static View makeView(String type, MusicEditorModel model) {
    switch (type.toLowerCase()) {
      case "console": return new ConsoleView(model);
      case "visual":  return new MainGUI(model);
      case "midi":    return new MidiView(model);
      case "full":    MainGUI gui = new MainGUI(model);
                      MidiView midi = new MidiView(model);
                      CompositeView.setViews(gui, midi);
                      GuiView view = new CompositeView(gui, midi);
                      Controller controller = new GUIController(view);
                      view.addKeyListener(controller.createKeyListener());
                      view.addMouseListener(controller.createMouseListener());
                      return view;
      default:        throw new IllegalArgumentException("unrecognized view type string");
    }
  }
}
