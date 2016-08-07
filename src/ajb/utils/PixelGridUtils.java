package ajb.utils;

import java.awt.Point;
import java.util.List;

import ajb.domain.Parameters;
import ajb.domain.Pixel;
import ajb.random.Rng;

public class PixelGridUtils {

    private static int previousNeighbour = 0;

    /**
     * Returns true if the passed in point is within the boundaries of the passed in grid i.e.
     * in a grid initialise with 10 rows and 10 columns points with x or y above 10 are not within the grid.
     *
     * @param point {@link Point}
     * @param grid  {@link Pixel}[][]
     * @return {@link boolean}
     */
    public static boolean isPointWithinGrid(Point point, Pixel[][] grid) {
        try {
            @SuppressWarnings("unused")
            Pixel pixel = grid[point.x][point.y];
            return true;
        } catch (IndexOutOfBoundsException ioobe) {
            return false;
        }
    }

    /**
     * Outputs the passed in grid to the console
     *
     * @param grid {@link Pixel}[][]
     */
    public static void outputGridAsAscii(Pixel[][] grid) {
        for (int x = 0; x < grid.length; x++) {
            StringBuilder strBld = new StringBuilder();
            for (int y = 0; y < grid[0].length; y++) {
                switch (grid[x][y].value) {
                    case EMPTY:
                        strBld.append(" ");
                        break;
                    case FILLED:
                        strBld.append(".");
                        break;
                    case BORDER:
                        strBld.append("x");
                        break;
                }
            }
            System.out.println(strBld.toString());
        }
    }

    /**
     * Simply populates the passed in grid with {@link Pixel} objects
     */
    public static void initEmptyGrid(Pixel[][] grid, int rows, int cols) {
        for (int x = 0; x < rows; x++)
            for (int y = 0; y < cols; y++)
                grid[x][y] = new Pixel();
    }

    /**
     * Takes the passed in grid and returns a grid that has been floored i.e. reduced so that rows and column with no filled pixels on them are removed
     *
     * @param grid {@link Pixel}[][]
     * @return {@link Pixel}[][]
     */
    public static Pixel[][] removeEmptyCells(Pixel[][] grid) {
        Pixel[][] flooredGrid;
        int lastFilledRow = 0;
        int lastFilledCol = 0;
        int firstFilledRow = grid.length;
        int firstFilledColumn = grid[0].length;
        for (int r = 0; r < grid.length; r++) {
            boolean empty = true;
            for (int c = 0; c < grid[0].length; c++) {
                boolean colEmpty = true;
                if (grid[r][c].value != Pixel.State.EMPTY) {
                    if (firstFilledRow > r)
                        firstFilledRow = r;
                    if (firstFilledColumn > c)
                        firstFilledColumn = c;
                    empty = false;
                    colEmpty = false;
                }
                if (!colEmpty && c > lastFilledCol)
                    lastFilledCol = c;
            }
            if (!empty)
                lastFilledRow = r;
        }
        flooredGrid = new Pixel[lastFilledRow - (firstFilledRow - 1)][lastFilledCol - (firstFilledColumn - 1)];
        int newRow = 0;
        for (int r = firstFilledRow; r < lastFilledRow + 1; r++) {
            int newCol = 0;
            for (int c = firstFilledColumn; c < lastFilledCol + 1; c++) {
                flooredGrid[newRow][newCol] = grid[r][c];
                newCol++;
            }
            newRow++;
        }
        return flooredGrid;
    }

