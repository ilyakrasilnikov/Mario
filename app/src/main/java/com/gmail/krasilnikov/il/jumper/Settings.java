package com.gmail.krasilnikov.il.jumper;

import com.gmail.krasilnikov.il.framework.FileIO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Settings {
    public static boolean soundEnabled = true;
    public final static int[] highScores = new int[] {100, 80, 50, 30, 10};
    public final static String file = ".superjumper";

    public static void load(FileIO files) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(files.readFile(file)));
            soundEnabled = Boolean.parseBoolean(in.readLine());
            for (int i = 0; i < 5; i++) {
                highScores[i] = Integer.parseInt(in.readLine());
            }
        } catch (IOException e) {
        } catch (NumberFormatException e) {
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
            }
        }
    }

    public static void save(FileIO files) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(files.writeFile(file)));
            out.write(Boolean.toString(soundEnabled));
            out.write("\n");
            for (int i = 0; i < 5; i++) {
                out.write(Integer.toString(highScores[i]));
                out.write("\n");
            }
        } catch (IOException e) {
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
            }
        }
    }

    public static void addScore(int score) {
        for (int i = 0; i < 5; i++) {
            if (highScores[i] < score) {
                for (int j = 4; j > i; j --)
                    highScores[j] = highScores[j - 1];
                highScores[i] = score;
                break;
            }
        }
    }

}
