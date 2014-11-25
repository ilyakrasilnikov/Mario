package com.gmail.krasilnikov.il.framework;

import com.gmail.krasilnikov.il.framework.math.Vector3;

public class DynamicGameObject3D extends GameObject3D{

    public final Vector3 velocity;
    public final Vector3 accel;


    public DynamicGameObject3D(float x, float y, float z, float radius) {
        super(x, y, z, radius);
        this.velocity = new Vector3();
        this.accel = new Vector3();
    }
}
