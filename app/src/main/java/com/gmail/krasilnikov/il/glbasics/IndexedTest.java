package com.gmail.krasilnikov.il.glbasics;

import com.gmail.krasilnikov.il.framework.Game;
import com.gmail.krasilnikov.il.framework.Screen;
import com.gmail.krasilnikov.il.framework.gl.Texture;
import com.gmail.krasilnikov.il.framework.gl.Vertices;
import com.gmail.krasilnikov.il.framework.impl.GLGraphics;
import com.gmail.krasilnikov.il.framework.impl.GLGame;

import javax.microedition.khronos.opengles.GL10;

public class IndexedTest extends GLGame{

    @Override
    public Screen getStartScreen() {
        return new IndexedScreen(this);
    }

    class IndexedScreen extends Screen {
        GLGraphics glGraphics;
        Texture texture;
        Vertices vertices;

        public IndexedScreen(Game game) {
            super(game);
            glGraphics = ((GLGame)game).getGLGraphics();
            vertices = new Vertices(glGraphics, 4, 6, true, true);
            vertices.setVertices(new float[] {100.0f, 100.0f, 0.0f, 0.0f, 1.0f, 0.5f, 0.0f, 1.0f,
                                              228.0f, 100.0f, 1.0f, 0.0f, 0.0f, 0.5f, 1.0f, 1.0f,
                                              228.0f, 228.0f, 0.0f, 1.0f, 0.0f, 0.5f, 1.0f, 0.0f,
                                              100.0f, 228.0f, 1.0f, 0.0f, 0.0f, 0.5f, 0.0f, 0.0f},
                                 0, 32);
            vertices.setIndices(new short[] {0, 1, 2,
                                             2, 3, 0},
                                0, 6);
            texture = new Texture((GLGame)game, "bobrgb888.png");

        }

        @Override
        public void update(float deltaTime) {
            game.getInput().getTouchEvents();
            game.getInput().getKeyEvents();
        }

        @Override
        public void present(float deltaTime) {
            GL10 gl = glGraphics.getGL();
            gl.glViewport(0, 0, glGraphics.getWidth(), glGraphics.getHeight());
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
            gl.glMatrixMode(GL10.GL_PROJECTION);
            gl.glLoadIdentity();
            gl.glOrthof(0, 320, 0, 480, 1, -1);
            gl.glEnable(GL10.GL_TEXTURE_2D);
            texture.bind();
            vertices.draw(GL10.GL_TRIANGLES, 0, 6);
        }

        @Override
        public void pause() {

        }

        @Override
        public void resume() {

        }

        @Override
        public void dispose() {

        }
    }
}
