import java.util.Random;

class Player {

    private static final Random RANDOM = new Random();
    private static double unitPlayerPositionY;
    private static double unitPlayerPositionX;
    private static double unitPlayerViewAngle = 0;
    private static final char[][] LEVEL_MAP = Map.getLevelMap();
    private static final int MAP_HEIGHT = Map.getMapHeight();
    private static final int MAP_WIDTH = Map.getMapWidth();

    static {
        do {
            unitPlayerPositionY = RANDOM.nextInt(MAP_HEIGHT - 1) + 1;
            unitPlayerPositionX = RANDOM.nextInt(MAP_WIDTH - 1) + 1;
        } while (LEVEL_MAP[(int) unitPlayerPositionY][(int) unitPlayerPositionX] != ' ');
    }

    public static void changeUnitPlayerViewAngle(double value) { unitPlayerViewAngle += value; }

    public static void changeUnitPlayerPosition(double value, boolean isSidewalk) {
        double unitPlayerViewAngleData = unitPlayerViewAngle;
        if (isSidewalk) {
            unitPlayerViewAngleData += Math.PI / 2;
        }
        final double NEW_UNIT_PLAYER_POSITION_Y = unitPlayerPositionY + -Math.sin(unitPlayerViewAngleData) * value;
        final double NEW_UNIT_PLAYER_POSITION_X = unitPlayerPositionX + Math.cos(unitPlayerViewAngleData) * value;
        if (LEVEL_MAP[(int) NEW_UNIT_PLAYER_POSITION_Y][(int) unitPlayerPositionX] != '#'
        && LEVEL_MAP[(int) NEW_UNIT_PLAYER_POSITION_Y][(int) unitPlayerPositionX] != 'E') {
            unitPlayerPositionY = NEW_UNIT_PLAYER_POSITION_Y;
        }
        if (LEVEL_MAP[(int) unitPlayerPositionY][(int) NEW_UNIT_PLAYER_POSITION_X] != '#'
        && LEVEL_MAP[(int) unitPlayerPositionY][(int) NEW_UNIT_PLAYER_POSITION_X] != 'E') {
            unitPlayerPositionX = NEW_UNIT_PLAYER_POSITION_X;
        }
    }

    public static double getUnitPlayerPositionY() {
        return unitPlayerPositionY;
    }

    public static double getUnitPlayerPositionX() {
        return unitPlayerPositionX;
    }

    public static double getUnitPlayerViewAngle() {
        return unitPlayerViewAngle;
    }

}
