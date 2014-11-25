package com.gmail.krasilnikov.il.framework;
public interface Music {
    public void play();
    public void stop();
    public void pause();
    public void setLooping (boolean looping);
    public void setVolume (float volume);
    public boolean isPlaying();
    public boolean isStoping();
    public boolean isLooping();
    public void dispose();
}
