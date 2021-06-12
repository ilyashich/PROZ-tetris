import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Frame class, which represents View
 */
public class TetrisFrame extends JFrame
{
    /** Text field, where points number is shown */
    private final JTextField statusBar;
    /** Main frame */
    private final TetrisBoard board;
    /** Controller
     * @see Controller#Controller(Model, TetrisFrame)
     */
    Controller controller;

    /**
     * Constructor
     * initializing text field and main frame
     */
    public TetrisFrame()
    {
        statusBar = new JTextField("Score: 0");
        this.board = new TetrisBoard(this);
    }

    /**
     * Initialization, setting frame and text field parameters
     */
    public void init()
    {
        this.setLayout(new BorderLayout());
        this.add(statusBar, BorderLayout.SOUTH);
        this.add(board, BorderLayout.CENTER);
        statusBar.setPreferredSize(new Dimension(400, 30));
        statusBar.setFont(new Font("Times New Roman", Font.BOLD, 20));
        statusBar.setBackground(Color.black);
        statusBar.setForeground(Color.yellow);
        statusBar.setBorder(new LineBorder( new Color(27,62,99), 1));
        board.setController(controller);
        controller.start();
        this.setPreferredSize(new Dimension(400, 880));
        this.setBounds(200, 100, 400, 880);
        setTitle("Tetris");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.pack();
        setVisible(true);
        setResizable(false);
    }

    /**
     * Text field getter
     * @return text field
     */
    JTextField getStatusBar()
    {
        return statusBar;
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
     * Main frame getter
     * @return main frame
     */
    public TetrisBoard getBoard()
    {
        return board;
    }
}