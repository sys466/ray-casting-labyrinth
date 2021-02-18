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

    public Hunter() {
        generatePosition();
        this.direction = random.nextInt(4);
        this.speed = 0.04;
    }

    private void generatePosition() {
        do {
            this.positionY = random.nextInt(MAP_HEIGHT - 1) + 1;
            this.positionX = random.nextInt(MAP_WIDTH - 1) + 1;
        } while (levelMap[(int) this.positionY][(int) this.positionX] != ' ');
    }

    private void scan() {
        boolean spotted = false;
    }

    public void move() {
    }

}
