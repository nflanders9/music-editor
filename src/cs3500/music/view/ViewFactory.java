package cs3500.music.view;

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
      case "full":    return new CompositeView(new MainGUI(model), new MidiView(model));
      default:        throw new IllegalArgumentException("unrecognized view type string");
    }
  }
}
