package ru.coolz.liquidsgame.entity;

public final class Drop implements Cloneable {
    private final int color;

    public Drop(int color) {
        this.color = color;
    }

    public int color() {
        return color;
    }

    @Override
    public Drop clone() {
        return new Drop(color);
    }

    @Override
    public String toString() {
        return "Drop[" +
            "color=" + color + ']';
    }
}
