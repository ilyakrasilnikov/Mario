package com.gmail.krasilnikov.il.droidinvaders;

import com.gmail.krasilnikov.il.framework.GameObject3D;

public class Shield extends GameObject3D{
    static float SHIELD_RADIUS = 0.5f;

    public Shield(float x, float y, float z) {
        super(x, y, z, SHIELD_RADIUS);
    }
}
