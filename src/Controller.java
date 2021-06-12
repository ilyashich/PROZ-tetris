
import javax.swing.*;
import java.awt.*;

/**
 * Controller, center of application logic
 */
public class Controller
{
    /** Tetris board */
    private final TetrisBoard tetrisBoard;
    /** Board width (in squares)*/
    private final int BOARD_WIDTH;
    /** Board height (in squares) */
    private final int BOARD_HEIGHT;
    /** True if piece has felt */
    private boolean isFallingFinished = false;
    /** True if game has started */
    private boolean isStarted = false;
    /** True if game is paused */
    private boolean isPaused = false;
    /** Delay after removal of full line */
    private int removeDelay = 0;
    /** Number of removed lines, it's equal to player's score */
    private int numLinesRemoved = 0;
    /** Current X coordinate */
    private int currentX = 0;
    /** Current Y coordinate */
    private int currentY = 0;
    /** Timer */
    private final Timer timer;
    /** Current piece */
    private Model currentPiece;
    /** Board */
    private final Model.Shape[] board;

    /**
     * Constructor - sets model, view, timer delay,
     *               activates timer, sets board size (in squares), clears board
     * @param model piece
     * @param tetrisFrame view
     */
    public Controller(Model model, TetrisFrame tetrisFrame)
    {
        this.tetrisBoard = tetrisFrame.getBoard();
        this.BOARD_WIDTH = tetrisBoard.getBOARD_WIDTH();
        this.BOARD_HEIGHT = tetrisBoard.getBOARD_HEIGHT();
        this.currentPiece = model;
        this.timer = new Timer(400, tetrisBoard);
        timer.start();
        this.board = new Model.Shape[BOARD_WIDTH * BOARD_HEIGHT];

        clearBoard();
    }

    /**
     * Method which is called when
     * @see TetrisBoard#actionPerformed
     * when current piece felt down, new piece is created
     * if not, piece moves 1 square down
     * this method always checks if there are full lines
     */
    public void gameAction()
    {
        if (isFallingFinished)
        {
            isFallingFinished = false;
            newPiece();
        }
        else
        {
            oneLineDown();
        }
        removeFullLines();
    }

    /**
     * Check if game has started
     * @return true if game has started
     */
    public boolean isStarted()
    {
        return isStarted;
    }

    /**
     * Check if game is paused
     * @return true if game is paused
     */
    public boolean isPaused()
    {
        return isPaused;
    }

    /**
     * Check if current piece is empty shape
     * @return true if current piece is empty shape
     */
    public boolean isCurrentPieceNoShaped()
    {
        return currentPiece.getPieceShape() == Model.Shape.EmptyShape;
    }

    /**
     * Controller start, it's called in
     * @see TetrisFrame#init()
     */
    public void start()
    {
        if (isPaused)
        {
            return;
        }
        isStarted = true;
        isFallingFinished = false;
        numLinesRemoved = 0;
        clearBoard();
        newPiece();
        timer.start();

    }

    /**
     * Pause game
     */
    public void pause()
    {
        if (!isStarted)
        {
            return;
        }

        isPaused = !isPaused;
        if (isPaused)
        {
            timer.stop();
            tetrisBoard.setStatusText("paused");
        }
        else
        {
            timer.start();
            tetrisBoard.setStatusText(String.valueOf(numLinesRemoved));
        }
        tetrisBoard.repaint();
    }

    /**
     * Move piece one line down, if piece cannot be moved,
     * it means it's already felt down
     */
    public void oneLineDown()
    {
        if (!tryMove(currentPiece, currentX, currentY - 1))
        {
            pieceDropped();
        }
    }

    /**
     * Clear board,
     * whole board will contain empty shapes
     */
    private void clearBoard()
    {
        for (int i = 0; i < BOARD_HEIGHT * BOARD_WIDTH; ++i)
        {
            board[i] = Model.Shape.EmptyShape;
        }
    }

    /**
     * If SPACE button is pressed, piece will instantly go all the way down
     */
    public void dropDown()
    {
        int newY = currentY;
        while (newY > 0)
        {
            if (!tryMove(currentPiece, currentX, newY - 1))
            {
                break;
            }
            --newY;
        }
        pieceDropped();
    }

    /**
     * Check which piece is located at given coordinates
     * @param x x coordinate
     * @param y y coordinate
     * @return piece
     */
    private Model.Shape shapeAt(int x, int y)
    {
        return board[(y * BOARD_WIDTH) + x];
    }

    /**
     * Drawing additional grid
     * @param g Graphics object
     * @param width board width (in pixels)
     * @param height board height (in pixels)
     */
    public void paint(Graphics g, double width, double height)
    {
        int squareWidth = (int) width / BOARD_WIDTH;
        int squareHeight = (int) height / BOARD_HEIGHT;
        int boardTop = (int) height - BOARD_HEIGHT * squareHeight;


        for (int i = 0; i < BOARD_HEIGHT; ++i)
        {
            for (int j = 0; j < BOARD_WIDTH; ++j)
            {
                tetrisBoard.drawLines(g,j * squareWidth, boardTop + i * squareHeight);
                Model.Shape shape = shapeAt(j, BOARD_HEIGHT - i - 1);
                if (shape != Model.Shape.EmptyShape)
                {
                    tetrisBoard.drawSquare(g, j * squareWidth, boardTop + i * squareHeight, shape);
                }
            }
        }

        if (currentPiece.getPieceShape() != Model.Shape.EmptyShape)
        {
            for (int i = 0; i < 4; ++i)
            {
                int x = currentX + currentPiece.getX(i);
                int y = currentY - currentPiece.getY(i);
                tetrisBoard.drawSquare(g, x * squareWidth,
                        boardTop + (BOARD_HEIGHT - y - 1) * squareHeight,
                        currentPiece.getPieceShape());
            }
        }
    }

