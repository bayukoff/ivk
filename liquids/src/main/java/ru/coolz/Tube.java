package ru.coolz;

import java.util.Stack;

public class Tube {
    private final Stack<Drop> liquids;
    private final int capacity;

    public Tube(int capacity) {
        this.capacity = capacity;
        this.liquids = new Stack<>();
    }

    public boolean isEmpty() {
        return liquids.isEmpty();
    }

    public boolean isFull() {
        return liquids.size() >= capacity;
    }

    public Drop topDrop() {
        return isEmpty() ? null : liquids.peek();
    }

    public int topCount() {
        if (isEmpty()) return 0;
        Drop top = topDrop();
        int count = 0;
        for (int i = liquids.size() - 1; i >= 0; i--) {
            if (!liquids.get(i).color().equals(top.color())) break;
            count++;
        }
        return count;
    }

    public boolean canPourTo(Tube target) {
        if (this.isEmpty() || target.isFull()) return false;
        Drop top = this.topDrop();
        return target.isEmpty() || target.topDrop().color().equals(top.color());
    }

    public int pourTo(Tube target) {
        if (!canPourTo(target)) return 0;
        int maxPour = Math.min(this.topCount(), target.capacity - target.liquids.size());
        Drop color = this.topDrop();
        for (int i = 0; i < maxPour; i++) {
            this.liquids.pop();
            target.liquids.push(color);
        }
        return maxPour;
    }

    public void addDrop(Drop drop) {
        if (!isFull()) liquids.push(drop);
    }

    @Override
    public String toString() {
        return liquids.toString();
    }
}
