package ajb.factory;

import java.awt.Point;

import ajb.domain.Parameters;
import ajb.domain.Pixel;
import ajb.domain.AssetSize;
import ajb.random.Rng;
import ajb.utils.PixelGridUtils;

public class StationGeneratorFactory {

	private int rows = 0;
	private int cols = 0;

	public Pixel[][] create(AssetSize size) {

		if (size.equals(AssetSize.RANDOM)) {
			rows = 300;
			cols = 300;
		} else if (size.equals(AssetSize.SMALL)) {
			rows = 100;
			cols = 100;
		} else if (size.equals(AssetSize.MEDIUM)) {
			rows = 200;
			cols = 200;
		} else if (size.equals(AssetSize.LARGE)) {
			rows = 300;
			cols = 300;
		}		
		
		Pixel[][] grid = createBaseGrid(size, Parameters.MINE);

		grid = PixelGridUtils.removeEmptyCells(grid);
		grid = PixelGridUtils.mirrorCopyGridHorizontally(grid);
		grid = PixelGridUtils.mirrorCopyGridVertically(grid);
		grid = PixelGridUtils.addBorders(grid);
		grid = PixelGridUtils.removeEmptyCells(grid);
		PixelGridUtils.fillEmptySurroundedPixelsInGrid(grid);
		PixelGridUtils.addNoiseToFlatPixels(grid, Parameters.MINE);
		PixelGridUtils.setPixelDepth(grid);		

		if (validateGrid(grid)) {		
			return grid;
		} else {
			return create(size);
		}
	}

	private boolean validateGrid(Pixel[][] grid) {
		
		boolean result = true;
		
		int noOfFilledPixels = 0;
		int noOfSecondaryPixels = 0;
		
		for (int x = 0; x < grid.length; x++) {
			for (int y = 0; y < grid[0].length; y++) {
				if (grid[x][y].value == Pixel.State.FILLED) {
					noOfFilledPixels++;
				} else if (grid[x][y].value == Pixel.State.SECONDARY) {
					noOfSecondaryPixels++;
				}
			}
		}

		if (noOfSecondaryPixels == 0) {
			return false;
		}
		
		if (noOfSecondaryPixels > (noOfFilledPixels / 4)) {
			result = false;
		}		
		
		return result;
	}
	
	private Pixel[][] createBaseGrid(AssetSize size, Parameters param) {
		Pixel[][] grid = new Pixel[rows][cols];
		PixelGridUtils.initEmptyGrid(grid, rows, cols);

		Point point = new Point(rows -1, cols - 1);

		int steps = Rng.intBetween(calculateMinNoOfSteps(size), calculateMaxNoOfSteps(size));
		int subSteps = Rng.intBetween(calculateMinNoOfSubSteps(size), calculateMaxNoOfSubSteps(size));

		for (int i = 0; i < steps; i++) {

			if (point == null) {
				// down top
				for (int x = rows - 1; x > 0; x--) {
					// left to right
					for (int y = 0; y < cols; y++) {
						if (grid[x][y].value == Pixel.State.FILLED) {
							point = new Point(x, y);
						}
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
	
	private int calculateMinNoOfSteps(AssetSize size) {

		int result = 0;

		if (size.equals(AssetSize.RANDOM)) {
			result = 5;
		} else if (size.equals(AssetSize.SMALL)) {
			result = 5;
		} else if (size.equals(AssetSize.MEDIUM)) {
			result = 10;
		} else if (size.equals(AssetSize.LARGE)) {
			result = 20;
		}

		return result;
	}

	private int calculateMaxNoOfSteps(AssetSize size) {

		int result = 0;

		if (size.equals(AssetSize.RANDOM)) {
			result = 50;
		} else if (size.equals(AssetSize.SMALL)) {
			result = 15;
		} else if (size.equals(AssetSize.MEDIUM)) {
			result = 30;
		} else if (size.equals(AssetSize.LARGE)) {
			result = 50;
		}

		return result;
	}

	private int calculateMinNoOfSubSteps(AssetSize size) {

		int result = 1;

		if (size.equals(AssetSize.RANDOM)) {
			result = 5;
		} else if (size.equals(AssetSize.SMALL)) {
			result = 5;
		} else if (size.equals(AssetSize.MEDIUM)) {
			result = 10;
		} else if (size.equals(AssetSize.LARGE)) {
			result = 15;
		}

		return result;
	}

	private int calculateMaxNoOfSubSteps(AssetSize size) {

		int result = 0;

		if (size.equals(AssetSize.RANDOM)) {
			result = 50;
		} else if (size.equals(AssetSize.SMALL)) {
			result = 30;
		} else if (size.equals(AssetSize.MEDIUM)) {
			result = 40;
		} else if (size.equals(AssetSize.LARGE)) {
			result = 50;
		}

		return result;
	}	
}
