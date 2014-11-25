package com.gmail.krasilnikov.il.framework.impl;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.Window;
import android.view.WindowManager;

import com.gmail.krasilnikov.il.framework.Audio;
import com.gmail.krasilnikov.il.framework.FileIO;
import com.gmail.krasilnikov.il.framework.Game;
import com.gmail.krasilnikov.il.framework.Graphics;
import com.gmail.krasilnikov.il.framework.Input;
import com.gmail.krasilnikov.il.framework.Screen;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public abstract class GLGame extends Activity implements Game, GLSurfaceView.Renderer {

    @Override
    public Input getInput() {
        return input;
    }

    @Override
    public FileIO getFileIO() {
        return fileIO;
    }

    @Override
    public Graphics getGraphics() {
        throw new IllegalStateException("We are using OpenGL");
    }

    @Override
    public Audio getAudio() {
        return audio;
    }

    @Override
    public void setScreen(Screen screen) {
        if (screen == null)
            throw new IllegalArgumentException("Screen must not be null");
        this.screen.pause();
        this.screen.dispose();
        screen.resume();
        screen.update(0);
        this.screen = screen;
    }

    @Override
    public Screen getCurrentScreen() {
        return screen;
    }

    enum GlGameState {
        Initialized,
        Running,
        Paused,
        Finished,
        Idle
    }

    GLSurfaceView glView;
    GLGraphics glGraphics;
    Audio audio;
    Input input;
    FileIO fileIO;
    Screen screen;
    GlGameState state = GlGameState.Initialized;
    Object stateChanged = new Object();
    long startTime = System.nanoTime();

    // Activity, UI thread
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);
        glView = new GLSurfaceView(this);
        glView.setRenderer(this);
        setContentView(glView);
        glGraphics = new GLGraphics(glView);
        fileIO = new AndroidFileIO(getAssets());
        audio = new AndroidAudio(this);
        input = new AndroidInput(this, glView, 1, 1);
        PowerManager powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
    }

    // Activity, UI thread
    @Override
    public void onResume() {
        super.onResume();
        glView.onResume();
    }

    // GLSurfaceView.Renderer, V thread
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glGraphics.setGL(gl);
        synchronized (stateChanged) {
            if (state == GlGameState.Initialized)
                screen = getStartScreen();
            state = GlGameState.Running;
            screen.resume();
            startTime = System.nanoTime();
        }
    }

    // GLSurfaceView.Renderer, V thread
    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
    }

    // GLSurfaceView.Renderer, V thread
    @Override
    public void onDrawFrame(GL10 gl10) {
        GlGameState state = null;
        synchronized (stateChanged) {
            state = this.state;
        }

        if (state == GlGameState.Running) {
            float deltaTime = (System.nanoTime() - startTime) / 1000000000.0f;
            startTime = System.nanoTime();
            screen.update(deltaTime);
            screen.present(deltaTime);
        }

        if (state == GlGameState.Paused) {
            screen.pause();
            synchronized (stateChanged) {
                this.state = GlGameState.Idle;
                stateChanged.notifyAll();
            }
        }

        if (state == GlGameState.Finished) {
            screen.pause();
            screen.dispose();
            synchronized (stateChanged) {
                this.state = GlGameState.Idle;
                stateChanged.notifyAll();
            }
        }
    }

    // Activity, UI thread
    @Override
    public void onPause() {
        synchronized (stateChanged) {
            if (isFinishing())
                state = GlGameState.Finished;
            else
                state = GlGameState.Paused;
            while (true) {
                try {
                    stateChanged.wait();
                    break;
                } catch (InterruptedException e) {
                }
            }
        }
        glView.onPause();
        super.onPause();
    }

    //
    public GLGraphics getGLGraphics() {
        return glGraphics;
    }



}
