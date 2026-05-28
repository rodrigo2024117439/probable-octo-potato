package pt.isec.pa.deepsea.ui.res;

import java.net.URL;
import java.util.HashMap;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundManager {
    private static SoundManager instance;
    private HashMap<String, Media> sounds;
    private MediaPlayer currentMovePlayer;
    private MediaPlayer currentActionPlayer;
    private BooleanProperty isMuted;

    private SoundManager() {
        sounds = new HashMap<>();
        isMuted = new SimpleBooleanProperty(false);
    }

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    public void loadSound(String name, String filename) {
        URL resource = getClass().getResource("/sounds/" + filename);
        if (resource != null) {
            sounds.put(name, new Media(resource.toString()));
        }
    }

    public void playMoveSound(String name) {
        if (isMuted.get() || !sounds.containsKey(name)) return;

        if (currentMovePlayer != null) {
            currentMovePlayer.stop();
        }

        currentMovePlayer = new MediaPlayer(sounds.get(name));
        currentMovePlayer.play();
    }

    public void playSequence(String moveSound, String actionSound) {
        if (isMuted.get()) return;

        if (currentMovePlayer != null) {
            currentMovePlayer.stop();
        }

        if (sounds.containsKey(moveSound)) {
            currentMovePlayer = new MediaPlayer(sounds.get(moveSound));
            currentMovePlayer.setOnEndOfMedia(() -> {
                if (sounds.containsKey(actionSound)) {
                    if (currentActionPlayer != null) {
                        currentActionPlayer.stop();
                    }
                    currentActionPlayer = new MediaPlayer(sounds.get(actionSound));
                    currentActionPlayer.play();
                }
            });
            currentMovePlayer.play();
        } else if (sounds.containsKey(actionSound)) {
            playActionSound(actionSound);
        }
    }

    public void playActionSound(String name) {
        if (isMuted.get() || !sounds.containsKey(name)) return;

        if (currentActionPlayer != null) {
            currentActionPlayer.stop();
        }

        currentActionPlayer = new MediaPlayer(sounds.get(name));
        currentActionPlayer.play();
    }

    public BooleanProperty isMutedProperty() {
        return isMuted;
    }

    public void setMuted(boolean muted) {
        this.isMuted.set(muted);
        if (muted) {
            if (currentMovePlayer != null) currentMovePlayer.stop();
            if (currentActionPlayer != null) currentActionPlayer.stop();
        }
    }

    public boolean isMuted() {
        return isMuted.get();
    }
}