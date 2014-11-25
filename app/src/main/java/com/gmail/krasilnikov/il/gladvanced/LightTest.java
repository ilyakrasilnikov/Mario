package com.gmail.krasilnikov.il.gladvanced;

import android.opengl.GLU;

import com.gmail.krasilnikov.il.framework.Game;
import com.gmail.krasilnikov.il.framework.Screen;
import com.gmail.krasilnikov.il.framework.gl.AmbientLight;
import com.gmail.krasilnikov.il.framework.gl.DirectionalLight;
import com.gmail.krasilnikov.il.framework.gl.Material;
import com.gmail.krasilnikov.il.framework.gl.PointLight;
import com.gmail.krasilnikov.il.framework.gl.Texture;
import com.gmail.krasilnikov.il.framework.gl.Vertices3;
import com.gmail.krasilnikov.il.framework.impl.GLGame;
import com.gmail.krasilnikov.il.framework.impl.GLScreen;

import javax.microedition.khronos.opengles.GL10;

public class LightTest extends GLGame {

    @Override
    public Screen getStartScreen() {
        return new LightScreen(this);
    }

    public class LightScreen extends GLScreen {

        float angle;
        Vertices3 cube;
        Texture texture;
        AmbientLight ambientLight;
        PointLight pointLight;
        DirectionalLight directionalLight;
        Material material;

        public LightScreen(Game game) {
            super(game);
            cube = createCube();
            texture = new Texture(glGame, "crate.png");
            ambientLight = new AmbientLight();
            ambientLight.setColor(0, 0.2f, 0, 1);
            pointLight = new PointLight();
            pointLight.setDiffuse(1, 0, 0, 1);
            pointLight.setPosition(3, 3, 0);
            directionalLight = new DirectionalLight();
            directionalLight.setDiffuse(0, 0, 1, 1);
            directionalLight.setDirection(1, 0, 0);
            material = new Material();
        }

        private Vertices3 createCube() {
            float[] vertices = {-0.5f, -0.5f,  0.5f, 0, 1, 0, 0, 1,
                                 0.5f, -0.5f,  0.5f, 1, 1, 0, 0, 1,
                                 0.5f,  0.5f,  0.5f, 1, 0, 0, 0, 1,
                                -0.5f,  0.5f,  0.5f, 0, 0, 0, 0, 1,

                                 0.5f, -0.5f,  0.5f, 0, 1, 1, 0, 0,
                                 0.5f, -0.5f, -0.5f, 1, 1, 1, 0, 0,
                                 0.5f,  0.5f, -0.5f, 1, 0, 1, 0, 0,
                                 0.5f,  0.5f,  0.5f, 0, 0, 1, 0, 0,

                                 0.5f, -0.5f, -0.5f, 0, 1, 0, 0, -1,
                                -0.5f, -0.5f, -0.5f, 1, 1, 0, 0, -1,
                                -0.5f,  0.5f, -0.5f, 1, 0, 0, 0, -1,
                                 0.5f,  0.5f, -0.5f, 0, 0, 0, 0, -1,

                                -0.5f, -0.5f, -0.5f, 0, 1, -1, 0, 0,
                                -0.5f, -0.5f,  0.5f, 1, 1, -1, 0, 0,
                                -0.5f,  0.5f,  0.5f, 1, 0, -1, 0, 0,
                                -0.5f,  0.5f, -0.5f, 0, 0, -1, 0, 0,

                                -0.5f,  0.5f,  0.5f, 0, 1, 0, 1, 0,
                                 0.5f,  0.5f,  0.5f, 1, 1, 0, 1, 0,
                                 0.5f,  0.5f, -0.5f, 1, 0, 0, 1, 0,
                                -0.5f,  0.5f, -0.5f, 0, 0, 0, 1, 0,

                                -0.5f, -0.5f,  0.5f, 0, 1, 0, -1, 0,
                                 0.5f, -0.5f,  0.5f, 1, 1, 0, -1, 0,
                                 0.5f, -0.5f, -0.5f, 1, 0, 0, -1, 0,
                                -0.5f, -0.5f, -0.5f, 0, 0, 0, -1, 0};
            short[] indices = {0, 1, 3, 1, 2, 3,
                               4, 5, 7, 5, 6, 7,
                               8, 9, 11, 9, 10, 11,
                               12, 13, 15, 13, 14, 15,
                               16, 17, 19, 17, 18, 19,
                               20, 21, 23, 21, 22, 23};
            Vertices3 cube = new Vertices3(glGraphics, 24, 36, false, true, true);
            cube.setVertices(vertices, 0, vertices.length);
            cube.setIndices(indices, 0, indices.length);
            return cube;
        };

        @Override
        public void update(float deltaTime) {
            angle +=  deltaTime * 20;
        }

        @Override
        public void present(float deltaTime) {
            GL10 gl = glGraphics.getGL();
            gl.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
            gl.glEnable(GL10.GL_DEPTH_TEST);
            gl.glViewport(0, 0, glGraphics.getWidth(), glGraphics.getHeight());
            gl.glMatrixMode(GL10.GL_PROJECTION);
            gl.glLoadIdentity();
            GLU.gluPerspective(gl, 67, glGraphics.getWidth() / (float)glGraphics.getHeight(), 0.1f, 10f);
            gl.glMatrixMode(GL10.GL_MODELVIEW);
            gl.glLoadIdentity();
            GLU.gluLookAt(gl, 0, 1, 3, 0, 0, 0, 0, 1, 0);
            gl.glEnable(GL10.GL_LIGHTING);
            ambientLight.enable(gl);
            pointLight.enable(gl, GL10.GL_LIGHT0);
            directionalLight.enable(gl, GL10.GL_LIGHT1);
            material.enable(gl);
            gl.glEnable(GL10.GL_TEXTURE_2D);
            texture.bind();
            gl.glRotatef(angle, 0, 1, 0);
            cube.bind();
            cube.draw(GL10.GL_TRIANGLES, 0, 6 * 2 * 3);
            cube.unbind();
            pointLight.disable(gl);
            directionalLight.disable(gl);
            gl.glDisable(GL10.GL_TEXTURE_2D);
            gl.glDisable(GL10.GL_DEPTH_TEST);
        }

        @Override
        public void pause() {

        }

        @Override
        public void resume() {
            texture.reload();
        }

        @Override
        public void dispose() {

        }
    }
}
