public class ScreenRenderer {

    private static double unitPlayerPositionX;
    private static double unitPlayerPositionY;
    private static double unitPlayerViewAngle = 0;
    private static final double UNIT_PLAYER_VIEW_DISTANCE = 16.0;
    private static double unitPlayerFOVVectorWallDistance;
    private static double unitPlayerFOVVectorCardDistance;
    private static boolean isFOVVectorReachedWall;
    private static boolean isFOVVectorReachedExit;
    private static int cardVectorsSum;
    private static int cardVectorsCount;
    private static int cardScreenPosition;
    private static int cardWidth;
    private static int cardHeight;
    private static final int MAP_WIDTH = 24;
    private static final int MAP_HEIGHT = 24;
    private static final char[][] levelMap = new char[MAP_HEIGHT][MAP_WIDTH];
    private static final int SCREEN_WIDTH = 120;
    private static final int SCREEN_HEIGHT = 40;
    private static final StringBuilder screen = new StringBuilder();
    private static final String SCREEN_TEMPLATE = initScreenTemplate();
    private static int screenWallPoint;

    private static String initScreenTemplate() {
        StringBuilder template = new StringBuilder();
        for (int h = 0; h < SCREEN_HEIGHT / 2; h++) {
            for (int w = 0; w < SCREEN_WIDTH; w++) {
                if (h < SCREEN_HEIGHT * 0.125) {
                    template.append("=");
                } else  if (h < SCREEN_HEIGHT * 0.25) {
                    template.append("-");
                } else if (h < SCREEN_HEIGHT * 0.375) {
                    template.append(".");
                } else {
                    template.append(" ");
                }
            }
            template.append("\n");
        }
        return template.toString();
    }

    public static void initMap() {
        String levelMapData =
                "########################" +
                "#    P    #        ### #" +
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
                "#K          # #  # # # #" +
                "############### ## #   #" +
                "#      #   #### ## #####" +
                "# #### # # ##          #" +
                "# #    # # ##     K    #" +
                "# #### # ####          #" +
                "# #    #      ###### # #" +
                "# #### ########      # #" +
                "#    #        #### ### #" +
                "####E########      #   #" +
                "########################";

        // P - PLAYER
        // E - EXIT
        // K - KEY
        // # - WALL

        int levelMapDataIndex = 0;
        for (int h = 0; h < MAP_HEIGHT; h++) {
            for (int w = 0; w < MAP_WIDTH; w++) {
                levelMap[h][w] = levelMapData.charAt(levelMapDataIndex);
                if (levelMapData.charAt(levelMapDataIndex) == 'P') {
                    unitPlayerPositionX = levelMapDataIndex % MAP_WIDTH;
                    unitPlayerPositionY = (double) levelMapDataIndex / MAP_WIDTH;
                }
                levelMapDataIndex++;
            }
        }
    }

    private static void calculateVectorDistance(int w) {
        double unitPlayerFOV = Math.PI / 4;
        double unitPlayerFOVVector = unitPlayerViewAngle - unitPlayerFOV / 2 + unitPlayerFOV / SCREEN_WIDTH * w;
        double unitPlayerFOVX = Math.sin(unitPlayerFOVVector);
        double unitPlayerFOVY = Math.cos(unitPlayerFOVVector);
        boolean isFOVVectorReachedCard = false;
        isFOVVectorReachedWall = false;
        isFOVVectorReachedExit = false;
        unitPlayerFOVVectorWallDistance = 0;
        while (!isFOVVectorReachedWall && !isFOVVectorReachedExit && unitPlayerFOVVectorWallDistance < UNIT_PLAYER_VIEW_DISTANCE) {
            unitPlayerFOVVectorWallDistance += 0.1;
            if (levelMap[(int) (unitPlayerPositionY + unitPlayerFOVY * unitPlayerFOVVectorWallDistance)][(int) (unitPlayerPositionX + unitPlayerFOVX * unitPlayerFOVVectorWallDistance)] == '#') {
                isFOVVectorReachedWall = true;
            } else if (levelMap[(int) (unitPlayerPositionY + unitPlayerFOVY * unitPlayerFOVVectorWallDistance)][(int) (unitPlayerPositionX + unitPlayerFOVX * unitPlayerFOVVectorWallDistance)] == 'E') {
                isFOVVectorReachedExit = true;
            }
            if (!isFOVVectorReachedCard) {
                if (levelMap[(int) (unitPlayerPositionY + unitPlayerFOVY * unitPlayerFOVVectorWallDistance)][(int) (unitPlayerPositionX + unitPlayerFOVX * unitPlayerFOVVectorWallDistance)] == 'K') {
                    isFOVVectorReachedCard = true;
                    cardVectorsSum += w;
                    cardVectorsCount++;
                    unitPlayerFOVVectorCardDistance = unitPlayerFOVVectorWallDistance;
                }
            }
        }
    }

