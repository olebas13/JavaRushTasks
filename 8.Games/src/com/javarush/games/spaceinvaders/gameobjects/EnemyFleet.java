package com.javarush.games.spaceinvaders.gameobjects;

import com.javarush.engine.cell.Game;
import com.javarush.games.spaceinvaders.Direction;
import com.javarush.games.spaceinvaders.ShapeMatrix;
import com.javarush.games.spaceinvaders.SpaceInvadersGame;

import java.util.ArrayList;
import java.util.List;

public class EnemyFleet {

    private static final int ROWS_COUNT = 3;
    private static final int COLUMNS_COUNT = 10;
    private static final int STEP = ShapeMatrix.ENEMY.length + 1;

    private List<EnemyShip> ships;
    private Direction direction = Direction.RIGHT;

    public EnemyFleet() {
        createShips();
    }

    private void createShips() {
        ships = new ArrayList<EnemyShip>();
        for (int x = 0; x < COLUMNS_COUNT; x++) {
            for (int y = 0; y < ROWS_COUNT; y++) {
                ships.add(new EnemyShip(x * STEP, y * STEP + 12));
            }
        }
    }

    public void draw(Game game) {
        for (EnemyShip ship : ships) {
            ship.draw(game);
        }
    }

    private double getLeftBorder() {
        double min = ships.get(0).x;

        for (EnemyShip ship : ships) {
            if (ship.x < min) {
                min = ship.x;
            }
        }
        return min;
    }

    private double getRightBorder() {
        double max = ships.get(0).x + ships.get(0).width;

        for (EnemyShip ship : ships) {
            if ((ship.x + ship.width) > max) {
                max = ship.x + ship.width;
            }
        }

        return max;
    }

    private double getSpeed() {
        double value2 = 3.0 / ships.size();
        return 2.0 > value2 ? value2 : 2.0;
    }

    public void move() {
        boolean flag = false;

        if (ships.isEmpty()) {
            return;
        }

        if (direction == Direction.LEFT && getLeftBorder() < 0) {
            direction = Direction.RIGHT;
            flag = true;
        }

        if (direction == Direction.RIGHT && getRightBorder() > SpaceInvadersGame.WIDTH) {
            direction = Direction.LEFT;
            flag = true;
        }

        if (flag) {
            for (EnemyShip ship : ships) {
                ship.move(Direction.DOWN, getSpeed());
            }
        } else {
            for (EnemyShip ship : ships) {
                ship.move(direction, getSpeed());
            }
        }
    }

    public Bullet fire(Game game) {
        if (ships.isEmpty()) {
            return null;
        }

        if (game.getRandomNumber(100 / SpaceInvadersGame.COMPLEXITY) > 0) {
            return null;
        } else {
            int firedShip = game.getRandomNumber(ships.size());
            return ships.get(firedShip).fire();
        }
    }
}
