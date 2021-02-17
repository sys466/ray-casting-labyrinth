import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class GUI extends JFrame {

    private static double mouseX;
    private static Robot robot;

    static {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private static final JTextPane viewPort = new JTextPane();

    private static final HashMap<String, Boolean> controls = new HashMap<>();

    public static void renderScreen(String screenData) {
        viewPort.setText(screenData);
    }

    public static void calculatePlayerMovement() {
        if (controls.get("UP")) {
            ScreenRenderer.changeUnitPlayerPosition(0.05, false);
        }
        if (controls.get("DOWN")) {
            ScreenRenderer.changeUnitPlayerPosition(-0.05, false);
        }
        if (controls.get("LEFT")) {
            ScreenRenderer.changeUnitPlayerPosition(-0.05, true);
        }
        if (controls.get("RIGHT")) {
            ScreenRenderer.changeUnitPlayerPosition(0.05, true);
        }
        ScreenRenderer.checkUnitPlayerPositionChange();
    }

    public static void calculateMouseMovement() {
        double newMouseX = MouseInfo.getPointerInfo().getLocation().getX();
        double changeValue = Math.abs(newMouseX - mouseX) / 1000;
        if (newMouseX > mouseX) {
            ScreenRenderer.changeUnitPlayerViewAngle(changeValue);
        } else if (newMouseX < mouseX) {
            ScreenRenderer.changeUnitPlayerViewAngle(-changeValue);
        }
        mouseX = newMouseX;
        if (mouseX > 1260 || mouseX < 460) {  // MAKE LESS SPECIFIC
            robot.mouseMove(960, 540);  // CALCULATE CENTER BASED ON RESOLUTION
            mouseX = 960;
        }
    }

    public GUI() {
        super("ray-casting-labyrinth by sys466");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 920);
        setLocationRelativeTo(null);
        setBackground(Color.BLACK);
        setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
                new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(0, 0),"null")
        );
        initComponents();
        setLayout(null);
        setResizable(false);
        setVisible(true);
        controls.put("UP", false);
        controls.put("DOWN", false);
        controls.put("LEFT", false);
        controls.put("RIGHT", false);
    }

    private void initComponents() {

        // Fonts
        Font viewPortFont = new Font("Consolas", Font.PLAIN,16);

        // Tech panel components
        // ADD TIME
        // PLAYER HAS 10 MINUTES TO FIND THE EXIT

        // View panel components
        SimpleAttributeSet textCenterPosition = new SimpleAttributeSet();
        StyleConstants.setAlignment(textCenterPosition, StyleConstants.ALIGN_CENTER);
        viewPort.setParagraphAttributes(textCenterPosition, false);
        viewPort.setFont(viewPortFont);
        viewPort.setBackground(Color.BLACK);
        viewPort.setForeground(Color.WHITE);
        viewPort.setEditable(false);
        viewPort.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_W) {
                    controls.replace("UP", true);
                }
                if (e.getKeyCode() == KeyEvent.VK_S) {
                    controls.replace("DOWN", true);
                }
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    controls.replace("LEFT", true);
                }
                if (e.getKeyCode() == KeyEvent.VK_D) {
                    controls.replace("RIGHT", true);
                }
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.exit(0);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_W) {
                    controls.replace("UP", false);
                }
                if (e.getKeyCode() == KeyEvent.VK_S) {
                    controls.replace("DOWN", false);
                }
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    controls.replace("LEFT", false);
                }
                if (e.getKeyCode() == KeyEvent.VK_D) {
                    controls.replace("RIGHT", false);
                }
            }
        });
        viewPort.setFocusable(true);

        // Status panel components
        // ADD CONTROLS DESCRIPTION

        // Panels
        JPanel techPanel = new JPanel();
        techPanel.setBounds(0, 0, 1200, 40);
        techPanel.setLayout(new BorderLayout());
        techPanel.setBackground(Color.DARK_GRAY);

        JPanel viewPanel = new JPanel();
        viewPanel.setBounds(0, 40, 1200, 760);
        viewPanel.setLayout(new BorderLayout());
        viewPanel.setBackground(Color.BLACK);
        viewPanel.add(viewPort);

        JPanel statusPanel = new JPanel();
        statusPanel.setBounds(0, 800, 1200, 120);
        statusPanel.setLayout(new BorderLayout());
        statusPanel.setBackground(Color.DARK_GRAY);

        // Adding panels
        add(techPanel);
        add(viewPanel);
        add(statusPanel);
    }

}
