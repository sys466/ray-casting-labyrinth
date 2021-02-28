import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

public class Main {

    public static void main(String[] args) throws InvocationTargetException, InterruptedException {

        Runnable initGUI = GUI::new;
        SwingUtilities.invokeAndWait(initGUI);

        while (true) {
            FOV.runRenderingCycle();
            GUI.calculatePlayerKeyboardMovement();
            GUI.calculatePlayerMouseMovement();
            Card.checkUnitPlayerPositionOnCard();
            if (Hunter.moveHunters()) {
                FOV.dyingMessage();
                break;
            }

            if (Map.checkUnitPlayerPositionOnExit() && Card.checkAllCardsCollected()) {
                FOV.exitingMessage();
                break;
            }
            Thread.sleep(1000 / 60);
        }

    }

}