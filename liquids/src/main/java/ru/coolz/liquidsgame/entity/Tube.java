package ru.coolz.liquidsgame.entity;

import ru.coolz.liquidsgame.util.TubeIdGenerator;

import java.util.*;

public class Tube implements Cloneable{

    private final LinkedList<Drop> drops;
    private final int capacity;
    private int id;

    public Tube(int capacity) {
        this.capacity = capacity;
        this.drops = new LinkedList<>();
        this.id = TubeIdGenerator.getNextId();
    }

    private Tube(int capacity, LinkedList<Drop> drops, int id) {
        this.capacity = capacity;
        this.drops = drops;
        this.id = id;
    }

    public Deque<Drop> getDrops() {
        return drops;
    }

    public boolean isEmpty() {
        return drops.isEmpty();
    }

    public boolean isFull() {
        return drops.size() >= capacity;
    }

    public Drop topDrop() {
        return isEmpty() ? null : drops.peek();
    }

    public int topCount() {
        if (isEmpty()) return 0;
        Drop top = topDrop();
        int count = 0;
        for (Drop drop : drops) {
            if (drop.color() != top.color()) break;
            count++;
        }
        return count;
    }

    public boolean isFullSingleColor(){
        return topCount() == capacity;
    }

    public boolean canPourTo(Tube target) {
        if (this.isEmpty() || target.isFull()) return false;
        Drop top = this.topDrop();
        return target.isEmpty() || target.topDrop().color() == top.color();
    }

    public int pourTo(Tube target) {
        if (!canPourTo(target)) return 0;
        int maxPour = Math.min(this.topCount(), target.capacity - target.drops.size());
        Drop color = this.topDrop();
        for (int i = 0; i < maxPour; i++) {
            this.drops.pop();
            target.drops.push(color);
        }
        return maxPour;
    }

    public void addDrop(Drop drop) {
        if (!isFull()) drops.push(drop);
    }

    @Override
    public Tube clone() {
        var clonedDrops = new LinkedList<Drop>();
        for (Drop d : drops) {
            clonedDrops.add(d.clone());
        }
        return new Tube(capacity, clonedDrops, id);
    }

    @Override
    public String toString() {
        return drops.toString();
    }

    public int getId() {
        return id;
    }

}
