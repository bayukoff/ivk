package ru.coolz;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Game {
    private final Tube[] tubes;

    public Game(Map<Integer, List<Drop>> initialState, int tubeCapacity, int totalTubes) {
        tubes = new Tube[totalTubes];
        for (int i = 0; i < totalTubes; i++) {
            tubes[i] = new Tube(tubeCapacity);
            List<Drop> drops = initialState.getOrDefault(i, new ArrayList<>());
            for (Drop drop : drops) {
                tubes[i].addDrop(drop);
            }
        }
    }

    public Tube getTube(int index) {
        return tubes[index];
    }

    public boolean move(int from, int to) {
        Tube source = tubes[from];
        Tube target = tubes[to];
        if (source.canPourTo(target)) {
            source.pourTo(target);
            return true;
        }
        return false;
    }

    public void printState() {
        for (int i = 0; i < tubes.length; i++) {
            System.out.println("Tube " + i + ": " + tubes[i]);
        }
    }
}
