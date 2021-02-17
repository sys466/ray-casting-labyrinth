public class ScreenRenderer {

    private static final char[][] levelMap = Map.initMap();
    private static double unitPlayerPositionY = Map.getUnitPlayerPositionY();
    private static double unitPlayerPositionX = Map.getUnitPlayerPositionX();
    private static int unitPlayerPositionYOld = (int) Math.floor(unitPlayerPositionY);
    private static int unitPlayerPositionXOld = (int) Math.floor(unitPlayerPositionX);
    private static double unitPlayerViewAngle = 0;
    private static final double UNIT_PLAYER_VIEW_DISTANCE = 16.0;
    private static double unitPlayerFOVVectorWallDistance;
    private static double unitPlayerFOVVectorCardDistance;
    private static boolean isFOVVectorReachedWall;
    private static boolean isFOVVectorReachedExit;
    private static final int CARD_NUMBER = 3;
    private static int currentCardNumber = 0;
    private static int cardVectorsSum;
    private static int cardVectorsCount;
    private static int cardScreenPosition;
    private static int cardWidth;
    private static int cardHeight;
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
            int positionYChange = (int) (unitPlayerPositionY + unitPlayerFOVY * unitPlayerFOVVectorWallDistance);
            int positionXChange = (int) (unitPlayerPositionX + unitPlayerFOVX * unitPlayerFOVVectorWallDistance);
            if (levelMap[positionYChange][positionXChange] == '#') {
                isFOVVectorReachedWall = true;
            } else if (levelMap[positionYChange][positionXChange] == 'E') {
                isFOVVectorReachedExit = true;
            } else if (!isFOVVectorReachedCard) {
                if (levelMap[positionYChange][positionXChange] == 'C') {
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
                screen.setCharAt(h * 121 + w, isFOVVectorReachedWall ? '‖' : 'X');
            } else if (screenWallPoint < SCREEN_HEIGHT * 0.25) {
                screen.setCharAt(h * 121 + w, isFOVVectorReachedWall ? '|' : 'x');
            } else if (screenWallPoint < SCREEN_HEIGHT * 0.375) {
                screen.setCharAt(h * 121 + w, isFOVVectorReachedWall ? ':' : '+');
            } else {
                screen.setCharAt(h * 121 + w, '·');
            }
        }
    }

    private static int calculateCardParameters(boolean width) {
        if (width) {
            int w = (int) Math.floor(30 - unitPlayerFOVVectorCardDistance * 3);
            if (w <= 0) {
                w = 1;
            }
            return w;
        } else {
            int h = (int) Math.floor(4 - unitPlayerFOVVectorCardDistance * 0.4);
            if (h <= 0) {
                h = 1;
            }
            return h;
        }
    }

    private static void drawCard() {
        for (int i = 0; i < cardHeight; i++) {
            int index = (19 - i) * 121 + cardScreenPosition;
            for (int j = index; j < index + cardWidth; j++) {
                if (j == (20 - i) * 121 - 1) {
                    break;
                } else {
                    screen.setCharAt(j, unitPlayerFOVVectorCardDistance > 4 ? ':' : '#');  // TEMPORARY SOLUTION
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
        double unitPlayerPositionYStep = Math.cos(unitPlayerViewAngleData) * value;
        double unitPlayerPositionXStep = Math.sin(unitPlayerViewAngleData) * value;
        if (levelMap[(int) (unitPlayerPositionY + unitPlayerPositionYStep)][(int) unitPlayerPositionX] != '#'
            && levelMap[(int) (unitPlayerPositionY + unitPlayerPositionYStep)][(int) unitPlayerPositionX] != 'E') {
            unitPlayerPositionY += unitPlayerPositionYStep;
        }
        if (levelMap[(int) unitPlayerPositionY][(int) (unitPlayerPositionX + unitPlayerPositionXStep)] != '#'
            && levelMap[(int) unitPlayerPositionY][(int) (unitPlayerPositionX + unitPlayerPositionXStep)] != 'E') {
            unitPlayerPositionX += unitPlayerPositionXStep;
        }
    }

    public static void checkUnitPlayerPositionChange() {
        if ((int) Math.floor(unitPlayerPositionY) != unitPlayerPositionYOld
        || (int) Math.floor(unitPlayerPositionX) != unitPlayerPositionXOld
        || levelMap[unitPlayerPositionYOld][unitPlayerPositionXOld] == ' ') {
            updateUnitPlayerPosition();
        }
    }

    private static void updateUnitPlayerPosition() {
        levelMap[unitPlayerPositionYOld][unitPlayerPositionXOld] = ' ';
        unitPlayerPositionYOld = (int) Math.floor(unitPlayerPositionY);
        unitPlayerPositionXOld = (int) Math.floor(unitPlayerPositionX);
        if (levelMap[unitPlayerPositionYOld][unitPlayerPositionXOld] == ' ') {
            levelMap[unitPlayerPositionYOld][unitPlayerPositionXOld] = 'P';
        }
    }

    public static boolean checkUnitPlayerPositionNextToExit() {
        return levelMap[(int) unitPlayerPositionY + 1][(int) unitPlayerPositionX] == 'E'
                || levelMap[(int) unitPlayerPositionY - 1][(int) unitPlayerPositionX] == 'E'
                || levelMap[(int) unitPlayerPositionY][(int) unitPlayerPositionX + 1] == 'E'
                || levelMap[(int) unitPlayerPositionY][(int) unitPlayerPositionX - 1] == 'E';
    }

    public static boolean checkUnitPlayerPositionOnCard() {
        return levelMap[(int) unitPlayerPositionY][(int) (unitPlayerPositionX)] == 'C';
    }

    public static void pickUpCard() {
        levelMap[(int) unitPlayerPositionY][(int) (unitPlayerPositionX)] = ' ';
        currentCardNumber++;
    }

    public static boolean checkIfAllCardsCollected() {
        return currentCardNumber == CARD_NUMBER;
    }

    public static void exitingMessage() {  // REWORK MESSAGE
        GUI.renderScreen("YOU FOUND THE EXIT\nYOU SURVIVED");
    }

}
