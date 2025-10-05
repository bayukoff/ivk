package ru.coolz.liquidsgame.util;

public class TubeIdGenerator {
    private static int id;

    public static int getNextId(){
        return id++;
    }
}
