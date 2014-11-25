package com.gmail.krasilnikov.il.framework;

import com.gmail.krasilnikov.il.framework.impl.PoolObjectFactory;

import java.util.ArrayList;
import java.util.List;

public class Pool<T> {
    private final List<T> freeObjects;
    private final PoolObjectFactory<T> factory;
    private final int maxSize;

    public Pool(PoolObjectFactory<T> factory, int maxSize) {
        this.factory = factory;
        this.maxSize = maxSize;
        this.freeObjects = new ArrayList<T>(maxSize);
    }

    public T newObject() {
        T object = null;
        if (freeObjects.size() == 0)
            object = factory.CreateObject();
        else
            object = freeObjects.remove(freeObjects.size() - 1);
        return object;
    }

    public void free(T object) {
        if (freeObjects.size() < maxSize)
            freeObjects.add(object);
    }


}
