import java.util.ArrayList;
import java.util.Random;

class Card {

    private static final Random RANDOM = new Random();
    private static final ArrayList<Card> CARDS = new ArrayList<>();
    private static final int CARDS_NUMBER = 3;  // Can be adjusted
    private static final double CARD_MAP_HALF_WIDTH = 0.15;

    private final double positionY;
    private final double positionX;

    private Card(double positionY, double positionX) {
        this.positionY = positionY;
        this.positionX = positionX;
    }

    static {
        final char[][] LEVEL_MAP = Map.getLevelMap();
        final int MAP_HEIGHT = Map.getMapHeight();
        final int MAP_WIDTH = Map.getMapWidth();
        int positionY;
        int positionX;
        for (int i = 0; i < CARDS_NUMBER; i++) {
            do {
                positionY = RANDOM.nextInt(MAP_HEIGHT - 1) + 1;
                positionX = RANDOM.nextInt(MAP_WIDTH - 1) + 1;
            } while (LEVEL_MAP[positionY][positionX] != ' ');
            LEVEL_MAP[positionY][positionX] = 'C';
            CARDS.add(new Card(positionY + 0.5, positionX + 0.5));
        }
    }

    public static boolean checkFOVVectorReachedCard(double vectorPositionY, double vectorPositionX) {
        for (Card card: CARDS) {
            if (vectorPositionY > card.positionY - CARD_MAP_HALF_WIDTH && vectorPositionY < card.positionY + CARD_MAP_HALF_WIDTH
            && vectorPositionX > card.positionX - CARD_MAP_HALF_WIDTH && vectorPositionX < card.positionX + CARD_MAP_HALF_WIDTH) {
                return true;
            }
        }
        return false;
    }

    public static void checkUnitPlayerPositionOnCard() {
        for (Card card: CARDS) {
            if ((int) Player.getUnitPlayerPositionY() == (int) card.positionY
            && (int) Player.getUnitPlayerPositionX() == (int) card.positionX) {
                CARDS.remove(card);
                Hunter.addHunter();
                Hunter.increaseSpeed();
                break;
            }
        }
    }

    public static boolean checkAllCardsCollected() {
        return CARDS.isEmpty();
    }

}
