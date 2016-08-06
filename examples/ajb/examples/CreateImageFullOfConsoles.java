package ajb.examples;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import ajb.domain.Parameters;
import ajb.domain.Pixel;
import ajb.factory.ConsoleGeneratorFactory;
import ajb.utils.ImageUtils;

public class CreateImageFullOfConsoles {

	public static void main(String[] args) {

		// get an instance of our factory
		ConsoleGeneratorFactory factory = new ConsoleGeneratorFactory();

		// list to hold all the console we generate
		List<Pixel[][]> grids = new ArrayList<Pixel[][]>();

		// create X console and add to our list
		for (int i = 0; i < 240; i++) {

			Pixel[][] grid = factory.create(Parameters.MINE);

			// add grid to list
			grids.add(grid);
		}

        //g.setColor(Color.decode("#FFC600"));
        //int identifierWidth = g.getFontMetrics().stringWidth(hex.getIdentifier());
        //int identifierHeight = g.getFontMetrics().getHeight();
        //g.drawString(hex.getIdentifier(), (int) hex.getMiddlePoint().getX() - (identifierWidth / 2), (int) hex.getMiddlePoint().getY() + (identifierHeight / 2));						
		
		Color primaryColor = Color.decode("#2A2A2A");
		
		// create image
		BufferedImage img = ImageUtils.outputAllToImage(grids, 800, 800, primaryColor, null);

		// save image
		// replace with the path of wherever you want the image to go - if left
		// as is it will be in the root project folder
		ImageUtils.save(img, "png", "consoles");
	}

}
