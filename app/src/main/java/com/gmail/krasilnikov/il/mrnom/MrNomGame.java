package com.gmail.krasilnikov.il.mrnom;

import com.gmail.krasilnikov.il.framework.Screen;
import com.gmail.krasilnikov.il.framework.impl.AndroidGame;
import com.gmail.krasilnikov.il.mrnom.LoadingScreen;

public class MrNomGame extends AndroidGame {

    @Override
    public Screen getStartScreen() {
        return new LoadingScreen(this);
    }

}