    /**
     * Generate random piece
     * If generated piece cannot move one square down, it means that game shall be stopped
     */
    private void newPiece()
    {
        currentPiece.setRandomShape();
        currentX = BOARD_WIDTH / 2 + 1;
        currentY = BOARD_HEIGHT - 1 + currentPiece.minValueY();

        if (!tryMove(currentPiece, currentX, currentY))
        {
            currentPiece.setPieceShape(Model.Shape.EmptyShape);
            timer.stop();
            isStarted = false;
            tetrisBoard.setStatusText("Game over, your score: " + numLinesRemoved);
        }
    }

    /**
     * Check if given piece can be moved to given coordinates
     * @param newPiece piece which we want to move
     * @param newX new x coordinate
     * @param newY new y coordinate
     * @return true if piece can be moved
     */
    private boolean tryMove(Model newPiece, int newX, int newY)
    {
        for (int i = 0; i < 4; ++i)
        {
            int x = newX + newPiece.getX(i);
            int y = newY - newPiece.getY(i);
            if (x < 0 || x >= BOARD_WIDTH || y < 0 || y >= BOARD_HEIGHT)
            {
                return false;
            }
            if (shapeAt(x, y) != Model.Shape.EmptyShape)
            {
                return false;
            }
        }

        currentPiece = newPiece;
        currentX = newX;
        currentY = newY;
        tetrisBoard.repaint();
        return true;
    }

    /**
     * Attempt to rotate piece
     * @param newPiece piece which we want to rotate
     */
    private void tryRotate(Model newPiece)
    {
        for (int i = 0; i < 4; ++i)
        {
            int x = currentX + newPiece.getX(i);
            int y = currentY - newPiece.getY(i);
            if (x == -2)
            {
                tryMove(newPiece, currentX + 2, currentY);
                return;
            }
            if (x == -1)
            {
                tryMove(newPiece, currentX + 1, currentY);
                return;
            }
            if (x == BOARD_WIDTH)
            {
                tryMove(newPiece, currentX - 1, currentY);
                return;
            }
            if (x == BOARD_WIDTH + 1)
            {
                tryMove(newPiece, currentX - 2, currentY);
                return;
            }
            if (x < -2 || x > BOARD_WIDTH + 1 || y < 0 || y >= BOARD_HEIGHT)
            {
                return;
            }
            if (shapeAt(x, y) != Model.Shape.EmptyShape)
            {
                return;
            }
        }

        currentPiece = newPiece;
        tetrisBoard.repaint();
    }

    /**
     * Method which is called when the piece is dropped down
     * This piece will remain as a part of the board until it's line will not be removed, until then it's stored in board array
     */
    private void pieceDropped()
    {
        for (int i = 0; i < 4; ++i)
        {
            int x = currentX + currentPiece.getX(i);
            int y = currentY - currentPiece.getY(i);
            board[(y * BOARD_WIDTH) + x] = currentPiece.getPieceShape();
        }

        isFallingFinished = true;
    }

    /**
     * Tries to remove full lines, and when it succeeds, it increments the score
     */
    private void removeFullLines()
    {
        int numFullLines = 0;

        for (int i = BOARD_HEIGHT - 1; i >= 0; --i)
        {
            boolean lineIsFull = true;

            for (int j = 0; j < BOARD_WIDTH; ++j)
            {
                if (shapeAt(j, i) == Model.Shape.EmptyShape)
                {
                    lineIsFull = false;
                    break;
                }
            }

            if(lineIsFull && removeDelay < 1)
            {
                removeDelay++;
                return;
            }


            if (lineIsFull && removeDelay == 1)
            {
                ++numFullLines;
                for (int k = i; k < BOARD_HEIGHT - 1; ++k)
                {
                    for (int j = 0; j < BOARD_WIDTH; ++j)
                    {
                        board[(k * BOARD_WIDTH) + j] = shapeAt(j, k + 1);
                    }
                }
            }
        }

        if (numFullLines > 0)
        {
            numLinesRemoved += numFullLines;
            tetrisBoard.setStatusText("Score: " + numLinesRemoved);
            removeDelay = 0;
        }
    }

    /**
     * Moves piece one square left
     */
    public void moveLeft()
    {
        tryMove(currentPiece, currentX - 1, currentY);
    }

    /**
     * Moves piece one square right
     */
    public void moveRight()
    {
        tryMove(currentPiece, currentX + 1, currentY);
    }

    /**
     * Rotates piece 90 degrees to the right
     */
    public void rotateRight()
    {
        tryRotate(currentPiece.rotateRight());
    }
}