    /**
     * Takes the passed in grid and returns a grid that contains the original plus a mirrored copy
     */
    public static Pixel[][] mirrorCopyGridHorizontally(Pixel[][] halfGrid) {
        int rows = halfGrid.length;
        int cols = (halfGrid[0].length * 2);

        Pixel[][] fullGrid = new Pixel[rows][cols];

        // Copy left to right
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < (cols / 2); c++) {
                fullGrid[r][c] = halfGrid[r][c];
                fullGrid[r][(cols - 1) - c] = halfGrid[r][c];
            }
        }
        return fullGrid;
    }

    /**
     * Takes the passed in grid and returns a grid that contains the original
     * plus a mirrored copy
     *
     * @param halfGrid {@link Pixel}[][]
     * @return {@link Pixel}[][]
     */
    public static Pixel[][] mirrorCopyGridVertically(Pixel[][] halfGrid) {
        int rows = (halfGrid.length * 2);
        int cols = halfGrid[0].length;

        Pixel[][] fullGrid = new Pixel[rows][cols];

        // Copy left to right
        for (int r = 0; r < rows / 2; r++) {
            for (int c = 0; c < cols; c++) {
                fullGrid[r][c] = halfGrid[r][c];
                fullGrid[(rows - 1) - r][c] = halfGrid[r][c];
            }
        }

        return fullGrid;
    }

    /**
     * Takes the passed in grid extends it by 2 then puts a border pixel next to every edge pixel i.e. pixels that do not have another filled pixel next to them
     */
    public static Pixel[][] addBorders(Pixel[][] grid) {
        Pixel[][] result = PixelGridUtils.extendGrid(grid, 2);

        for (int r = 0; r < result.length; r++) {
            for (int c = 0; c < result[0].length; c++) {
                if (result[r][c].value == Pixel.State.FILLED) {
                    // Top
                    if (result[r == 0 ? 0 : r - 1][c].value != Pixel.State.FILLED && result[r == 0 ? 0 : r - 1][c].value != Pixel.State.SECONDARY)
                        result[r == 0 ? 0 : r - 1][c].value = Pixel.State.BORDER;
                    // Left
                    if (result[r][c == 0 ? 0 : c - 1].value != Pixel.State.FILLED && result[r][c == 0 ? 0 : c - 1].value != Pixel.State.SECONDARY)
                        result[r][c == 0 ? 0 : c - 1].value = Pixel.State.BORDER;
                    // Right
                    if (result[r][c == (result[0].length / 2) - 1 ? (result[0].length / 2) - 1 : c + 1].value != Pixel.State.FILLED && result[r][c == (result[0].length / 2) - 1 ? (result[0].length / 2) - 1 : c + 1].value != Pixel.State.SECONDARY)
                        result[r][c == (result[0].length / 2) - 1 ? (result[0].length / 2) - 1 : c + 1].value = Pixel.State.BORDER;
                    // Bottom
                    if (result[r == result.length - 1 ? result.length - 1 : r + 1][c].value != Pixel.State.FILLED && result[r == result.length - 1 ? result.length - 1 : r + 1][c].value != Pixel.State.SECONDARY)
                        result[r == result.length - 1 ? result.length - 1 : r + 1][c].value = Pixel.State.BORDER;
                }
            }
        }

        return result;
    }

    /**
     * Takes the passed in grid and extends it by the passed in amount
     *
     * @param grid         {@link Pixel}[][]
     * @param extendAmount {@link int}
     * @return {@link Pixel}[][]
     */
    public static Pixel[][] extendGrid(Pixel[][] grid, int extendAmount) {
        Pixel[][] extendedGrid = new Pixel[grid.length + extendAmount][grid[0].length + extendAmount];
        initEmptyGrid(extendedGrid, grid.length + extendAmount, grid[0].length + extendAmount);

        for (int r = 0; r < grid.length; r++)
            for (int c = 0; c < grid[0].length; c++)
                extendedGrid[r + (extendAmount / 2)][c + (extendAmount / 2)] = grid[r][c];
        return extendedGrid;
    }

    /**
     * Loops through each pixel in the grid and works out if they are surrounded
     * by filled pixels i.e. a straight path through other pixels until it hits
     * a pixel of value Pixel.State.FILLED
     *
     * @param grid {@link Pixel}[][]
     */
    public static void fillEmptySurroundedPixelsInGrid(Pixel[][] grid) {
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                if (grid[r][c].value == Pixel.State.EMPTY) {
                    boolean filledPixelAbove = false;
                    boolean filledPixelBelow = false;
                    boolean filledPixelOnTheLeft = false;
                    boolean filledPixelOnTheRight = false;

                    for (int r1 = r - 1; r1 > 0; r1--) {
                        if (grid[r1][c].value == Pixel.State.FILLED) {
                            filledPixelAbove = true;
                            break;
                        }
                    }
                    for (int r1 = r + 1; r1 < grid.length; r1++) {
                        if (grid[r1][c].value == Pixel.State.FILLED) {
                            filledPixelBelow = true;
                            break;
                        }
                    }
                    for (int c1 = c - 1; c1 > 0; c1--) {
                        if (grid[r][c1].value == Pixel.State.FILLED) {
                            filledPixelOnTheLeft = true;
                            break;
                        }
                    }
                    for (int c1 = c + 1; c1 < grid[0].length; c1++) {
                        if (grid[r][c1].value == Pixel.State.FILLED) {
                            filledPixelOnTheRight = true;
                            break;
                        }
                    }
                    if (filledPixelAbove && filledPixelBelow && filledPixelOnTheLeft && filledPixelOnTheRight) {
                        grid[r][c].value = Pixel.State.SECONDARY;
                    }
                }
            }
        }
    }

    /**
     * Loops through each pixel in the grid and works out if they are surrounded
     * by filled pixels i.e. a straight path through other pixels until it hits
     * a pixel of value Pixel.State.FILLED without going over any pixels with value
     * Pixel.State.EMPTY
     *
     * @param grid {@link Pixel}[][]
     */
    public static void addNoiseToFlatPixels(Pixel[][] grid, Parameters param) {
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                if (grid[r][c].value == Pixel.State.SECONDARY && Rng.anInt(3) == 0) {
                    boolean filledPixelAbove = false;
                    boolean filledPixelBelow = false;
                    boolean filledPixelOnTheLeft = false;
                    boolean filledPixelOnTheRight = false;

                    for (int r1 = r - 1; r1 > 0; r1--)
                        if (grid[r1][c].value == Pixel.State.EMPTY) {
                            filledPixelAbove = false;
                            break;
                        } else if (grid[r1][c].value == Pixel.State.FILLED) {
                            filledPixelAbove = true;
                            break;
                        }
                    for (int r1 = r + 1; r1 < grid.length; r1++)
                        if (grid[r1][c].value == Pixel.State.EMPTY) {
                            filledPixelBelow = false;
                            break;
                        } else if (grid[r1][c].value == Pixel.State.FILLED) {
                            filledPixelBelow = true;
                            break;
                        }
                    for (int c1 = c - 1; c1 > 0; c1--)
                        if (grid[r][c1].value == Pixel.State.EMPTY) {
                            filledPixelOnTheLeft = false;
                            break;
                        } else if (grid[r][c1].value == Pixel.State.FILLED) {
                            filledPixelOnTheLeft = true;
                            break;
                        }
                    for (int c1 = c + 1; c1 < grid[0].length; c1++)
                        if (grid[r][c1].value == Pixel.State.EMPTY) {
                            filledPixelOnTheLeft = false;
                            break;
                        } else if (grid[r][c1].value == Pixel.State.FILLED) {
                            filledPixelOnTheRight = true;
                            break;
                        }
                    setNoiseValue(grid, param, r, c, filledPixelAbove, filledPixelBelow, filledPixelOnTheLeft, filledPixelOnTheRight);
                }
            }
        }
    }

    private static void setNoiseValue(Pixel[][] grid, Parameters param, int r, int c, boolean filledPixelAbove, boolean filledPixelBelow, boolean filledPixelOnTheLeft, boolean filledPixelOnTheRight) {
        /**
         * TODO : Rework to make it more flexible
         */
        if (Utility.countTrues(filledPixelAbove, filledPixelBelow, filledPixelOnTheLeft, filledPixelOnTheRight) == 4) {
            int secondary = Rng.anInt(param.greyNoisePercentage);
            int border = Rng.anInt(param.blackNoisePercentage);
            int filled = Rng.anInt(param.colorNoisePercentage);
            if (secondary > border && secondary > filled)
                grid[r][c].value = Pixel.State.SECONDARY;
            if (border > secondary && border > filled)
                grid[r][c].value = Pixel.State.BORDER;
            if (filled > secondary && filled > border)
                grid[r][c].value = Pixel.State.FILLED;
        }
    }

    public static Point getRandomFilledPoint(Pixel[][] grid) {

        Point point = null;

        while (point == null) {

            int x = Rng.intBetween(1, grid.length - 1);
            int y = Rng.intBetween(1, grid[0].length - 1);

            Pixel possiblePixel = grid[x][y];

            if (possiblePixel.value == Pixel.State.FILLED) {
                point = new Point();
                point.x = x;
                point.y = y;
            }
        }

        return point;

    }

    public static void mergeGridsRandomly(Pixel[][] sourceGrid, Pixel[][] targetGrid) {
        int attempts = 0;
        Point targetPoint = null;

        while (targetPoint == null && attempts < 10) {
            Point potentialPoint = getRandomFilledPoint(targetGrid);
            potentialPoint.x = potentialPoint.x - sourceGrid.length - 1;
            potentialPoint.y = potentialPoint.y - sourceGrid[0].length - 1;
            if (isPointWithinGrid(potentialPoint, targetGrid))
                targetPoint = potentialPoint;
            attempts++;
        }
        if (targetPoint != null) {
            int startY = targetPoint.y;
            for (int r = 0; r < sourceGrid.length; r++) {
                for (int c = 0; c < sourceGrid[0].length; c++) {
                    targetGrid[targetPoint.x][targetPoint.y].value = sourceGrid[r][c].value;
                    targetPoint.y = targetPoint.y + 1;
                }
                targetPoint.x = targetPoint.x + 1;
                targetPoint.y = startY;
            }
        }
    }

    public static Pixel[][] floorHorizontally(Pixel[][] grid) {
        Pixel[][] flooredGrid;

        int lastFilledCol = 0, firstFilledColumn = grid[0].length;

        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                boolean colEmpty = true;
                if (grid[r][c].value != Pixel.State.EMPTY) {
                    if (firstFilledColumn > c)
                        firstFilledColumn = c;
                    colEmpty = false;
                }
                if (!colEmpty && c > lastFilledCol)
                    lastFilledCol = c;
            }
        }
        flooredGrid = new Pixel[grid.length][lastFilledCol - (firstFilledColumn - 1)];

        int newRow = 0;

        for (int r = 0; r < grid.length; r++) {
            int newCol = 0;

            for (int c = firstFilledColumn; c < lastFilledCol + 1; c++) {
                flooredGrid[newRow][newCol] = grid[r][c];
                newCol++;
            }
            newRow++;
        }
        return flooredGrid;
    }

    public static Pixel[][] floorVertically(Pixel[][] grid) {

        Pixel[][] flooredGrid = null;

        int lastFilledRow = 0;
        int firstFilledRow = grid.length;

        for (int r = 0; r < grid.length; r++) {
            boolean empty = true;

            for (int c = 0; c < grid[0].length; c++) {

                if (grid[r][c].value != Pixel.State.EMPTY) {
                    if (firstFilledRow > r) {
                        firstFilledRow = r;
                    }

                    empty = false;
                }
            }

            if (!empty) {
                lastFilledRow = r;
            }
        }

        flooredGrid = new Pixel[lastFilledRow - (firstFilledRow - 1)][grid[0].length];

        int newRow = 0;

        for (int r = firstFilledRow; r < lastFilledRow + 1; r++) {
            int newCol = 0;

            for (int c = 0; c < grid[0].length; c++) {
                flooredGrid[newRow][newCol] = grid[r][c];
                newCol++;
            }

            newRow++;
        }

        return flooredGrid;
    }

    public static Pixel[][] combineGrids(List<Pixel[][]> grids) {

        int maxRows = 0;
        int maxCols = grids.get(0)[0].length;

        for (Pixel[][] grid : grids) {
            maxRows += grid.length;
        }

        Pixel[][] combinedGrid = new Pixel[maxRows][maxCols];
        initEmptyGrid(combinedGrid, maxRows, maxCols);

        int startingRow = 0;

        for (Pixel[][] grid : grids) {
            for (int r = 0; r < grid.length; r++) {
                for (int c = 0; c < maxCols; c++) {

                    combinedGrid[startingRow + r][c].value = grid[r][c].value;
                }
            }

            startingRow += grid.length;
        }

        return combinedGrid;
    }

    public static void setPixelDepth(Pixel[][] grid) {
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {

                if (grid[r][c].value != Pixel.State.EMPTY &&
                        grid[r][c].value != Pixel.State.BORDER) {

                    Pixel.State pixelValue = grid[r][c].value;

                    int noOfSamePixelsAbove = 0;
                    int noOfSamePixelsBelow = 0;
                    int noOfSamePixelsOnTheLeft = 0;
                    int noOfSamePixelsOnTheRight = 0;

                    for (int r1 = r - 1; r1 > 0; r1--) {
                        if (pixelValue == grid[r1][c].value) {
                            noOfSamePixelsAbove++;
                        }
                    }

                    for (int r1 = r + 1; r1 < grid.length; r1++) {
                        if (pixelValue == grid[r1][c].value) {
                            noOfSamePixelsBelow++;
                        }
                    }

                    for (int c1 = c - 1; c1 > 0; c1--) {
                        if (pixelValue == grid[r][c1].value) {
                            noOfSamePixelsOnTheLeft++;
                        }
                    }

                    for (int c1 = c + 1; c1 < grid[0].length; c1++) {
                        if (pixelValue == grid[r][c1].value) {
                            noOfSamePixelsOnTheRight++;
                        }
                    }

                    int depth1 = Math.min(noOfSamePixelsAbove, noOfSamePixelsBelow);
                    int depth2 = Math.min(noOfSamePixelsOnTheLeft, noOfSamePixelsOnTheRight);

                    grid[r][c].depth = Math.min(depth1, depth2);
                }
            }
        }
    }

    public static Point getRandomAdjacentPoint(Point point, Pixel[][] grid, Parameters parameters) {
        Point[] neighbours = getNeightboursPoints(point, grid);

        // go to a random neighbour
        Point newPoint = Rng.aFloat() < parameters.tendancyToKeepLine ? neighbours[previousNeighbour] : null;
        while (newPoint == null) {
            int ri = Rng.intBetween(0, neighbours.length);
            if (neighbours[ri] != null) {
                newPoint = neighbours[ri];
                previousNeighbour = ri;
            }
        }
        return newPoint;
    }

    private static Point[] getNeightboursPoints(Point point, Pixel[][] grid) {
        Point[] neighbours = new Point[10];
        Point top = new Point(point.x - 1, point.y);
        Point topLeft = new Point(point.x - 1, point.y - 1);
        Point topRight = new Point(point.x - 1, point.y + 1);
        Point bottom = new Point(point.x + 1, point.y);
        Point bottomLeft = new Point(point.x + 1, point.y - 1);
        Point bottomRight = new Point(point.x + 1, point.y + 1);
        Point left = new Point(point.x, point.y - 1);
        Point right = new Point(point.x, point.y + 1);

        assignIfValid(0, top, neighbours, grid);
        assignIfValid(1, bottom, neighbours, grid);
        assignIfValid(2, left, neighbours, grid);
        assignIfValid(3, right, neighbours, grid);
        assignIfValid(4, topLeft, neighbours, grid);
        assignIfValid(5, topRight, neighbours, grid);
        assignIfValid(6, bottomLeft, neighbours, grid);
        assignIfValid(7, bottomRight, neighbours, grid);
        assignIfValid(8, right, neighbours, grid);
        assignIfValid(9, bottomRight, neighbours, grid);

        return neighbours;
    }

    private static void assignIfValid(int index, Point point, Point[] points, Pixel[][] grid) {
        if (isPointWithinGrid(point, grid))
            points[index] = point;
    }

    public static void removePixelsByType(Pixel[][] grid, Pixel.State type) {
        for (int r = 0; r < grid.length; r++)
            for (int c = 0; c < grid[0].length; c++)
                if (grid[r][c].value == type)
                    grid[r][c].value = Pixel.State.EMPTY;
    }

    public static int countPixelsByType(Pixel[][] grid, Pixel.State type) {
        int result = 0;
        for (int r = 0; r < grid.length; r++)
            for (int c = 0; c < grid[0].length; c++)
                if (grid[r][c].value == type)
                    result++;
        return result;
    }


    public static void addPattern(Pixel[][] grid, Pixel.State state) {
        int iterations = Rng.anInt(10);
        int length = Rng.anInt(30);
        int startX = Rng.anInt(grid.length);
        int startY = Rng.anInt(grid[startX].length);
        for (int i = 0; i < iterations; i++)
            drawLine(grid, state, length,
                    -1, i % 2,
                    startY + i * 4, startX + grid[0].length - i);
    }

    private static void arrowHead(Pixel[][] grid, Pixel.State state) {
        int iterations = 60;
        for (int i = 0; i < 15; i++)
            drawLine(grid, state, iterations,
                    -1, 1,
                    i * 4, grid[0].length - i);
    }
    private static void drillHead(Pixel[][] grid, Pixel.State state) {
        int iterations = 60;
        for (int i = 0; i < 15; i++)
            drawLine(grid, state, iterations,
                    0, 0,
                    i * 4, grid[0].length - i);
    }
    private static void squareHead(Pixel[][] grid, Pixel.State state) {
        int iterations = 60;
        for (int i = 0; i < 15; i++)
            drawLine(grid, state, iterations,
                    -1, i % 2,
                    i * 4, grid[0].length - i);
    }

    public static void drawLine(Pixel[][] grid, Pixel.State state, int iterations, int xOrientation, int yOrientation, int xStarter, int yStarter) {
        int x = xStarter;
        int y = yStarter;
        for (int i = 0; i < iterations; i++) {
            if (coordWithinGrid(grid, x, y))
                grid[x][y].value = state;
            // continuing if not valid allows for picking a not valid starting point
            x += xOrientation;
            y += yOrientation;
        }
    }

    public static boolean coordWithinGrid(Pixel[][] grid, int x, int y) {
        return x >= 0 && y >= 0 && x < grid.length && y < grid[x].length;
    }
}
