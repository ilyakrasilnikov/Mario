package com.gmail.krasilnikov.il.gamedev2d;

import android.util.FloatMath;
import android.util.Log;

import com.gmail.krasilnikov.il.framework.Game;
import com.gmail.krasilnikov.il.framework.Input;
import com.gmail.krasilnikov.il.framework.Screen;
import com.gmail.krasilnikov.il.framework.gl.Vertices;
import com.gmail.krasilnikov.il.framework.impl.GLGraphics;
import com.gmail.krasilnikov.il.framework.impl.GLGame;
import com.gmail.krasilnikov.il.framework.math.Vector2;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class CannonGravityTest extends GLGame{

    @Override
    public Screen getStartScreen() {
        return new CannonGravityScreen(this);
    }

    public class CannonGravityScreen extends Screen {

        float FRUSTUM_WIDTH = 9.6f;
        float FRUSTUM_HEIGHT = 6.4f;
        GLGraphics glGraphics;
        Vertices cannonVertices;
        Vertices ballVertices;
        Vector2 cannonPos = new Vector2(0.5f, 0.5f);
        float cannonAngle = 0;
        Vector2 touchPos = new Vector2();
        Vector2 ballPos = new Vector2(0, 0);
        Vector2 ballVelocity = new Vector2(0, 0);
        Vector2 gravity = new Vector2(0, -10);

        public CannonGravityScreen(Game game) {
            super(game);
            glGraphics = ((GLGame)game).getGLGraphics();
            cannonVertices = new Vertices(glGraphics, 3, 0, true, false);
            cannonVertices.setVertices(new float[]{-0.5f, -0.5f, 1, 1, 1, 1,
                                                    0.5f,  0.0f, 0, 0, 1, 1,
                                                   -0.5f,  0.5f, 1, 1, 1, 1}, 0, 18);
            ballVertices = new Vertices(glGraphics, 4, 6, false, false);
            ballVertices.setVertices(new float[] {-0.1f, -0.1f,
                                                   0.1f, -0.1f,
                                                   0.1f,  0.1f,
                                                  -0.1f,  0.1f}, 0, 8);
            ballVertices.setIndices(new short[] {0, 1, 2, 2, 3, 0}, 0, 6);
        }

        @Override
        public void update(float deltaTime) {
            List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
            game.getInput().getKeyEvents();
            int len = touchEvents.size();
            for (int i = 0; i < len; i++) {
                Input.TouchEvent event = touchEvents.get(i);
                touchPos.x = (event.x / (float) glGraphics.getWidth()) * FRUSTUM_WIDTH;
                touchPos.y = (1 - event.y / (float) glGraphics.getHeight()) * FRUSTUM_HEIGHT;
                cannonAngle = touchPos.sub(cannonPos).angle();
                if (event.type == Input.TouchEvent.TOUCH_UP) {
                    float radians = cannonAngle * Vector2.TO_RADIANS;
                    float ballSpeed = touchPos.len() * 2;
                    ballPos.set(cannonPos);
                    ballVelocity.x = FloatMath.cos(radians) * ballSpeed;
                    ballVelocity.y = FloatMath.sin(radians) * ballSpeed;
                }
            }
            ballVelocity.add(gravity.x * deltaTime, gravity.y * deltaTime);
            ballPos.add(ballVelocity.x * deltaTime, ballVelocity.y * deltaTime);
        }

        @Override
        public void present(float deltaTime) {
            GL10 gl = glGraphics.getGL();
            gl.glViewport(0, 0, glGraphics.getWidth(), glGraphics.getHeight());
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
            gl.glMatrixMode(GL10.GL_PROJECTION);
            gl.glLoadIdentity();
            gl.glOrthof(0, FRUSTUM_WIDTH, 0, FRUSTUM_HEIGHT, 1, -1);
            gl.glMatrixMode(GL10.GL_MODELVIEW);
            gl.glLoadIdentity();
            gl.glTranslatef(cannonPos.x, cannonPos.y, 0);
            gl.glRotatef(cannonAngle, 0, 0, 1);
            cannonVertices.bind();
            cannonVertices.draw(GL10.GL_TRIANGLES, 0, 3);
            cannonVertices.unbind();
            gl.glLoadIdentity();
            gl.glTranslatef(ballPos.x, ballPos.y, 0);
            gl.glColor4f(0, 0, 1, 1);
            ballVertices.bind();
            ballVertices.draw(GL10.GL_TRIANGLES, 0, 6);
            ballVertices.unbind();
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
