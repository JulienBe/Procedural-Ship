package ajb.factory;

import java.awt.Point;

import ajb.domain.Parameters;
import ajb.domain.Pixel;
import ajb.utils.PixelGridUtils;

public class ConsoleGeneratorFactory {

	private final int ROWS = 9;
	private final int COLS = 9;

	public Pixel[][] create(Parameters param) {
		Pixel[][] grid = createBaseGrid(Parameters.MINE);

		grid = PixelGridUtils.mirrorCopyGridHorizontally(grid);
		grid = PixelGridUtils.mirrorCopyGridVertically(grid);
		grid = PixelGridUtils.mirrorCopyGridHorizontally(grid);
		grid = PixelGridUtils.addBorders(grid);
		PixelGridUtils.fillEmptySurroundedPixelsInGrid(grid);
		PixelGridUtils.addNoiseToFlatPixels(grid, param);
		PixelGridUtils.setPixelDepth(grid);		

		return validateGrid(grid) ? grid : create(param);
	}

	private boolean validateGrid(Pixel[][] grid) {
		boolean result = true;
		int noOfSecondaryPixels = 0;
		
		for (int x = 0; x < grid.length; x++) {
			for (int y = 0; y < grid[0].length; y++) {
				if (grid[x][y].value == Pixel.State.FILLED) {
					if (grid[x - 1][y].value == Pixel.State.EMPTY ||
							grid[x + 1][y].value == Pixel.State.EMPTY ||
							grid[x][y - 1].value == Pixel.State.EMPTY ||
							grid[x][y + 1].value == Pixel.State.EMPTY) {
						return false;
					}
				} else if (grid[x][y].value == Pixel.State.SECONDARY) {
					noOfSecondaryPixels++;
					
					if (grid[x - 1][y].value == Pixel.State.EMPTY ||
							grid[x + 1][y].value == Pixel.State.EMPTY ||
							grid[x][y - 1].value == Pixel.State.EMPTY ||
							grid[x][y + 1].value == Pixel.State.EMPTY) {
						return false;
					}
				}
			}
		}
		if (noOfSecondaryPixels == 0)
			return false;
		return result;
	}
	
	private Pixel[][] createBaseGrid(Parameters param) {
		Pixel[][] grid = new Pixel[ROWS][COLS];
		PixelGridUtils.initEmptyGrid(grid, ROWS, COLS);
		Point point = new Point(ROWS -1, COLS - 1);

		int steps = 2;
		int subSteps = 10;

		for (int i = 0; i < steps; i++) {
			if (point == null) {
				// down top
				for (int x = ROWS - 1; x > 0; x--) {
					// left to right
					for (int y = 0; y < COLS; y++) {
						if (grid[x][y].value == Pixel.State.FILLED)
							point = new Point(x, y);
					}
				}
			}
			for (int y = 0; y < subSteps; y++)
				point = processPoint(point, grid, param);
			point = null;
		}
		return grid;
	}

	private Point processPoint(Point point, Pixel[][] grid, Parameters param) {
		if (grid[point.x][point.y].value == Pixel.State.EMPTY) {
			grid[point.x][point.y].value = Pixel.State.FILLED;
			grid[point.y][point.x].value = Pixel.State.FILLED;
		}
		return PixelGridUtils.getRandomAdjacentPoint(point, grid, param);
	}
}
