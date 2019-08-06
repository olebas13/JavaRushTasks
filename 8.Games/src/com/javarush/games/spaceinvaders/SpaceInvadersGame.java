package com.javarush.games.spaceinvaders;

import com.javarush.engine.cell.*;
import com.javarush.games.spaceinvaders.gameobjects.Bullet;
import com.javarush.games.spaceinvaders.gameobjects.EnemyFleet;
import com.javarush.games.spaceinvaders.gameobjects.PlayerShip;
import com.javarush.games.spaceinvaders.gameobjects.Star;

import java.util.ArrayList;
import java.util.List;

public class SpaceInvadersGame extends Game {

    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;
    public static final int COMPLEXITY = 5;

    private List<Star> stars;
    private List<Bullet> enemyBullets;
    private EnemyFleet enemyFleet;
    private PlayerShip playerShip;
    private boolean isGameStopped;
    private int animationsCount;

    @Override
    public void initialize() {
        setScreenSize(WIDTH, HEIGHT);
        createGame();
    }

    @Override
    public void onTurn(int step) {
        moveSpaceObjects();
        check();
        Bullet fireEnemyResult = enemyFleet.fire(this);
        if (fireEnemyResult != null) {
            enemyBullets.add(fireEnemyResult);
        }
        drawScene();
    }

    private void createGame() {
        setTurnTimer(40);
        createStars();
        enemyFleet = new EnemyFleet();
        enemyBullets = new ArrayList<Bullet>();
        playerShip = new PlayerShip();
        isGameStopped = false;
        animationsCount = 0;
        drawScene();
    }

    private void drawScene() {
        drawField();
        playerShip.draw(this);
        for (Bullet bullet : enemyBullets) {
            bullet.draw(this);
        }
        enemyFleet.draw(this);
    }

    private void drawField() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                setCellValueEx(x, y, Color.BLACK, "");
            }
        }

        for (Star star : stars) {
            star.draw(this);
        }
    }

    private void createStars() {
        stars = new ArrayList<Star>();
        for (int i = 0; i < 8; i++) {
            stars.add(new Star(getRandomNumber(63), getRandomNumber(63)));
        }
    }

    private void moveSpaceObjects() {
        enemyFleet.move();
        for (Bullet bullet : enemyBullets) {
            bullet.move();
        }
    }

    private void removeDeadBullets() {
        List<Bullet> deletedBulets = new ArrayList<Bullet>();
        for (Bullet bullet : enemyBullets) {
            if (!bullet.isAlive || bullet.y >= HEIGHT - 1) {
                deletedBulets.add(bullet);
            }
        }

        for (Bullet bullet : deletedBulets) {
            enemyBullets.remove(bullet);
        }
    }

    private void check() {
        playerShip.verifyHit(enemyBullets);
        removeDeadBullets();

        if (!playerShip.isAlive) {
            stopGameWithDelay();
        }
    }

    private void stopGame(boolean isWin) {
        isGameStopped = true;
        stopTurnTimer();
        if (isWin) {
            showMessageDialog(Color.PERU, "YOU WIN!", Color.GREEN, 40);
        } else {
            showMessageDialog(Color.TAN, "GAME OVER!", Color.RED, 40);
        }
    }

    private void stopGameWithDelay() {
        animationsCount++;
        if (animationsCount >= 10) {
            stopGame(playerShip.isAlive);
        }
    }
}
