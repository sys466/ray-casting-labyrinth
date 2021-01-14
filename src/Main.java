import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

public class Main {

    public static void main(String[] args) throws InvocationTargetException, InterruptedException {

        ScreenRenderer.initMap();
        Runnable initGUI = GUI::new;
        SwingUtilities.invokeAndWait(initGUI);

        while (true) {
            ScreenRenderer.runScreenRenderingCycle();
            GUI.calculatePlayerMovement();
            GUI.calculateMouseMovement();
            if (ScreenRenderer.checkUnitPlayerPositionOnExit()) {
                ScreenRenderer.exitingMessage();
                break;
            }
            Thread.sleep(1000 / 60);
        }

    }

}