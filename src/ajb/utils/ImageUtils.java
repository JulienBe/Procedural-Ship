package ajb.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import ajb.domain.Pixel;

public class ImageUtils {

	public static BufferedImage outputToImage(Pixel[][] grid, Color primaryColor, Color secondaryColor) {
		if (primaryColor == null)
			primaryColor = Color.decode(ColorUtils.getRandomColour());			
		if (secondaryColor == null)
			secondaryColor = Color.decode(ColorUtils.getRandomColour());

		BufferedImage layer1Img = createImage(grid, primaryColor, secondaryColor, true);
		BufferedImage result = blend(volumeAndLightLayer(grid, primaryColor, secondaryColor), layer1Img);

		return result;
	}

    private static BufferedImage volumeAndLightLayer(Pixel[][] grid, Color primaryColor, Color secondaryColor) {
		GaussianFilter filter = new GaussianFilter();
		filter.setRadius(12f);
		return filter.filter(createImage(grid, primaryColor, secondaryColor, true), null);
    }
	
	public static BufferedImage createImage(Pixel[][] grid, Color primaryColor, Color secondaryColor, boolean noise) {
		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		int scaleFactor = 2;

		BufferedImage img = gc.createCompatibleImage(grid[0].length * scaleFactor, grid.length * scaleFactor, BufferedImage.TYPE_INT_ARGB);

		Graphics2D gr = (Graphics2D) img.getGraphics();

		gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// ho... I think that he has inverted x and y here... Really need a grid abstraction
		for (int x = 0; x < grid.length; x++) {
			for (int y = 0; y < grid[0].length; y++) {
				switch (grid[x][y].value) {
					case BORDER:
						gr.setColor(Color.BLACK);
						gr.fillRect(y * scaleFactor, x * scaleFactor, scaleFactor, scaleFactor);
						break;
					case EMPTY:
						break;
					case FILLED:
						gr.setColor(ColorUtils.lighter(primaryColor, grid[x][y].depth * 0.05 > 3 ? 3 : grid[x][y].depth * 0.05));
						gr.fillRect(y * scaleFactor, x * scaleFactor, scaleFactor, scaleFactor);
						break;
					case FILL_STRUCTURE:
					    if (noise) {
                            gr.setColor(ColorUtils.lighter(secondaryColor, grid[x][y].depth * 0.15f));
                            gr.fillRect(y * scaleFactor, x * scaleFactor, scaleFactor, scaleFactor);
                        }
						break;
					case SECONDARY:
						gr.setColor(ColorUtils.lighter(secondaryColor, grid[x][y].depth * 0.05));
						gr.fillRect(y * scaleFactor, x * scaleFactor, scaleFactor, scaleFactor);
						break;
				}
			}
		}
		gr.dispose();
		return img;
	}

	public static BufferedImage outputAllToImage(List<Pixel[][]> grids, int width, int height, Color primaryColor, Color secondaryColor) {
		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();

		BufferedImage img = gc.createCompatibleImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D gr = (Graphics2D) img.getGraphics();
		gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		// Fill background
		gr.setColor(Color.decode("#1E1E1E"));
		gr.fillRect(0, 0, width, height);
		
		int x = 10;
		int y = 10;
		int maxYForLine = 0;

		for (Pixel[][] grid : grids) {
			BufferedImage vesselImg = outputToImage(grid, primaryColor, secondaryColor);
			if (x + (vesselImg.getWidth() + 10) > width) {
				x = 10;
				y += maxYForLine + 10;
				maxYForLine = 0;
			}
			if (y + (vesselImg.getHeight() + 10) > height)
				continue;
			gr.drawImage(vesselImg, x, y, null);
			x += vesselImg.getWidth() + 10;

			if (vesselImg.getHeight() > maxYForLine)
				maxYForLine = vesselImg.getHeight();
		}
		gr.dispose();
		return img;
	}

	public static void save(BufferedImage image, String ext, String fileName) {

		File file = new File(fileName + "." + ext);

		try {
			ImageIO.write(image, ext, file);
		} catch (IOException e) {
			System.out.println("Write error for " + file.getPath() + ": " + e.getMessage());
		}
	}

	/**
	 * Blend the contents of two BufferedImages according to a specified weight.
	 * 
	 * @return new BufferedImage containing blended contents of BufferedImage arguments
	 */
	public static BufferedImage blend(BufferedImage bi1, BufferedImage bi2) {
		if (bi1 == null)
			throw new NullPointerException("bi1 is null");

		if (bi2 == null)
			throw new NullPointerException("bi2 is null");

		int width = bi1.getWidth();
		if (width != bi2.getWidth())
			throw new IllegalArgumentException("widths not equal");

		int height = bi1.getHeight();
		if (height != bi2.getHeight())
			throw new IllegalArgumentException("heights not equal");

		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D gr = (Graphics2D) img.getGraphics();

		gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Composite oldcomp = gr.getComposite();
		// draw first image fully opaque
		gr.drawImage(bi1, 0, 0, null);
		// change opacity of second image after each call to repaint()
		gr.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
		gr.drawImage(bi2, 0, 0, null);
		gr.setComposite(oldcomp);

		gr.dispose();

		return img;
	}
}
