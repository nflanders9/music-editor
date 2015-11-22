package cs3500.music.view;

import java.util.Objects;

import cs3500.music.model.MusicEditorModel;

/**
 * Represents a view that is a composition of a MIDI view and a graphical view
 */
public class CompositeView implements View {

  /**
   * Represents the GUI view contained in this composite view
   */
  private final MainGUI gui;

  /**
   * Represents the MIDI view contained in this composite view
   */
  private final MidiView midi;

  /**
   * Constructs a new CompositeView with the given graphical and MIDI views
   * @param gui   the graphical view to include
   * @param midi  the MIDI view to include
   */
  public CompositeView(MainGUI gui, MidiView midi) {
    Objects.requireNonNull(gui);
    Objects.requireNonNull(midi);
    this.gui = gui;
    this.midi = midi;
  }

  @Override
  public void render(double timestamp) {
    this.midi.render(timestamp);
    this.gui.render(timestamp);
  }

  @Override
  public void setModel(MusicEditorModel model) {
    this.midi.setModel(model);
    this.gui.setModel(model);
  }

  @Override
  public ViewModel getViewModel() {
    return gui.getViewModel();
  }
}
