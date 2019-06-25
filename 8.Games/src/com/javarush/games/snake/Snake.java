package com.javarush.games.snake;

import com.javarush.engine.cell.*;

import java.util.ArrayList;
import java.util.List;

public class Snake {

    private static final String HEAD_SIGN = "\uD83D\uDC7E";
    private static final String BODY_SIGN = "\u26AB";

    private List<GameObject> snakeParts = new ArrayList<>();
    public boolean isAlive = true;
    private Direction direction = Direction.LEFT;

    public Snake(int x, int y) {
        GameObject firstPart = new GameObject(x, y);
        GameObject secondPart = new GameObject(x + 1, y);
        GameObject thirdPart = new GameObject(x + 2, y);
        snakeParts.add(firstPart);
        snakeParts.add(secondPart);
        snakeParts.add(thirdPart);
    }

    public void draw(Game game) {
        for (int i = 0; i < snakeParts.size(); i++) {
            if (isAlive) {
                if (i == 0) {
                    game.setCellValueEx(snakeParts.get(i).x, snakeParts.get(i).y, Color.NONE, HEAD_SIGN, Color.BLACK, 75);
                } else {
                    game.setCellValueEx(snakeParts.get(i).x, snakeParts.get(i).y, Color.NONE, BODY_SIGN, Color.BLACK, 75);
                }
            } else {
                if (i == 0) {
                    game.setCellValueEx(snakeParts.get(i).x, snakeParts.get(i).y, Color.NONE, HEAD_SIGN, Color.RED, 75);
                } else {
                    game.setCellValueEx(snakeParts.get(i).x, snakeParts.get(i).y, Color.NONE, BODY_SIGN, Color.RED, 75);
                }
            }
        }
    }

    public void move(Apple apple) {
        GameObject newHead = createNewHead();
        if (newHead.x >= SnakeGame.HEIGHT || newHead.x < 0 || newHead.y < 0 || newHead.y >= SnakeGame.WIDTH) {
            isAlive = false;
        } else if (newHead.x == apple.x && newHead.y == apple.y) {
            apple.isAlive = false;
            if (checkCollision(newHead)) {
                isAlive = false;
            }
            snakeParts.add(0, newHead);
        } else {
            if (checkCollision(newHead)) {
                isAlive = false;
            } else {
                snakeParts.add(0, newHead);
                removeTail();
            }

        }
    }

    public GameObject createNewHead() {
        GameObject newObject = null;
        if (direction == Direction.UP) {
            newObject = new GameObject(snakeParts.get(0).x, snakeParts.get(0).y - 1);
        } else if (direction == Direction.DOWN) {
            newObject = new GameObject(snakeParts.get(0).x, snakeParts.get(0).y + 1);
        } else if (direction == Direction.RIGHT) {
            newObject = new GameObject(snakeParts.get(0).x + 1, snakeParts.get(0).y);
        } else if (direction == Direction.LEFT) {
            newObject = new GameObject(snakeParts.get(0).x - 1, snakeParts.get(0).y);
        }
        return newObject;
    }

    public void removeTail() {
        snakeParts.remove(snakeParts.size() - 1);
    }

    public boolean checkCollision(GameObject go) {
        for (GameObject snakePart : snakeParts) {
            if (go.x == snakePart.x && go.y == snakePart.y) {
                return true;
            }
        }
        return false;
    }

    public void setDirection(Direction direction) {
        if (direction == Direction.DOWN && this.direction == Direction.UP) {
            return;
        }
        if (direction == Direction.UP && this.direction == Direction.DOWN) {
            return;
        }
        if (direction == Direction.RIGHT && this.direction == Direction.LEFT) {
            return;
        }
        if (direction == Direction.LEFT && this.direction == Direction.RIGHT) {
            return;
        }
        this.direction = direction;
    }
}
