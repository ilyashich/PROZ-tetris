import java.util.Random;

/**
 * Model - module which contains data and methods which can process it
 */
public class Model
{
    /**
     * List of available shapes with empty shape
     */
    public enum Shape
    {
        EmptyShape,
        ZShape,
        SShape,
        LineShape,
        TShape,
        OShape,
        LShape,
        JShape
    }

    /** Current shape of current peace */
    private Shape pieceShape;
    /**
     * Array which contains coordinates of every square of current tetris peace
     * first column - X
     * second column - Y
     */
    private final int[][] coordinates;
    /**
     * 3D array which contains coordinates of every square of every peace on the board
     */
    private final int[][][] coordsTable;

    /**
     * Constructor
     * current shape is empty shape
     */
    public Model()
    {
        coordinates = new int[4][2];
        coordsTable = new int[][][]
                {
                        //NoShape
                        {
                                {0, 0}, {0, 0}, {0, 0}, {0, 0}
                        },
                        //ZShape
                        {
                                {1, 0}, {0, 0}, {0, -1}, {-1, -1}
                        },
                        //SShape
                        {
                                {-1, 0}, {0, 0}, {0, -1}, {1, -1}
                        },
                        //LineShape
                        {
                                {-2, 0}, {-1, 0}, {0, 0}, {1, 0}
                        },
                        //TShape
                        {
                                {-1, 0}, {0, 0}, {1, 0}, {0, 1}
                        },
                        //SquareShape
                        {
                                {0, 0}, {1, 0}, {0, 1}, {1, 1}
                        },
                        //LShape
                        {
                                {-1, 0}, {0, 0}, {1, 0}, {1, -1}
                        },
                        //JShape
                        {
                                {1, 0}, {0, 0}, {-1, 0}, {-1, -1}
                        }
                };
        setPieceShape(Shape.EmptyShape);
    }

    /**
     * Peace shape setter
     * @param pieceShape peace shape to be set
     */
    public void setPieceShape(Shape pieceShape)
    {
        for (int i = 0; i < 4; i++)
        {
            System.arraycopy(coordsTable[pieceShape.ordinal()][i], 0, coordinates[i], 0, 2);
        }
        this.pieceShape = pieceShape;
    }

    /**
     * Setter of X coordinate of a given square
     * @param index square index (from 0 to 3)
     * @param x x coordinate
     */
    private void setX(int index, int x)
    {
        coordinates[index][0] = x;
    }

    /**
     * Setter of Y coordinate of a given square
     * @param index square index (from 0 to 3)
     * @param y y coordinate
     */
    private void setY(int index, int y)
    {
        coordinates[index][1] = y;
    }

    /**
     * Getter of x coordinate of a given piece square
     * @param index square index (from 0 to 3)
     * @return x coordinate
     */
    public int getX(int index)
    {
        return coordinates[index][0];
    }

    /**
     * Getter of y coordinate of a given piece square
     * @param index square index (from 0 to 3)
     * @return y coordinate
     */
    public int getY(int index)
    {
        return coordinates[index][1];
    }

    /**
     * Get current piece shape
     * @return current piece shape
     */
    public Shape getPieceShape()
    {
        return pieceShape;
    }

    /**
     * Choosing random shape and setting it as current piece shape
     */
    public void setRandomShape()
    {
        Random r = new Random();
        int x = Math.abs(r.nextInt()) % 7 + 1;
        Shape[] values = Shape.values();
        setPieceShape(values[x]);
    }

    /**
     * Get minimal X coordinate for current piece
     * @return minimal X coordinate for current piece
     */
    public int minValueX()
    {
        int min = coordinates[0][0];
        for (int i = 0; i < 4; i++)
        {
            min = Math.min(min, coordinates[i][0]);
        }
        return min;
    }

    /**
     * Get minimal Y coordinate for current piece
     * @return minimal Y coordinate for current piece
     */
    public int minValueY()
    {
        int min = coordinates[0][1];
        for (int i = 0; i < 4; i++)
        {
            min = Math.min(min, coordinates[i][1]);
        }
        return min;
    }

    /**
     * Rotate piece 90 degrees to the right, if it is a square piece,
     * don;t do anything
     * @return rotated piece
     */
    public Model rotateRight()
    {
        if (pieceShape == Shape.OShape)
        {
            return this;
        }

        Model result = new Model();
        result.pieceShape = pieceShape;

        for (int i = 0; i < 4; ++i)
        {
            result.setX(i, -getY(i));
            result.setY(i, getX(i));
        }
        return result;
    }

}
