package com.gmail.krasilnikov.il.framework;

import java.io.IOException;

public interface Audio {
    public Music newMusic (String filename); // throws IOException;
    public Sound newSound (String filename); // throws IOException;
}
