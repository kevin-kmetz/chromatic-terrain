package ChromaticTerrain;

import java.awt.Color;
import java.util.LinkedList;

public class HeightPalette {

	private LinkedList<Integer> heights = new LinkedList<Integer>();
	private LinkedList<Color> colors = new LinkedList<Color>();

	private Color DEFAULT_COLOR = new Color(Color.MAGENTA.getRGB());

	public HeightPalette() {

		add(0, new Color(DEFAULT_COLOR.getRGB()));

	}

	public void add(int newHeight, Color newColor) {

		if (heights.size() == 0) {

			heights.add(newHeight);
			colors.add(newColor);

		} else if (heights.indexOf(Integer.valueOf(newHeight)) != -1) {
		
			colors.set(heights.indexOf(Integer.valueOf(newHeight)), newColor);

		} else {

			boolean inserted = false;
			for (int i = 0, currentSize = heights.size(); i < currentSize && inserted == false; i++) {

				if (newHeight < heights.get(i)) {

					heights.add(i, newHeight);
					colors.add(i, newColor);

					inserted = true;

				} else if (i == heights.size() - 1 ) {

					heights.add(newHeight);
					colors.add(newColor);

				}

			}

		}

	}

	public void add(int newHeight, int red, int green, int blue) {

		Color color = new Color(red, green, blue);

		add(newHeight, color);

	}

	public Color getColor(int height) {

		Color color = new Color(Color.YELLOW.getRGB());

		boolean colorFound = false;
		for (int i = heights.size()-1; i >= 0 && colorFound == false; i--) {

			if (height >= heights.get(i)) {

				color = colors.get(i);
				colorFound = true;

			}

		}

		return color;

	}

	public void display() {

		System.out.println("Number of entries: " + heights.size());
		System.out.println();

		for (int i = 0; i < heights.size(); i++) {

			int tempHeight = heights.get(i);
			Color tempColor = colors.get(i);

			System.out.print("Index " + i + " -\theight: " + tempHeight);
			System.out.println("\tcolor: " + tempColor.getRed() + ":" + tempColor.getGreen() + ":" + tempColor.getBlue());

		}

		System.out.println();

	}

	public void reset(){

		heights = new LinkedList<Integer>();
		colors = new LinkedList<Color>();

		add(0, new Color(DEFAULT_COLOR.getRGB()));

	}

}