package ru.coolz.liquidsgame.game;

import ru.coolz.liquidsgame.entity.Drop;
import ru.coolz.liquidsgame.entity.Tube;

import java.util.*;

public class Game {
    private Tube[] tubes;
    private final int colorCount;
    private final int tubeCapacity;

    public Game(int[][] initialState, int colorsAmount) {
        this.tubeCapacity = initialState[0].length;
        this.tubes = new Tube[initialState.length];
        this.colorCount = checkColorCount(colorsAmount, initialState.length);
        fillTubes();
    }

    private int checkColorCount(int colorsAmount, int totalTubes){
        if (colorsAmount >= totalTubes)
            throw new IllegalArgumentException("Количество цветов не может быть больше или равно количеству пробирок!");
        return colorsAmount;
    }

    private void fillTubes() {
        Random rand = new Random();
        for (int i = 0; i < tubes.length; i++) {
            var tube = new Tube(tubeCapacity);
            tubes[i] = tube;
        }
        for (int color = 0; color < colorCount; color++) {
            int dropsAdded = 0;
            while (dropsAdded < tubeCapacity) {
                var tubeIndex = rand.nextInt(colorCount);
                var tube = tubes[tubeIndex];
                if (!tube.isFull()) {
                    tube.addDrop(new Drop(color));
                    dropsAdded++;
                }
            }
        }
    }

    public List<int[]> solve(){
        Deque<State> states = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        printState(tubes);
        states.add(new State(tubes, List.of()));
        while (!states.isEmpty()) {
            var state = states.poll();
            var currentStateTubes = state.tubes();
            if (isSolved(currentStateTubes)) {
                printState(currentStateTubes);
                return state.moves();
            }
            var encodedState = encodeState(currentStateTubes);
            if (visited.contains(encodedState))
                continue;
            visited.add(encodedState);
            for (int i = 0; i < currentStateTubes.length; i++) {
                var src = currentStateTubes[i];
                for (int j = 0; j < currentStateTubes.length; j++) {
                    if (i == j || src.isEmpty()) continue;
                    var dst = currentStateTubes[j];
                    if (src.canPourTo(dst)){
                        var newStateTubes = new Tube[currentStateTubes.length];
                        for (int k = 0; k < currentStateTubes.length; k++) {
                            newStateTubes[k] = currentStateTubes[k].clone();
                        }
                        newStateTubes[i].pourTo(newStateTubes[j]);
                        List<int[]> moves = new ArrayList<>(state.moves());
                        moves.add(new int[]{src.getId(), dst.getId()});
                        var newState = new State(newStateTubes, moves);
                        states.add(newState);
                    }
                }
            }
        }

        return List.of();
    }

    private boolean isSolved(Tube[] tubes){
        int fullUniformTubes = 0;
        for (Tube t : tubes) {
            if (t.isFullSingleColor()) fullUniformTubes++;
        }
        return fullUniformTubes == colorCount;
    }

    private String encodeState(Tube[] tubes) {
        StringBuilder sb = new StringBuilder();
        for (Tube tube : tubes) {
            for (Drop drop : tube.getDrops()) {
                sb.append(drop.color());
            }
            sb.append("|");
        }
        return sb.toString();
    }

    public void printState() {
        for (int i = 0; i < tubes.length; i++) {
            System.out.println("Tube " + i + ": " + tubes[i]);
        }
    }

    public void printState(Tube[] tubes){
        for (int i = 0; i < tubes.length; i++) {
            System.out.println("Tube " + i + ": " + tubes[i]);
        }
    }
}
