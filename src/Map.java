import java.util.Random;

public class Map {

    private static final String level1MapData =
        "########################" +
        "#         #        ### #" +
        "# ####### # #### ##### #" +
        "# #     #   ## # #  #  #" +
        "# # ### # # ##   #  # ##" +
        "# # # #   # ## # #  # ##" +
        "# ### ##### ####       #" +
        "#           #### ##### #" +
        "# # ### ##           # #" +
        "# # ### ############ # #" +
        "# # #              # # #" +
        "# # # ##### ###### # # #" +
        "# ######### #    # # # #" +
        "#           # #  # # # #" +
        "###### ###### # ## #   #" +
        "#      #   #### ## #####" +
        "# #### # # #           #" +
        "# #    # # #    ###    #" +
        "# #### # ###           #" +
        "# #    #      ###### # #" +
        "# #### ########      # #" +
        "#    #        #### ### #" +
        "#    ########      #   #" +
        "########################";

    private static final String level2MapData =
        "########################" +
        "# #         ####    #  #" +
        "# # #    ##  ##  #  ## #" +
        "# #   #  ##  ####      #" +
        "#    #  ###        #####" +
        "#####   ###  ###########" +
        "#        #             #" +
        "# ####### ### ##########" +
        "#         # #          #" +
        "#         # #   #####  #" +
        "# ########   #######   #" +
        "#                      #" +
        "###    #####  ###  ##  #" +
        "#  ##         #        #" +
        "#    ##   #####  ###   #" +
        "#                      #" +
        "#   # # #              #" +
        "#                      #" +
        "#    # #               #" +
        "###                    #" +
        "#          ######  #   #" +
        "#  #########      # #  #" +
        "#          #     #     #" +
        "########################";

    private static final String level3MapData =
        "########################################" +
        "#          #  ##  #     #    #   #   # #" +
        "# ### ###  #      #  #  #  #   #   #   #" +
        "# #     #  ###  ###  ######  #   #   # #" +
        "# #######                              #" +
        "#  #     #   #   #     ######  # ####  #" +
        "#   #     #  #####          # #  #  #  #" +
        "#    ##  ###           ######  #       #" +
        "#              ####    #      #  #  #  #" +
        "# ####  #####  #  #    ######  # ####  #" +
        "# #   # #         #           #        #" +
        "# #   # ###########   #  #  #  #  #### #" +
        "#                    #  #  #      #    #" +
        "########################################";

    private static final Random random = new Random();
    private static final String[] MAPS = {level1MapData, level2MapData, level3MapData};
    private static final int[] MAPS_HEIGHTS = {24, 24, 14};
    private static final int[] MAPS_WIDTHS = {24, 24, 40};
    private static final int MAP_NUMBER = random.nextInt(MAPS.length);
    private static final int MAP_HEIGHT = MAPS_HEIGHTS[MAP_NUMBER];
    private static final int MAP_WIDTH = MAPS_WIDTHS[MAP_NUMBER];
    private static final int CARD_NUMBER = 3;
    private static int unitPlayerPositionY;
    private static int unitPlayerPositionX;

    public static char[][] initMap() {
        char[][] levelMap = new char[MAPS_HEIGHTS[MAP_NUMBER]][MAPS_WIDTHS[MAP_NUMBER]];
        int levelMapDataIndex = 0;
        for (int h = 0; h < MAPS_HEIGHTS[MAP_NUMBER]; h++) {
            for (int w = 0; w < MAPS_WIDTHS[MAP_NUMBER]; w++) {
                levelMap[h][w] = MAPS[MAP_NUMBER].charAt(levelMapDataIndex);
                levelMapDataIndex++;
            }
        }
        generatePlayer(levelMap);
        generateCards(levelMap);
        generateExit(levelMap);
        return levelMap;
    }

    private static void generatePlayer(char[][] levelMap) {
        do {
            unitPlayerPositionY = random.nextInt(MAP_HEIGHT - 1) + 1;
            unitPlayerPositionX = random.nextInt(MAP_WIDTH - 1) + 1;
        } while (levelMap[unitPlayerPositionY][unitPlayerPositionX] != ' ');
    }

    public static int getUnitPlayerPositionY() {
        return unitPlayerPositionY;
    }

    public static int getUnitPlayerPositionX() {
        return unitPlayerPositionX;
    }

    private static void generateCards(char[][] levelMap) {
        int cardNumber = 0;
        int cardPositionY;
        int cardPositionX;
        while (cardNumber < CARD_NUMBER) {
            do {
                cardPositionY = random.nextInt(MAP_HEIGHT - 1) + 1;
                cardPositionX = random.nextInt(MAP_WIDTH - 1) + 1;
            } while (levelMap[cardPositionY][cardPositionX] != ' ');
            levelMap[cardPositionY][cardPositionX] = 'C';
            cardNumber++;
        }
    }

    private static void generateExit(char[][] levelMap) {
        int exitPositionY;
        int exitPositionX;
        while (true) {
            do {
                exitPositionY = random.nextInt(MAP_HEIGHT);  // NEED TO FIX A BUG (INDEX OUT OF BOUNDS)
                exitPositionX = random.nextInt(MAP_WIDTH);  // NEED TO FIX A BUG (INDEX OUT OF BOUNDS)
            } while (levelMap[exitPositionY][exitPositionX] != '#');
            if (exitPositionY + 1 < MAP_HEIGHT && levelMap[exitPositionY + 1][exitPositionX] == ' '
                    || exitPositionY - 1 >= 0 && levelMap[exitPositionY - 1][exitPositionX] == ' '
                    || exitPositionX + 1 < MAP_WIDTH && levelMap[exitPositionY][exitPositionX + 1] == ' '
                    || exitPositionX - 1 >= 0 && levelMap[exitPositionY][exitPositionX - 1] == ' ') {
                levelMap[exitPositionY][exitPositionX] = 'E';
                break;
            }
        }
    }

}
