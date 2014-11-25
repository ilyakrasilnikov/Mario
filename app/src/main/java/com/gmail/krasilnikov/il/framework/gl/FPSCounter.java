package com.gmail.krasilnikov.il.framework.gl;

import android.util.Log;

public class FPSCounter {
    long startTime = System.nanoTime();
    int frames = 0;

    public void logFrames() {
        frames++;
        float deltaTime = (System.nanoTime() - startTime) / 1000000000;
        if (deltaTime >= 1) {
            Log.d("FPSCounter", "fps: " + (int)(frames / deltaTime));
            frames = 0;
            startTime = System.nanoTime();
        }
    }
}
