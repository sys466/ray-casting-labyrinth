import java.util.Random;

public class Hunter {

    private static final Random random = new Random();
    private static final char[][] levelMap = ScreenRenderer.getLevelMap();
    private static final int MAP_HEIGHT = levelMap.length;
    private static final int MAP_WIDTH = levelMap[0].length;

    private double positionY;
    private double positionX;
    private int direction;  // 0 - UP, 1 - DOWN, 2 - LEFT, 3 - RIGHT
    private double speed;
    private int markerPositionY;
    private int markerPositionX;

    public Hunter() {
        generatePosition();
        this.direction = random.nextInt(4);
        this.speed = 0.04;
    }

    private void generatePosition() {
        do {
            this.markerPositionY = random.nextInt(MAP_HEIGHT - 1) + 1;
            this.markerPositionX = random.nextInt(MAP_WIDTH - 1) + 1;
        } while (levelMap[markerPositionY][markerPositionX] != ' ');
        levelMap[markerPositionY][markerPositionX] = 'H';
        this.positionY = markerPositionY;
        this.positionX = markerPositionX;
    }

    private boolean scan() {
        for (int i = 0; i < 4; i++) {
            int positionY = (int) Math.floor(this.positionY);
            int positionX = (int) Math.floor(this.positionX);
            do {
                switch (i) {
                    case 0 -> positionY--;
                    case 1 -> positionY++;
                    case 2 -> positionX--;
                    default -> positionX++;
                }
                if (levelMap[positionY][positionX] != 'P') {
                    this.direction = i;
                    return true;
                }
            } while (levelMap[positionY][positionX] != '#' && levelMap[positionY][positionX] != 'E');
        }
        return false;
    }

    private void changeDirection() {
        int newDirection;
        do {
            newDirection = random.nextInt(4);
        } while (newDirection == this.direction);
        this.direction = newDirection;
    }

    private void checkMarker() {
        if ((int) Math.floor(this.positionY) != this.markerPositionY
                || (int) Math.floor(this.positionX) != this.markerPositionX) {
            levelMap[this.markerPositionY][this.markerPositionX] = ' ';
            this.markerPositionY = (int) Math.floor(this.positionY);
            this.markerPositionX = (int) Math.floor(this.positionX);
            levelMap[this.markerPositionY][this.markerPositionX] = 'H';
        }
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getMarkerPositionY() {
        return this.markerPositionY;
    }

    public int getMarkerPositionX() {
        return this.markerPositionX;
    }

    public void move() {
        int stepY = 0;
        int stepX = 0;
        switch (this.direction) {
            case 0 -> stepY -= this.speed;
            case 1 -> stepY += this.speed;
            case 2 -> stepX -= this.speed;
            default -> stepX += this.speed;
        }
        if (this.scan() && levelMap[(int) Math.floor(this.positionY + stepY)][(int) Math.floor(this.positionX + stepX)] != 'C'
                || levelMap[(int) Math.floor(this.positionY + stepY)][(int) Math.floor(this.positionX + stepX)] == ' ') {
            this.positionY += stepY;
            this.positionX += stepX;
            this.checkMarker();
        } else {
            changeDirection();
        }
    }

}
