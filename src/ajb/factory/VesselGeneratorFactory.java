package ajb.factory;

import java.awt.Point;

import ajb.domain.Parameters;
import ajb.domain.Pixel;
import ajb.domain.AssetSize;
import ajb.domain.Steps;
import ajb.random.Rng;
import ajb.utils.PixelGridUtils;

public class VesselGeneratorFactory {

	private int rows = 0;
	private int cols = 0;

	public Pixel[][] create(AssetSize size, Parameters parameters, Steps steps) {
		rows = size.row;
        cols = size.col;

		Pixel[][] grid = createBaseGrid(size, steps);
		addExtras(grid, steps);

		grid = PixelGridUtils.removeEmptyCells(grid);
		grid = PixelGridUtils.mirrorCopyGridHorizontally(grid);
		grid = PixelGridUtils.addBorders(grid);
		grid = PixelGridUtils.removeEmptyCells(grid);
		PixelGridUtils.fillEmptySurroundedPixelsInGrid(grid);
		PixelGridUtils.addNoiseToFlatPixels(grid);
		PixelGridUtils.setPixelDepth(grid);

		return validateGrid(grid, parameters) ? grid : create(size, parameters, steps);
	}

	private boolean validateGrid(Pixel[][] grid, Parameters parameters) {
		boolean result = true;

		int noOfFilledPixels = 0;
		int noOfSecondaryPixels = 0;

		for (int x = 0; x < grid.length; x++) {
			for (int y = 0; y < grid[0].length; y++) {
				if (grid[x][y].value == Pixel.State.FILLED)
					noOfFilledPixels++;
				else if (grid[x][y].value == Pixel.State.SECONDARY)
					noOfSecondaryPixels++;
			}
		}
        int nbOfPixels = noOfFilledPixels + noOfSecondaryPixels;
        float colorPercentage = (float)noOfSecondaryPixels / (float)nbOfPixels;

        if (noOfSecondaryPixels == 0)
			return false;
        if (colorPercentage > parameters.colorMaxPercentage || colorPercentage < parameters.colorMinPercentage)
			return false;

        System.out.println(colorPercentage);
		return result;
	}

	private Pixel[][] createBaseGrid(AssetSize size, Steps stepsConst) {
		Pixel[][] grid = new Pixel[rows][cols];
		PixelGridUtils.initEmptyGrid(grid, rows, cols);

		Point point = new Point(rows / 2, cols - 1);

		int steps = Rng.anyRandomIntRange(stepsConst.minSubStep, stepsConst.maxSubSteps);
		int subSteps = Rng.anyRandomIntRange(stepsConst.minSteps, stepsConst.maxSteps);

		for (int i = 0; i < steps; i++) {
			if (point == null) {
				// we are passed the first step lets find the lowest most pixel that is closest to the middle, and go again from there...
				// top down
				for (int x = 0; x < rows; x++) {
					// left to right
					for (int y = 0; y < cols; y++) {
						if (grid[x][y].value == Pixel.State.FILLED)
							point = new Point(x, y);
					}
				}
			}
			for (int y = 0; y < subSteps; y++)
				point = processPoint(point, grid);
			point = null;
		}
		return grid;
	}

	private void addExtras(Pixel[][] grid, Steps stepsConst) {
		int steps = Rng.anyRandomIntRange(stepsConst.minSteps - 10, stepsConst.maxSteps - 10);
		int subSteps = Rng.anyRandomIntRange(stepsConst.minSubStep - 10, stepsConst.maxSubSteps - 10);

		for (int i = 0; i < steps; i++) {
			Point point = PixelGridUtils.getRandomFilledPoint(grid);
			for (int y = 0; y < subSteps; y++)
				point = processPoint(point, grid);
		}
	}

	private Point processPoint(Point point, Pixel[][] grid) {
		if (grid[point.x][point.y].value == Pixel.State.EMPTY)
			grid[point.x][point.y].value = Pixel.State.FILLED;
		return PixelGridUtils.getRandomAdjacentPoint(point, grid);
	}

}
