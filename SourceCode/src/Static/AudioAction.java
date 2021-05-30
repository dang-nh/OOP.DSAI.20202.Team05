package Static;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.Objects;


/**
 * Custom the Clip, load Audio from file .wav and use it
 *
 */
public class AudioAction {
    private Clip clip;
    private final String url;
    private int volumeNumber;

    public AudioAction(String url) {
        this.url = url;
    }

    public boolean isPlaying() {
        return clip != null && clip.isRunning();
    }

    protected void open() throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        clip = AudioSystem.getClip();
        clip.open(AudioSystem.getAudioInputStream(Objects.requireNonNull(this.getClass().getResource(url))));
        setVolume(volumeNumber);
    }

    public void play() throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        if (clip == null || !clip.isRunning()) {
            open();
            clip.start();
        }
    }

    public void playLoop(int number) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        if (clip == null || !clip.isRunning()) {
            open();
            clip.loop(number);
        }
    }

    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.flush();
            dispose();
        }
    }

    public void dispose() {
        try {
            clip.close();
        } finally {
            clip = null;
        }
    }

    public void setVolume(int level){
        Objects.requireNonNull(clip);
        FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        if (volume != null){
            float range = volume.getMinimum();
            float result = range * (1 - level/100f);
            volume.setValue(result);
        }
    }

    public int getVolumeNumber() {
        return volumeNumber;
    }

    public void setVolumeNumber(int volumeNumber) {
        this.volumeNumber = volumeNumber;
    }
}