    private static void calculateScreenData(int w) {
        for (int h = screenWallPoint; h < SCREEN_HEIGHT / 2; h++) {
            if (screenWallPoint < SCREEN_HEIGHT * 0.125) {
                screen.setCharAt(h * 121 + w, isFOVVectorReachedWall ? '‖' : 'X');  // TEMPORARY SOLUTION
            } else if (screenWallPoint < SCREEN_HEIGHT * 0.25) {
                screen.setCharAt(h * 121 + w, isFOVVectorReachedWall ? '|' : 'x');  // TEMPORARY SOLUTION
            } else if (screenWallPoint < SCREEN_HEIGHT * 0.375) {
                screen.setCharAt(h * 121 + w, isFOVVectorReachedWall ? ':' : '^');  // TEMPORARY SOLUTION
            } else {
                screen.setCharAt(h * 121 + w, '·');
            }
        }
    }

    private static int calculateCardParameters(boolean width) {
        return width ? (int) Math.floor((16 - unitPlayerFOVVectorCardDistance) * 1.25) : (int) Math.floor((16 - unitPlayerFOVVectorCardDistance) / 4);
    }

    private static void drawCard() {
        for (int i = 0; i < cardHeight; i++) {
            int index = (19 - i) * 121 + cardScreenPosition;
            for (int j = index; j < index + cardWidth; j++) {
                if (j == (20 - i) * 121 - 1) {
                    break;
                } else {
                    screen.setCharAt(j, unitPlayerFOVVectorCardDistance > 8 ? ':' : '#');  // TEMPORARY SOLUTION
                }
            }
        }
    }

    private static void completeScreenData() {
        for (int h = SCREEN_HEIGHT / 2 - 1; h >= 0; h--) {
            screen.append(screen.substring(h * 121, (h + 1) * 121));
        }
    }

    public static void runScreenRenderingCycle() {
        screen.append(SCREEN_TEMPLATE);
        cardVectorsSum = 0;
        cardVectorsCount = 0;
        for (int w = 0; w < SCREEN_WIDTH; w++) {
            calculateVectorDistance(w);
            if (isFOVVectorReachedWall || isFOVVectorReachedExit) {
                screenWallPoint = (int) Math.floor(SCREEN_HEIGHT / 2.0 - SCREEN_HEIGHT / 2.0 / (unitPlayerFOVVectorWallDistance * 1.25));
                if (screenWallPoint < 0) { screenWallPoint = 0; }
                calculateScreenData(w);
            }
        }
        if (cardVectorsCount > 0) {
            cardWidth = calculateCardParameters(true);
            cardHeight = calculateCardParameters(false);
            cardScreenPosition = cardVectorsSum / cardVectorsCount - cardWidth / 2;
            if (cardScreenPosition < 0) {
                cardScreenPosition = 0;
            }
            drawCard();
        }
        completeScreenData();
        GUI.renderScreen(screen.toString());
        screen.delete(0, screen.length());
    }

    public static void changeUnitPlayerViewAngle(double value) { unitPlayerViewAngle += value; }

    public static void changeUnitPlayerPosition(double value, boolean isSidewalk) {
        double unitPlayerViewAngleData = unitPlayerViewAngle;
        if (isSidewalk) {
            unitPlayerViewAngleData += Math.PI / 2;
        }
        double unitPlayerPositionXStep = Math.sin(unitPlayerViewAngleData) * value;
        double unitPlayerPositionYStep = Math.cos(unitPlayerViewAngleData) * value;
        if (levelMap[(int) unitPlayerPositionY][(int) (unitPlayerPositionX + unitPlayerPositionXStep)] != '#') {
            unitPlayerPositionX += unitPlayerPositionXStep;
        }
        if (levelMap[(int) (unitPlayerPositionY + unitPlayerPositionYStep)][(int) unitPlayerPositionX] != '#') {
            unitPlayerPositionY += unitPlayerPositionYStep;
        }
    }

    public static boolean checkUnitPlayerPositionOnExit() {
        return levelMap[(int) unitPlayerPositionY][(int) (unitPlayerPositionX)] == 'E';
    }

    public static boolean checkUnitPlayerPositionOnCard() {
        return levelMap[(int) unitPlayerPositionY][(int) (unitPlayerPositionX)] == 'K';
    }

    public static void exitingMessage() {  // REWORK MESSAGE
        GUI.renderScreen("YOU FOUND THE EXIT\nYOU SURVIVED");
    }

}
