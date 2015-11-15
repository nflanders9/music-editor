package cs3500.music.view;

import java.util.Objects;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;

import cs3500.music.model.MusicEditorModel;
import cs3500.music.model.Pitch;
import cs3500.music.model.Playable;

/**
 * represents the view of a musical editor through sound of the song
 */
public class MidiView implements View {
  private final Synthesizer synth;
  private final Receiver receiver;
  private MusicEditorModel model;

  /**
   * Constructs a default MidiView by constructing a synthesizer and
   * receiver, and turning the synthesizer open. If a MidiSystem is unavailable,
   * an exception is thrown.
   */
  public MidiView(MusicEditorModel model) {
    Synthesizer synthesizer = null;
    Receiver rcvr = null;
    try {
      synthesizer = MidiSystem.getSynthesizer();
      rcvr = synthesizer.getReceiver();
    }
    catch (MidiUnavailableException e){
      e.printStackTrace();
    }
    this.synth = synthesizer;
    try {
      this.synth.open();
    }
    catch (MidiUnavailableException e) {
      System.err.println("Midi device is unavailable");
    }
    this.receiver = rcvr;
    this.model = model;
  }

  /**
   * Construct a MidiView with the specified model and synthesizer
   * @param model the model to display with this view
   * @param synth the synthsizer Midi object
   * @throws NullPointerException if the given model is null
   */
  public MidiView(MusicEditorModel model, Synthesizer synth) {
    this.model = Objects.requireNonNull(model);
    this.synth = synth;
    Receiver rcvr = null;
    try {
      rcvr = synth.getReceiver();
    }
    catch (MidiUnavailableException e) {
      System.err.println("unable to access Midi receiver");
    }
    this.receiver = rcvr;
  }

  /**
   * Construct a new MidiView with a null model that must be set before rendering
   */
  public MidiView() {
    this(null);
  }

  @Override
  public void setModel(MusicEditorModel model) {
    this.model = Objects.requireNonNull(model);
  }

  /**
   * Renders the MidiView based on the given model, by playing all of the notes
   * in the model
   */
  private void playNotes(int beat) throws InvalidMidiDataException {
    for (Playable p : this.model.getNotes(beat)) {
      if (p.getStartBeat() == beat) {
        MidiMessage start = new ShortMessage(
                ShortMessage.NOTE_ON, p.getInstrumentID() - 1, Pitch.getMidi(p.getPitch(), p.getOctave()), 100);
        this.receiver.send(start, p.getStartBeat() * (60000000 / model.getTempo()));
      }
      if (p.getStartBeat() + p.getDuration() == beat + 1) {
        MidiMessage stop = new ShortMessage(
                ShortMessage.NOTE_OFF, p.getInstrumentID() - 1, Pitch.getMidi(p.getPitch(), p.getOctave()), 100);
        this.receiver.send(stop, (p.getStartBeat() + p.getDuration()) * (60000000 / model.getTempo()));
      }
    }
  }

  @Override
  public void render() {
    Objects.requireNonNull(this.model);
    try {
      for (int beat = 0; beat < model.getLength(); beat++) {
        this.playNotes(beat);
      }
    }
    catch (InvalidMidiDataException e){

    }

    this.receiver.close();

  }

}
