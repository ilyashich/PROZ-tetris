import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Drawing panel
 */
public class TetrisBoard extends JPanel implements ActionListener
{
    /** Board width (in squares) */
    private final int BOARD_WIDTH = 10;
    /** Board height (in squares) */
    private final int BOARD_HEIGHT = 22;
    /** Status bar with score */
    private final JTextField statusBar;
    /** Controller */
    private Controller controller;

    /**
     * Constructor, setting status bar, adding key listener
     * @param parent frame
     */
    TetrisBoard(TetrisFrame parent)
    {
        setFocusable(true);
        statusBar = parent.getStatusBar();
        addKeyListener(new TAdapter());
        setBackground(Color.black);
    }

    /**
     * Drawing grid
     * @param g Graphics object
     * @param x x coordinate
     * @param y y coordinate
     */
    public void drawLines(Graphics g, int x, int y)
    {
        Color color = new Color(27, 62, 99); //1B3E63
        g.setColor(color);


        g.drawLine(x, y + squareHeight() - 1, x, y);
        g.drawLine(x, y, x + squareWidth() - 1, y);


        g.drawLine(x + 1, y + squareHeight() - 1,
                x + squareWidth() - 1, y + squareHeight() - 1);

        g.drawLine(x + squareWidth() - 1, y + squareHeight() - 1,
                x + squareWidth() - 1, y + 1);
    }

    /**
     * Controller setter
     * @param controller controller to be set
     */
    public void setController(Controller controller)
    {
        this.controller = controller;
    }

    /**
     * Board width getter
     * @return board width
     */
    public int getBOARD_WIDTH()
    {
        return BOARD_WIDTH;
    }

    /**
     * Board height getter
     * @return board height
     */
    public int getBOARD_HEIGHT()
    {
        return BOARD_HEIGHT;
    }

    /**
     * This method waits for action, and when it's performed,
     * gameAction is called
     * @param e action event
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        controller.gameAction();
    }


    /**
     * Drawing
     * @param g Graphics object
     */
    public void paint(Graphics g)
    {
        super.paint(g);
        controller.paint(g, getSize().getWidth(), getSize().getHeight());

    }

    /**
     * Method, which returns square width in pixels
     * @return square width in pixels
     */
    private int squareWidth()
    {
        return (int) getSize().getWidth() / BOARD_WIDTH;
    }

    /**
     * Method, which returns square height in pixels
     * @return square height in pixels
     */
    private int squareHeight()
    {
        return (int) getSize().getHeight() / BOARD_HEIGHT;
    }

    /**
     * Method which draws one square at (x,y) coordinates
     * @param g Graphics object
     * @param x x coordinate
     * @param y y coordinate
     * @param shape shape which we want do draw
     */
    public void drawSquare(Graphics g, int x, int y, Model.Shape shape)
    {
        Color[] colors =
                {
                        new Color(0, 0, 0), new Color(255, 0, 0),
                        new Color(0, 255, 0), new Color(52, 223, 255),
                        new Color(185, 9, 212), new Color(255, 255, 0),
                        new Color(255, 146, 0), new Color(0, 0, 255)
                };


        Color color = colors[shape.ordinal()];

        g.setColor(color);
        g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);

        g.setColor(color.brighter());
        g.drawLine(x, y + squareHeight() - 1, x, y);
        g.drawLine(x, y, x + squareWidth() - 1, y);

        g.setColor(color.darker());
        g.drawLine(x + 1, y + squareHeight() - 1,
                x + squareWidth() - 1, y + squareHeight() - 1);
        g.drawLine(x + squareWidth() - 1, y + squareHeight() - 1,
                x + squareWidth() - 1, y + 1);
    }

    /**
     * Status bar setter
     * @param text  status bar with score
     */
    public void setStatusText(String text)
    {
        statusBar.setText(text);
    }

    /**
     * Class for collecting keyboard events
     */
    private class TAdapter extends KeyAdapter
    {
        /**
         * Method, which is called when the key is pressed
         * @param e keyboard event
         */
        public void keyPressed(KeyEvent e)
        {

            if (!controller.isStarted() || controller.isCurrentPieceNoShaped())
            {
                return;
            }

            int keycode = e.getKeyCode();

            if (keycode == 'p' || keycode == 'P')
            {
                controller.pause();
                return;
            }

            if (controller.isPaused())
            {
                return;
            }

            switch (keycode)
            {
                case KeyEvent.VK_LEFT:
                    controller.moveLeft();
                    break;
                case KeyEvent.VK_RIGHT:
                    controller.moveRight();
                    break;
                case KeyEvent.VK_UP:
                    controller.rotateRight();
                    break;
                case KeyEvent.VK_DOWN:
                    controller.oneLineDown();
                    break;
                case KeyEvent.VK_SPACE:
                    controller.dropDown();
                    break;
            }

        }
    }
}