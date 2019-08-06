package com.javarush.games.racer;

import com.javarush.engine.cell.*;
import com.javarush.games.racer.road.RoadManager;

public class RacerGame extends Game {

    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;
    public static final int CENTER_X = WIDTH / 2;
    public static final int ROADSIDE_WIDTH = 14;
    private static final int RACE_GOAL_CARS_COUNT = 30;

    private RoadMarking roadMarking;
    private PlayerCar player;
    private RoadManager roadManager;
    private FinishLine finishLine;
    private ProgressBar progressBar;
    private boolean isGameStopped;
    private int score;

    @Override
    public void initialize() {
        showGrid(false);
        setScreenSize(WIDTH, HEIGHT);
        createGame();
    }

    private void createGame() {
        roadMarking = new RoadMarking();
        player = new PlayerCar();
        roadManager = new RoadManager();
        finishLine = new FinishLine();
        progressBar = new ProgressBar(RACE_GOAL_CARS_COUNT);
        drawScene();
        setTurnTimer(40);
        score = 3500;
        isGameStopped = false;
    }

    private void drawScene() {
        drawField();
        progressBar.draw(this);
        roadManager.draw(this);
        roadMarking.draw(this);
        player.draw(this);
        finishLine.draw(this);
    }

    private void drawField() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (x == CENTER_X) {
                    setCellColor(x, y, Color.WHITE);
                } else if (x >= ROADSIDE_WIDTH && x < (WIDTH - ROADSIDE_WIDTH)) {
                    setCellColor(x, y, Color.DIMGRAY);
                } else {
                    setCellColor(x, y, Color.GREEN);
                }
            }
        }
    }

    @Override
    public void setCellColor(int x, int y, Color color) {
        if (x < 0 || y < 0 || x >= HEIGHT || y >= WIDTH) {
            return;
        }
        super.setCellColor(x, y, color);
    }

    private void moveAll() {
        roadMarking.move(player.speed);
        player.move();
        roadManager.move(player.speed);
        finishLine.move(player.speed);
        progressBar.move(roadManager.getPassedCarsCount());
    }

    @Override
    public void onTurn(int step) {
        score -= 5;
        if (roadManager.checkCrush(player)) {
            gameOver();
            drawScene();
            return;
        }
        if (roadManager.getPassedCarsCount() >= RACE_GOAL_CARS_COUNT) {
            finishLine.show();
        }
        if (finishLine.isCrossed(player)) {
            win();
            drawScene();
            return;
        }
        moveAll();
        roadManager.generateNewRoadObjects(this);
        setScore(score);
        drawScene();
    }

    @Override
    public void onKeyPress(Key key) {
        if (key == Key.RIGHT) {
            player.setDirection(Direction.RIGHT);
        }
        if (key == Key.LEFT) {
            player.setDirection(Direction.LEFT);
        }
        if (key == Key.UP) {
            player.speed = 2;
        }
        if (key == Key.SPACE && isGameStopped == true) {
            createGame();
        }
    }

    @Override
    public void onKeyReleased(Key key) {
        if (key == Key.RIGHT && player.getDirection() == Direction.RIGHT) {
            player.setDirection(Direction.NONE);
        }
        if (key == Key.LEFT && player.getDirection() == Direction.LEFT) {
            player.setDirection(Direction.NONE);
        }
        if (key == Key.UP) {
            player.speed = 1;
        }
    }

    private void gameOver() {
        isGameStopped = true;
        showMessageDialog(Color.CRIMSON, "GAME OVER", Color.ALICEBLUE, 42);
        stopTurnTimer();
        player.stop();
    }

    private void win() {
        isGameStopped = true;
        showMessageDialog(Color.RED, "YOU WINNER!!!", Color.CHARTREUSE, 42);
        stopTurnTimer();

    }
}
