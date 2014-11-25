package com.gmail.krasilnikov.il.framework.impl;

import android.view.KeyEvent;
import android.view.View;

import com.gmail.krasilnikov.il.framework.Input;
import com.gmail.krasilnikov.il.framework.Pool;

import java.util.ArrayList;
import java.util.List;

public class KeyboardHandler implements View.OnKeyListener {

    boolean[] pressedKeys = new boolean[128];
    Pool<Input.KeyEvent> keyEventPool;
    List<Input.KeyEvent> keyEventsBuffer = new ArrayList<Input.KeyEvent>();
    List<Input.KeyEvent> keyEvents = new ArrayList<Input.KeyEvent>();

    public KeyboardHandler(View view) {
        PoolObjectFactory<Input.KeyEvent> factory = new PoolObjectFactory<Input.KeyEvent>() {
            @Override
            public Input.KeyEvent CreateObject() {
                return new Input.KeyEvent();
            }
        };

        keyEventPool = new Pool<Input.KeyEvent>(factory, 100);
        view.setOnKeyListener(this);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }

    @Override
    public boolean onKey(View view, int keyCode, android.view.KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_MULTIPLE) {
           return false;
        };
        synchronized (this) {
            Input.KeyEvent keyEvent = keyEventPool.newObject();
            keyEvent.keyCode = keyCode;
            keyEvent.keyChar = (char) event.getUnicodeChar();
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                keyEvent.type = Input.KeyEvent.KEY_DOWN;
                if (keyCode > 0 && keyCode < 127)
                    pressedKeys[keyCode] = true;
            }
            if (event.getAction() == KeyEvent.ACTION_UP) {
                keyEvent.type = Input.KeyEvent.KEY_UP;
                if (keyCode > 0 && keyCode< 127)
                    pressedKeys[keyCode] = false;
            }
            keyEventsBuffer.add(keyEvent);
        }
        return false;
    }

    public boolean isKeyPressed(int keyCode) {
        if (keyCode < 0 || keyCode > 127)
            return false;
        return pressedKeys[keyCode];
    }

    public List<Input.KeyEvent> getKeyEvents() {
        synchronized (this) {
            int len = keyEvents.size();
            for (int i = 0; i < len; i++)
                keyEventPool.free(keyEvents.get(i));
            keyEvents.clear();
            keyEvents.addAll(keyEventsBuffer);
            keyEventsBuffer.clear();
            return keyEvents;
        }
    }

}
