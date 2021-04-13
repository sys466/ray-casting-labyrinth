import java.util.ArrayList;
import java.util.Random;

class Hunter {

    private static final ArrayList<Hunter> hunters = new ArrayList<>();
    private static final Random random = new Random();
    private static final char[][] levelMap = Map.getLevelMap();
    private static final int MAP_HEIGHT = Map.getMapHeight();
    private static final int MAP_WIDTH = Map.getMapWidth();
    private static final double HUNTER_MAP_HALF_WIDTH = 0.1;
    private static double speed = 0.01;

    private double positionY;
    private double positionX;
    private int positionYOld;
    private int positionXOld;
    private int direction;  // 0 - UP, 1 - DOWN, 2 - LEFT, 3 - RIGHT
    private double stepY;
    private double stepX;
    private boolean found;

    static {
        hunters.add(new Hunter());
    }

    private Hunter() {
        this.generatePosition();
        this.direction = random.nextInt(4);
        this.calculateSteps();
        this.found = false;
    }

    public static void addHunter() {
        hunters.add(new Hunter());
    }

    private void generatePosition() {
        do {
            this.positionY = random.nextInt(MAP_HEIGHT - 1) + 1;
            this.positionX = random.nextInt(MAP_WIDTH - 1) + 1;
        } while (levelMap[(int) this.positionY][(int) this.positionX] != ' ');
        this.positionY += 0.5;
        this.positionX += 0.5;
        this.positionYOld = (int) this.positionY;
        this.positionXOld = (int) this.positionX;
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

    public static boolean moveHunters() {
        for (Hunter hunter: hunters) {
            hunter.move();
            if (hunter.checkUnitPlayerPosition()) {
                return true;
            }
        }
        return false;
    }

    private boolean checkUnitPlayerPosition() {
        return (int) Math.floor(this.positionY) == (int) Math.floor(Player.getUnitPlayerPositionY())
                && (int) Math.floor(this.positionX) == (int) Math.floor(Player.getUnitPlayerPositionX());
    }

    private void move() {
        boolean spotted = this.hunt();
        if (!this.found && ((int) this.positionY != this.positionYOld || (int) this.positionX != this.positionXOld)) {
            this.navigate();
        }
        if (!spotted && (levelMap[(int) (this.positionY + this.stepY)][(int) (this.positionX + this.stepX)] == '#'
        || levelMap[(int) (this.positionY + this.stepY)][(int) (this.positionX + this.stepX)] == 'E')) {
            this.found = false;
            this.navigate();
        }
        this.positionY += this.stepY;
        this.positionX += this.stepX;
    }

    private boolean hunt() {
        double playerPosY = Player.getUnitPlayerPositionY();
        double playerPosX = Player.getUnitPlayerPositionX();
        double difY = playerPosY - this.positionY;
        double difX = playerPosX - this.positionX;
        double length = Math.sqrt(Math.pow(difY, 2) + Math.pow(difX, 2));
        double vectorStepY = difY / length;
        double vectorStepX = difX / length;
        double step = 0;
        while ((int) (this.positionY + vectorStepY * step) != (int) playerPosY
        || (int) (this.positionX + vectorStepX * step) != (int) playerPosX) {
            if (levelMap[(int) (this.positionY + vectorStepY * step)][(int) (this.positionX + vectorStepX * step)] != ' ') {
                return false;
            }
            step += 0.05;
        }
        this.found = true;
        this.stepY = vectorStepY * speed;
        this.stepX = vectorStepX * speed;
        return true;
    }

    private void navigate() {  // 0 - UP, 1 - DOWN, 2 - LEFT, 3 - RIGHT
        ArrayList<Integer> paths = new ArrayList<>();
        if (this.direction != 1 && levelMap[(int) this.positionY - 1][(int) this.positionX] == ' ') {
            paths.add(0);
        }
        if (this.direction != 0 && levelMap[(int) this.positionY + 1][(int) this.positionX] == ' ') {
            paths.add(1);
        }
        if (this.direction != 3 && levelMap[(int) this.positionY][(int) this.positionX - 1] == ' ') {
            paths.add(2);
        }
        if (this.direction != 2 && levelMap[(int) this.positionY][(int) this.positionX + 1] == ' ') {
            paths.add(3);
        }
        if (paths.isEmpty()) {
            if (this.direction < 2) {
                this.direction = this.direction == 0 ? 1 : 0;
            } else {
                this.direction = this.direction == 2 ? 3 : 2;
            }
        } else {
            this.direction = paths.get(random.nextInt(paths.size()));
        }
        this.calculateSteps();
        this.positionYOld = (int) this.positionY;
        this.positionXOld = (int) this.positionX;
    }

    private void calculateSteps() {  // 0 - UP, 1 - DOWN, 2 - LEFT, 3 - RIGHT
        switch (this.direction) {
            case 0 -> {
                this.stepY = -speed;
                this.stepX = 0;
            }
            case 1 -> {
                this.stepY = speed;
                this.stepX = 0;
            }
            case 2 -> {
                this.stepY = 0;
                this.stepX = -speed;
            }
            default -> {
                this.stepY = 0;
                this.stepX = speed;
            }
        }
    }

    public static void increaseSpeed() {
        speed += 0.01;
    }

}
