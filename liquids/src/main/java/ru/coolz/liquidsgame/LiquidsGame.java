package ru.coolz.liquidsgame;

import ru.coolz.liquidsgame.game.Game;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

public class LiquidsGame {
    public static void main(String[] args) {
        var game = initGame();
        var moves = game.solve();
        moves.forEach(move -> System.out.print(Arrays.toString(move) + " "));
    }

    private static Game initGame(){
        var properties = loadProperties("liquids/config/game.properties");
        var colorCount = Integer.parseInt(properties.getProperty("game.color.count"));
        var tubeCount = Integer.parseInt(properties.getProperty("game.tube.count"));
        var tubeCapacity = Integer.parseInt(properties.getProperty("game.tube.capacity"));

        int[][] initialState = new int[tubeCount][tubeCapacity];
        return new Game(initialState, colorCount);
    }

    private static Properties loadProperties(String filename) {
        try (FileInputStream fis = new FileInputStream(filename)) {
            var props = new Properties();
            props.load(fis);
            return props;
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
