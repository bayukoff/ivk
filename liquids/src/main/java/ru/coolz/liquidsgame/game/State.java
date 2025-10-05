package ru.coolz.liquidsgame.game;

import ru.coolz.liquidsgame.entity.Tube;

import java.util.List;

public record State(Tube[] tubes, List<int[]> moves){}
