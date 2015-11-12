package cs3500.music.consoleUI;

import javax.sound.midi.*;
import java.util.List;
import cs3500.music.consoleUI.View;
import cs3500.music.model.*;
/**
 * represents the view of a musical editor through sound of the song
 */
public class MidiView implements View {
  private final Synthesizer synth;
  private final Receiver receiver;
  private final MusicEditorModel model;

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
   * Renders the MidiView based on the given model, by playing all of the notes
   * in the model
   */
  public void playNotes(int beat) throws InvalidMidiDataException {
    for (Playable p : this.model.getNotes(beat)) {
      if (p.getStartBeat() == beat) {
        MidiMessage start = new ShortMessage(
                ShortMessage.NOTE_ON, 0, Pitch.getMidi(p.getPitch(), p.getOctave()), 100);
        this.receiver.send(start, p.getStartBeat() * 200000);
      }
      if (p.getStartBeat() + p.getDuration() == beat + 1) {
        MidiMessage stop = new ShortMessage(
                ShortMessage.NOTE_ON, 0, Pitch.getMidi(p.getPitch(), p.getOctave()), 100);
        this.receiver.send(stop, (p.getStartBeat() + p.getDuration()) * 200000);
      }
    }
  }
  public void render() {
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
