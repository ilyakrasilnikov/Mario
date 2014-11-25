package com.gmail.krasilnikov.il.framework;
import com.gmail.krasilnikov.il.framework.Graphics.PixmapFormat;
public interface Pixmap {
    public int getWidth();
    public int getHeight();
    public PixmapFormat getFormat();
    public void dispose();
}
