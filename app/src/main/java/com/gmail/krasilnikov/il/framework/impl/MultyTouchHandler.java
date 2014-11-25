package com.gmail.krasilnikov.il.framework.impl;

import android.view.MotionEvent;
import android.view.View;

import com.gmail.krasilnikov.il.framework.Input;
import com.gmail.krasilnikov.il.framework.Pool;

import java.util.ArrayList;
import java.util.List;

public class MultyTouchHandler implements TouchHandler{
    boolean[] isTouched = new boolean[20];
    int[] touchX = new int[20];
    int[] touchY = new int[20];
    Pool<Input.TouchEvent> touchEventPool;
    List<Input.TouchEvent> touchEvents = new ArrayList<Input.TouchEvent>();
    List<Input.TouchEvent> touchEventBuffer = new ArrayList<Input.TouchEvent>();
    float scaleX;
    float scaleY;

    public MultyTouchHandler(View view, float scaleX, float scaleY) {
        PoolObjectFactory<Input.TouchEvent> factory = new PoolObjectFactory<Input.TouchEvent>() {
            @Override
            public Input.TouchEvent CreateObject() {
                return new Input.TouchEvent();
            }
        };
        touchEventPool = new Pool<Input.TouchEvent>(factory, 100);
        view.setOnTouchListener(this);
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    @Override
    public boolean isTouchDown(int pointer) {
        synchronized (this) {
            if (pointer < 0 || pointer >= 20)
                return false;
            else
                return isTouched[pointer];
        }
    }

    @Override
    public int getTouchX(int pointer) {
        synchronized (this) {
            if (pointer < 0 || pointer >= 20)
                return 0;
            else
                return touchX[pointer];
        }
    }

    @Override
    public int getTouchY(int pointer) {
        synchronized (this) {
            if (pointer < 0 || pointer >= 20)
                return 0;
            else
                return touchY[pointer];
        }
    }

    @Override
    public List<Input.TouchEvent> getTouchEvents() {
        synchronized (this) {
            int len = touchEvents.size();
            for (int i = 0; i < len; i++)
                touchEventPool.free(touchEvents.get(i));
            touchEvents.clear();
            touchEvents.addAll(touchEventBuffer);
            touchEventBuffer.clear();
            return touchEvents;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        synchronized (this) {
            int action = event.getAction() & MotionEvent.ACTION_MASK;
            int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_ID_MASK) >> MotionEvent.ACTION_POINTER_ID_SHIFT;
            int pointerId = event.getPointerId(pointerIndex);
            Input.TouchEvent touchEvent;
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_DOWN:
                    touchEvent = touchEventPool.newObject();
                    touchEvent.type = Input.TouchEvent.TOUCH_DOWN;
                    touchEvent.pointer = pointerId;
                    touchEvent.x = touchX[pointerId] = (int) (event.getX(pointerIndex) * scaleX);
                    touchEvent.y = touchY[pointerId] = (int) (event.getY(pointerIndex) * scaleY);
                    isTouched[pointerId] = true;
                    touchEventBuffer.add(touchEvent);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                case MotionEvent.ACTION_CANCEL:
                    touchEvent = touchEventPool.newObject();
                    touchEvent.type = Input.TouchEvent.TOUCH_UP;
                    touchEvent.pointer = pointerId;
                    touchEvent.x = touchX[pointerId] = (int) (event.getX(pointerIndex) * scaleX);
                    touchEvent.y = touchY[pointerId] = (int) (event.getY(pointerIndex) * scaleY);
                    isTouched[pointerId] = false;
                    touchEventBuffer.add(touchEvent);
                    break;
                case MotionEvent.ACTION_MOVE:
                    int pointerCount = event.getPointerCount();
                    for (int i = 0; i < pointerCount; i++) {
                        pointerIndex = i;
                        pointerId = event.getPointerId(pointerIndex);
                        touchEvent = touchEventPool.newObject();
                        touchEvent.type = Input.TouchEvent.TOUCH_DRAGGED;
                        touchEvent.pointer = pointerId;
                        touchEvent.x = touchX[pointerId] = (int)(event.getX(pointerIndex) * scaleX);
                        touchEvent.y = touchY[pointerId] = (int)(event.getY(pointerIndex) * scaleY);
                        touchEventBuffer.add(touchEvent);
                    }
                    break;
            }
            return true;
        }
    }
}
