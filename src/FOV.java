class FOV {

    private static final int SCREEN_HEIGHT = 40;
    private static final int SCREEN_WIDTH = 120;
    private static final StringBuilder screen = new StringBuilder();
    private static final String SCREEN_TEMPLATE = initScreenTemplate();
    private static final char[][] LEVEL_MAP = Map.getLevelMap();
    private static final double UNIT_PLAYER_VIEW_DISTANCE = 16.0;
    private static double unitPlayerFOVVectorWallOrExitDistance;
    private static double unitPlayerFOVVectorCardDistance;
    private static double unitPlayerFOVVectorHunterDistance;
    private static boolean isFOVVectorReachedWall;
    private static boolean isFOVVectorReachedExit;
    private static boolean isFOVVectorReachedCard;
    private static boolean isFOVVectorReachedHunter;

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

    public static void runRenderingCycle() {
        screen.delete(0, screen.length());
        screen.append(SCREEN_TEMPLATE);
        final double UNIT_PLAYER_VIEW_ANGLE = Player.getUnitPlayerViewAngle();
        for (int w = 0; w < SCREEN_WIDTH; w++) {
            calculateVectorDistance(w, UNIT_PLAYER_VIEW_ANGLE);
            calculateScreenData(w);
        }
        completeScreenData();
        GUI.renderScreen(screen.toString());
    }

    private static void calculateVectorDistance(int w, double unitPlayerViewAngle) {
        final double UNIT_PLAYER_POSITION_Y = Player.getUnitPlayerPositionY();
        final double UNIT_PLAYER_POSITION_X = Player.getUnitPlayerPositionX();

        double unitPlayerFOV = Math.PI / 4;
        double unitPlayerFOVVector = unitPlayerViewAngle - unitPlayerFOV / 2 + unitPlayerFOV / SCREEN_WIDTH * w;
        double unitPlayerFOVY = -Math.sin(unitPlayerFOVVector);
        double unitPlayerFOVX = Math.cos(unitPlayerFOVVector);

        isFOVVectorReachedWall = false;
        isFOVVectorReachedExit = false;
        isFOVVectorReachedCard = false;
        isFOVVectorReachedHunter = false;
        unitPlayerFOVVectorWallOrExitDistance = 0;

        while (unitPlayerFOVVectorWallOrExitDistance < UNIT_PLAYER_VIEW_DISTANCE) {

            // Calculating FOV vector's position
            unitPlayerFOVVectorWallOrExitDistance += 0.1;
            double positionYChange = UNIT_PLAYER_POSITION_Y + unitPlayerFOVY * unitPlayerFOVVectorWallOrExitDistance;
            double positionXChange = UNIT_PLAYER_POSITION_X + unitPlayerFOVX * unitPlayerFOVVectorWallOrExitDistance;

            // Checking if FOV vector reached wall
            if (LEVEL_MAP[(int) positionYChange][(int) positionXChange] == '#') {
                isFOVVectorReachedWall = true;
                break;
            // Checking if FOV vector reached exit
            } else if (LEVEL_MAP[(int) positionYChange][(int) positionXChange] == 'E') {
                isFOVVectorReachedExit = true;
                break;
            }

            // Checking if FOV vector reached card
            if (!isFOVVectorReachedCard) {
                if (Card.checkFOVVectorReachedCard(positionYChange, positionXChange)) {
                    isFOVVectorReachedCard = true;
                    unitPlayerFOVVectorCardDistance = unitPlayerFOVVectorWallOrExitDistance;
                }
            }

            // Checking if FOV vector reached hunter
            if (!isFOVVectorReachedHunter) {
                if (Hunter.checkFOVVectorReachedHunter(positionYChange, positionXChange)) {
                    isFOVVectorReachedHunter = true;
                    unitPlayerFOVVectorHunterDistance = unitPlayerFOVVectorWallOrExitDistance;
                }
            }
        }
    }

    private static void calculateScreenData(int w) {

        // Drawing wall or exit
        if (isFOVVectorReachedWall || isFOVVectorReachedExit) {
            int wallOrExitHeight = (int) (SCREEN_HEIGHT / 2 - SCREEN_HEIGHT / 2 / (unitPlayerFOVVectorWallOrExitDistance * 1.25));
            if (wallOrExitHeight < 0) {
                wallOrExitHeight = 0;
            }

            for (int h = wallOrExitHeight; h < SCREEN_HEIGHT / 2; h++) {
                if (wallOrExitHeight < SCREEN_HEIGHT * 0.125) {
                    screen.setCharAt(h * 121 + w, isFOVVectorReachedWall ? '‖' : 'X');
                } else if (wallOrExitHeight < SCREEN_HEIGHT * 0.25) {
                    screen.setCharAt(h * 121 + w, isFOVVectorReachedWall ? '|' : 'x');
                } else if (wallOrExitHeight < SCREEN_HEIGHT * 0.375) {
                    screen.setCharAt(h * 121 + w, isFOVVectorReachedWall ? ':' : '+');
                } else {
                    screen.setCharAt(h * 121 + w, '·');
                }
            }
        }

        // Drawing card
        if (isFOVVectorReachedCard) {
            int cardHeight = (int) (SCREEN_HEIGHT / 2 - (Card.getCardFovHalfHeight() - unitPlayerFOVVectorCardDistance * 0.25));

            for (int h = cardHeight; h < SCREEN_HEIGHT / 2; h++) {
                screen.setCharAt(h * 121 + w, unitPlayerFOVVectorCardDistance > 4 ? ':' : '#');
            }
        }

        // Drawing hunter
        if (isFOVVectorReachedHunter) {
            int hunterHeight = (int) (SCREEN_HEIGHT / 2 - SCREEN_HEIGHT / 2 / (unitPlayerFOVVectorHunterDistance * 1.25));
            if (hunterHeight < 0) {
                hunterHeight = 0;
            }

            for (int h = hunterHeight; h < SCREEN_HEIGHT / 2; h++) {
                screen.setCharAt(h * 121 + w, unitPlayerFOVVectorHunterDistance > 4 ? 'o' : 'O');
            }
        }
    }

    private static void completeScreenData() {
        for (int h = SCREEN_HEIGHT / 2 - 1; h >= 0; h--) {
            screen.append(screen.substring(h * 121, (h + 1) * 121));
        }
    }

    public static void exitingMessage() {
        GUI.renderScreen("YOU FOUND THE EXIT\nYOU SURVIVED");
    }

    public static void dyingMessage() {
        GUI.renderScreen("YOU ARE DEAD\nGAME OVER");
    }

}
