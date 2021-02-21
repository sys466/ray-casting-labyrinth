import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

public class Main {

    public static void main(String[] args) throws InvocationTargetException, InterruptedException {

        Runnable initGUI = GUI::new;
        SwingUtilities.invokeAndWait(initGUI);

        while (true) {
            ScreenRenderer.runScreenRenderingCycle();
            GUI.calculatePlayerMovement();
            GUI.calculateMouseMovement();
            if (Hunter.moveHunters()) {
                ScreenRenderer.dyingMessage();
                break;
            }

            if (ScreenRenderer.checkUnitPlayerPositionNextToExit() && ScreenRenderer.checkIfAllCardsCollected()) {
                ScreenRenderer.exitingMessage();
                break;
            }
            if (ScreenRenderer.checkUnitPlayerPositionOnCard()) {
                ScreenRenderer.pickUpCard();
                Hunter.addHunter();
                Hunter.increaseSpeed();
            }
            Thread.sleep(1000 / 60);
        }

    }

}