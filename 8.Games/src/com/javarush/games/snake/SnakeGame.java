package com.javarush.games.snake;

import com.javarush.engine.cell.*;

public class SnakeGame extends Game {

    public static final int WIDTH = 15;
    public static final int HEIGHT = 15;

    private Snake snake;
    private Apple apple;
    private int turnDelay;

    public void initialize() {
        setScreenSize(WIDTH, HEIGHT);
        createGame();
    }

    public void onTurn(int step) {
        snake.move(apple);
        if (!apple.isAlive) {
            createNewApple();
        }
        drawScene();
    }

    public void onKeyPress(Key key) {
        if (key == Key.LEFT) {
            snake.setDirection(Direction.LEFT);
        }
        if (key == Key.RIGHT) {
            snake.setDirection(Direction.RIGHT);
        }
        if (key == Key.UP) {
            snake.setDirection(Direction.UP);
        }
        if (key == Key.DOWN) {
            snake.setDirection(Direction.DOWN);
        }
    }

    private void createGame() {
        snake = new Snake(WIDTH / 2, HEIGHT / 2);
        createNewApple();
        drawScene();
        turnDelay = 300;
        setTurnTimer(turnDelay);
    }

    private void drawScene() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                setCellValueEx(x, y, Color.DARKSEAGREEN, "");
            }
        }
        snake.draw(this);
        apple.draw(this);
    }

    private void createNewApple() {
        int appleX = getRandomNumber(WIDTH);
        int appleY = getRandomNumber(HEIGHT);
        apple = new Apple(appleX, appleY);
    }
    
}
