package ajb.examples;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import ajb.domain.Parameters;
import ajb.domain.Pixel;
import ajb.domain.AssetSize;
import ajb.domain.Steps;
import ajb.factory.VesselGeneratorFactory;
import ajb.utils.ImageUtils;

import javax.swing.*;

public class CreateImageFullOfVessels {

	private static final Parameters PARAM = Parameters.MINE;

	public static void main(String[] args) {
		// get an instance of our vessel factory
		VesselGeneratorFactory factory = new VesselGeneratorFactory();

//		for (AssetSize size : AssetSize.values()) {
        AssetSize size = AssetSize.MEDIUM;
			// list to hold all the vessels we generate
			List<Pixel[][]> grids = new ArrayList<Pixel[][]>();
	
			// create X vessels and add to our list
			for (int i = 0; i < 100; i++) {
				Pixel[][] grid = factory.create(size, PARAM, Steps.large);
				// add grid to list
				grids.add(grid);
			}
	
			Color primaryColor = Color.decode("#2A2A2A");
	
			// create image
			BufferedImage img = ImageUtils.outputAllToImage(grids, 1000, 1000, primaryColor, null);
	
			// save image
			// replace with the path of wherever you want the image to go - if left
			// as is it will be in the root project folder
			ImageUtils.save(img, "png", "1vessels_" + size);

			JFrame frame = new JFrame();
			frame.getContentPane().setLayout(new FlowLayout());
			frame.getContentPane().add(new JLabel(new ImageIcon(img)));
			frame.pack();
			frame.setVisible(true);
//		}
		System.out.println("================");
		System.out.println("Time create" + factory.timeCreate);
		System.out.println("Time noise" + factory.timeNoise);
		System.out.println("Time remove empty 1" + factory.timeRemoveEmpty1);
		System.out.println("Time remove empty 2" + factory.timeRemoveEmpty2);
		System.out.println("Time mirror" + factory.timeMirror);
		System.out.println("Time depth" + factory.timeDepth);
		System.out.println("Time add borders" + factory.timeAddBorders);
		System.out.println("Time fill empty" + factory.timeFillEmpty);
	}

}
