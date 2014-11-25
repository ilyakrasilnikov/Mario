package com.gmail.krasilnikov.il.gamedev2d;

import android.graphics.Color;

import com.gmail.krasilnikov.il.framework.math.Rectangle;
import com.gmail.krasilnikov.il.framework.math.Vector2;

public class GameObject {
    public final Vector2 position;
    public final Rectangle bounds;
    public static int color;

    public GameObject(float x, float y, float width, float height) {
        this.position = new Vector2(x, y);
        this.bounds = new Rectangle(x - width/2, y - height/2, width, height);
        this.color =  Color.BLUE;
    }

    public GameObject(float x, float y, float width, float height, int color) {
        this.position = new Vector2(x, y);
        this.bounds = new Rectangle(x - width/2, y - height/2, width, height);
        this.color = color;
    }
}
