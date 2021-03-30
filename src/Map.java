import java.util.Random;

class Map {

    private static final String LEVEL_1_MAP_DATA =
        "########################" +
        "#  #  #     #   #   #  #" +
        "#  #      #   #   #   ##" +
        "#  #  #     #   #   #  #" +
        "#  ####       #   #    #" +
        "##                  #  #" +
        "# #                    #" +
        "#     ######    ##  #  #" +
        "# #   #    #    ##     #" +
        "##    #    #    ##     #" +
        "#     #    #           #" +
        "#     ##  ##  ####   ###" +
        "# ###         #        #" +
        "# #                  # #" +
        "# #  ## ######  ##     #" +
        "#       #        #     #" +
        "# #  #  #      # # #   #" +
        "# #  #  ##########   # #" +
        "# #     #   #   #      #" +
        "# ##  #   #   #   #    #" +
        "#       #   #   #   #  #" +
        "### ##    #   #   #   ##" +
        "#     #     #   #   #  #" +
        "########################";

    private static final Random RANDOM = new Random();
    private static final String[] POSSIBLE_MAPS = {LEVEL_1_MAP_DATA};
    private static final int[] POSSIBLE_MAPS_HEIGHTS = {24};
    private static final int[] POSSIBLE_MAPS_WIDTHS = {24};
//    private static final int MAP_NUMBER = RANDOM.nextInt(POSSIBLE_MAPS.length);
    private static final int MAP_NUMBER = 0;
    private static final int MAP_HEIGHT = POSSIBLE_MAPS_HEIGHTS[MAP_NUMBER];
    private static final int MAP_WIDTH = POSSIBLE_MAPS_WIDTHS[MAP_NUMBER];
    private static final char[][] LEVEL_MAP = initMap();

    private static char[][] initMap() {
        char[][] levelMap = new char[MAP_HEIGHT][MAP_WIDTH];
        int levelMapDataIndex = 0;
        for (int h = 0; h < MAP_HEIGHT; h++) {
            for (int w = 0; w < MAP_WIDTH; w++) {
                levelMap[h][w] = POSSIBLE_MAPS[MAP_NUMBER].charAt(levelMapDataIndex);
                levelMapDataIndex++;
            }
        }
        return levelMap;
    }

    static {
        int exitPositionY;
        int exitPositionX;
        while (true) {
            do {
                exitPositionY = RANDOM.nextInt(MAP_HEIGHT);
                exitPositionX = RANDOM.nextInt(MAP_WIDTH);
            } while (LEVEL_MAP[exitPositionY][exitPositionX] != '#');
            if (exitPositionY + 1 < MAP_HEIGHT && LEVEL_MAP[exitPositionY + 1][exitPositionX] == ' '
                    || exitPositionY - 1 >= 0 && LEVEL_MAP[exitPositionY - 1][exitPositionX] == ' '
                    || exitPositionX + 1 < MAP_WIDTH && LEVEL_MAP[exitPositionY][exitPositionX + 1] == ' '
                    || exitPositionX - 1 >= 0 && LEVEL_MAP[exitPositionY][exitPositionX - 1] == ' ') {
                LEVEL_MAP[exitPositionY][exitPositionX] = 'E';
                break;
            }
        }
    }

    public static boolean checkUnitPlayerPositionOnExit() {
        final int UNIT_PLAYER_POSITION_Y = (int) Player.getUnitPlayerPositionY();
        final int UNIT_PLAYER_POSITION_X = (int) Player.getUnitPlayerPositionX();
        return LEVEL_MAP[UNIT_PLAYER_POSITION_Y + 1][UNIT_PLAYER_POSITION_X] == 'E'
                || LEVEL_MAP[UNIT_PLAYER_POSITION_Y - 1][UNIT_PLAYER_POSITION_X] == 'E'
                || LEVEL_MAP[UNIT_PLAYER_POSITION_Y][UNIT_PLAYER_POSITION_X + 1] == 'E'
                || LEVEL_MAP[UNIT_PLAYER_POSITION_Y][UNIT_PLAYER_POSITION_X - 1] == 'E';
    }

    public static char[][] getLevelMap() {
        return LEVEL_MAP;
    }

    public static int getMapHeight() {
        return MAP_HEIGHT;
    }

    public static int getMapWidth() {
        return MAP_WIDTH;
    }

}
