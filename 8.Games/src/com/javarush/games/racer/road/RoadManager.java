package com.javarush.games.racer.road;

import com.javarush.engine.cell.Game;
import com.javarush.games.racer.PlayerCar;
import com.javarush.games.racer.RacerGame;

import java.util.ArrayList;
import java.util.List;

public class RoadManager {

    public static final int LEFT_BORDER = RacerGame.ROADSIDE_WIDTH;
    public static final int RIGHT_BORDER = RacerGame.WIDTH - LEFT_BORDER;
    private static final int FIRST_LANE_POSITION = 16;
    private static final int FOURTH_LANE_POSITION = 44;
    private static final int PLAYER_CAR_DISTANCE = 17;

    private List<RoadObject> items = new ArrayList<>();
    private int passedCarsCount = 0;

    private RoadObject createRoadObject(RoadObjectType type, int x, int y) {
        if (type == RoadObjectType.THORN) {
            return new Thorn(x, y);
        } else if (type == RoadObjectType.DRUNK_CAR) {
            return new MovingCar(x, y);
        } else {
            return new Car(type, x, y);
        }
    }

    private void addRoadObject(RoadObjectType type, Game game) {
        int x = game.getRandomNumber(FIRST_LANE_POSITION, FOURTH_LANE_POSITION);
        int y = -1 * RoadObject.getHeight(type);
        RoadObject roadObject = createRoadObject(type, x, y);
        if (items != null && isRoadSpaceFree(roadObject)) {
            items.add(roadObject);
        }
    }

    public void draw(Game game) {
        for (RoadObject item : items) {
            item.draw(game);
        }
    }

    public void move(int boost) {
        for (RoadObject item : items) {
            item.move(boost + item.speed, items);
        }
        deletePassedItems();
    }

    private boolean isThornExists() {
        boolean isThornExists = false;
        for (RoadObject item : items) {
            if (item.type.equals(RoadObjectType.THORN)) {
                isThornExists = true;
            }
        }
        return isThornExists;
    }

    private void generateThorn(Game game) {
        int number = game.getRandomNumber(100);
        if (number < 10 && !isThornExists()) {
            addRoadObject(RoadObjectType.THORN, game);
        }
    }

    public void generateNewRoadObjects(Game game) {
        generateThorn(game);
        generateRegularCar(game);
        generateMovingCar(game);
    }

    private void deletePassedItems() {
        List<RoadObject> removeItems = new ArrayList<>();
        for (RoadObject item : items) {
            if (item.y >= RacerGame.HEIGHT) {
                removeItems.add(item);
            }
        }
        for (RoadObject roadObject : removeItems) {
            items.remove(roadObject);
            if (!roadObject.type.equals(RoadObjectType.THORN)) {
                passedCarsCount++;
            }
        }
    }

    public boolean checkCrush(PlayerCar playerCar) {
        boolean isCrushed = false;
        for (RoadObject roadObject : items) {
            if (roadObject.isCollision(playerCar)) {
                isCrushed = true;
            }
        }
        return isCrushed;
    }

    private void generateRegularCar(Game game) {
        int carTypeNumber = game.getRandomNumber(4);

        if (game.getRandomNumber(100) < 30) {
            addRoadObject(RoadObjectType.values()[carTypeNumber], game);
        }
    }

    private boolean isRoadSpaceFree(RoadObject object) {
        boolean isFree = true;
        for (RoadObject roadObject : items) {
            if (roadObject.isCollisionWithDistance(object, PLAYER_CAR_DISTANCE)) {
                isFree = false;
            }
        }
        return isFree;
    }

    private boolean isMovingCarExists() {
        boolean isMovingCarExists = false;
        for (RoadObject roadObject : items) {
            if (roadObject.type.equals(RoadObjectType.DRUNK_CAR)) {
                isMovingCarExists = true;
            }
        }
        return isMovingCarExists;
    }

    private void generateMovingCar(Game game) {
        if (game.getRandomNumber(100) < 10 && !isMovingCarExists()) {
            addRoadObject(RoadObjectType.DRUNK_CAR, game);
        }
    }

    public int getPassedCarsCount() {
        return passedCarsCount;
    }
}
