import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

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

    private static final boolean[] controls = {false, false, false, false};  // CHANGE TO MAP FOR BETTER CLARITY

    public static void renderScreen(String screenData) {
        viewPort.setText(screenData);
    }

    public static void calculatePlayerMovement() {  // DON'T FORGET TO CHANGE WITH MAP IMPLEMENTATION
        if (controls[0]) {
            ScreenRenderer.changeUnitPlayerPosition(0.1, false);
        }
        if (controls[1]) {
            ScreenRenderer.changeUnitPlayerPosition(-0.1, false);
        }
        if (controls[2]) {
            ScreenRenderer.changeUnitPlayerPosition(-0.1, true);
        }
        if (controls[3]) {
            ScreenRenderer.changeUnitPlayerPosition(0.1, true);
        }
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

    }

    private void initComponents() {

        // Fonts
        Font viewPortFont = new Font("Consolas", Font.PLAIN,16);

        // Tech panel components
        // ADD TIME
        // PLAYER HAS 5 MINUTES TO FIND THE EXIT

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
            public void keyPressed(KeyEvent e) {  // DON'T FORGET TO CHANGE WITH MAP IMPLEMENTATION
                if (e.getKeyCode() == KeyEvent.VK_W) {
                    controls[0] = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_S) {
                    controls[1] = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    controls[2] = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_D) {
                    controls[3] = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.exit(0);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {  // DON'T FORGET TO CHANGE WITH MAP IMPLEMENTATION
                if (e.getKeyCode() == KeyEvent.VK_W) {
                    controls[0] = false;
                }
                if (e.getKeyCode() == KeyEvent.VK_S) {
                    controls[1] = false;
                }
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    controls[2] = false;
                }
                if (e.getKeyCode() == KeyEvent.VK_D) {
                    controls[3] = false;
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
