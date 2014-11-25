package com.gmail.krasilnikov.il.framework;

import com.gmail.krasilnikov.il.framework.math.Sphere;
import com.gmail.krasilnikov.il.framework.math.Vector3;

public class GameObject3D {

    public final Vector3 position;
    public final Sphere bounds;

    public GameObject3D(float x, float y, float z, float radius) {
        this.position = new Vector3(x, y, z);
        this.bounds = new Sphere(x, y, z, radius);
    }
}
