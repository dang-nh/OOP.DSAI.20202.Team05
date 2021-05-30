import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


/**
 * Create a JPanel on which we draw and listen for keyboard and mouse events.
 */
public abstract class Frame extends JPanel implements KeyListener, MouseListener {

    // Keyboard states - Here are stored states for keyboard keys - is it down or not.
    private static boolean[] keyboardState = new boolean[525];

    // Mouse states - Here are stored states for mouse keys - is it down or not.
    private static boolean[] mouseState = new boolean[3];

    public Frame() {
        // Use double buffer to draw on the screen.
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.setBackground(Color.BLACK);

        // Add KeyListener and MouseListener to receive event from this component
        this.addKeyListener(this);
        this.addMouseListener(this);
    }

    /**
     * Used for drawing to the screen
     */
    public abstract void Draw(Graphics2D graphics2D);

    @Override
    public void paintComponent(Graphics graphic) {
        Graphics2D graphics2D = (Graphics2D) graphic;
        super.paintComponent(graphics2D);
        Draw(graphics2D);
    }

    /**
     * Is keyboard key "key" down?
     *
     * @param key Number of key for which you want to check the state.
     * @return true if the key is down, false if the key is not down.
     */
    public static boolean keyboardKeyState(int key) {
        return keyboardState[key];
    }

    @Override
    public void keyPressed(KeyEvent event) {
        keyboardState[event.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent event) {
        keyboardState[event.getKeyCode()] = false;
        keyReleasedGameWindow(event);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public abstract void keyReleasedGameWindow(KeyEvent e);

    // Mouse

    /**
     * Is mouse button "button" down?
     * Parameter "button" can be "MouseEvent.BUTTON1" - Indicates mouse button #1
     * or "MouseEvent.BUTTON2" - Indicates mouse button #2 ...
     *
     * @param button Number of mouse button for which you want to check the state.
     * @return true if the button is down, false if the button is not down.
     */
    public static boolean mouseButtonState(int button) {
        return mouseState[button - 1];
    }

    /**
     * Sets mouse key status.
     */
    private void mouseKeyStatus(MouseEvent e, boolean status) {
        if (e.getButton() == MouseEvent.BUTTON1)
            mouseState[0] = status;
        else if (e.getButton() == MouseEvent.BUTTON2)
            mouseState[1] = status;
        else if (e.getButton() == MouseEvent.BUTTON3)
            mouseState[2] = status;
    }

    /**
     * Methods of the mouse listener.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        mouseKeyStatus(e, true);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseKeyStatus(e, false);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    public static boolean[] getKeyboardState() {
        return keyboardState;
    }

    public static void setKeyboardState(boolean[] keyboardState) {
        Frame.keyboardState = keyboardState;
    }

    public static boolean[] getMouseState() {
        return mouseState;
    }

    public static void setMouseState(boolean[] mouseState) {
        Frame.mouseState = mouseState;
    }
}
