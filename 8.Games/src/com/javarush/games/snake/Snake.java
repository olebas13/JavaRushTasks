package com.javarush.games.snake;

import java.util.ArrayList;
import java.util.List;

public class Snake {

    private List<GameObject> snakeParts = new ArrayList<>();

    public Snake(int x, int y) {
        GameObject firstPart = new GameObject(x, y);
        GameObject secondPart = new GameObject(x + 1, y);
        GameObject thirdPart = new GameObject(x + 2, y);
        snakeParts.add(firstPart);
        snakeParts.add(secondPart);
        snakeParts.add(thirdPart);
    }
}
