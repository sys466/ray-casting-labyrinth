import java.util.ArrayList;
import java.util.Random;

class Hunter {

    private static final ArrayList<Hunter> hunters = new ArrayList<>();
    private static final Random random = new Random();
    private static final char[][] levelMap = Map.getLevelMap();
    private static final int MAP_HEIGHT = levelMap.length;
    private static final int MAP_WIDTH = levelMap[0].length;
    private static final double HUNTER_MAP_HALF_WIDTH = 0.1;
    private static double speed = 0.02;

    private double positionY;
    private double positionX;
    private int direction;  // 0 - UP, 1 - DOWN, 2 - LEFT, 3 - RIGHT

    static {
        hunters.add(new Hunter());
    }

    private Hunter() {
        generatePosition();
        this.direction = random.nextInt(4);
    }

    public static void addHunter() {
        hunters.add(new Hunter());
    }

    public static boolean moveHunters() {
        for (Hunter hunter: hunters) {
            hunter.move();
            if (hunter.checkUnitPlayerPosition()) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkFOVVectorReachedHunter(double vectorPositionY, double vectorPositionX) {
        for (Hunter hunter: hunters) {
            if (vectorPositionY > hunter.positionY - HUNTER_MAP_HALF_WIDTH && vectorPositionY < hunter.positionY + HUNTER_MAP_HALF_WIDTH
            && vectorPositionX > hunter.positionX - HUNTER_MAP_HALF_WIDTH && vectorPositionX < hunter.positionX + HUNTER_MAP_HALF_WIDTH) {
                return true;
            }
        }
        return false;
    }

    private boolean checkUnitPlayerPosition() {
        return (int) Math.floor(this.positionY) == (int) Math.floor(Player.getUnitPlayerPositionY())
                && (int) Math.floor(this.positionX) == (int) Math.floor(Player.getUnitPlayerPositionX());
    }

    private void generatePosition() {
        do {
            this.positionY = random.nextInt(MAP_HEIGHT - 1) + 1;
            this.positionX = random.nextInt(MAP_WIDTH - 1) + 1;
        } while (levelMap[(int) this.positionY][(int) this.positionX] != ' ');
    }

    private void scan() {
        for (int i = 0; i < 4; i++) {
            int positionY = (int) this.positionY;
            int positionX = (int) this.positionX;
            do {
                switch (i) {
                    case 0 -> positionY--;
                    case 1 -> positionY++;
                    case 2 -> positionX--;
                    default -> positionX++;
                }
                if (positionY == (int) Player.getUnitPlayerPositionY()
                && positionX == (int) Player.getUnitPlayerPositionX()) {
                    this.direction = i;
                    return;
                }
            } while (levelMap[positionY][positionX] != '#' && levelMap[positionY][positionX] != 'E');
        }
    }

    private void changeDirection() {
        int newDirection;
        do {
            newDirection = random.nextInt(4);
        } while (newDirection == this.direction);
        this.direction = newDirection;
    }

    public static void increaseSpeed() {
        speed += 0.01;
    }

    private void move() {
        this.scan();
        double stepY = 0;
        double stepX = 0;
        switch (this.direction) {
            case 0 -> stepY = -speed;
            case 1 -> stepY = speed;
            case 2 -> stepX = -speed;
            default -> stepX = speed;
        }
        if (levelMap[(int) (this.positionY + stepY)][(int) (this.positionX + stepX)] == ' ') {
            this.positionY += stepY;
            this.positionX += stepX;
        } else {
            changeDirection();
        }
    }

}
