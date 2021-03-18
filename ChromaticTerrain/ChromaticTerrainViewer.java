package ChromaticTerrain;

import java.awt.*;
import java.awt.event.*;

import java.util.Random;

public class ChromaticTerrainViewer {

	private Frame frame = new Frame("Chromatic Terrain Viewer");
	private ImageCanvas canvas = new ImageCanvas();
	private Label statusBar = new Label("Program initialized.", Label.LEFT);

	private Random mainRandom = new Random();
	private Random colorRandom;
	private Random heightRandom;

	private long colorSeed;
	private long heightSeed;

	private final int xLength = 960;
	private final int yLength = 640;
	private final int highestValue = 255;
	private final int lowestValue = 0;
	private final boolean utilizeStretch = true;
	private final int maxColors = 255;

	private int octaves;
	private double persistence;
	private double lacunarity;
	private long seed;
	private int numberOfColors;

	private TerrainGenerator generator;
	private HeightPalette palette;
	private Image image;
	private Gradient gradient = new Gradient(0, 0, 0, 255, 255, 0);

	private int imageCount = 0;
	private String fileNamePrefix;
	private boolean gradientModeEnabled = false;

	public static void main(String[] args) {

		ChromaticTerrainViewer viewer = new ChromaticTerrainViewer();

	}

	ChromaticTerrainViewer() {

		fileNamePrefix = String.valueOf((char)(mainRandom.nextInt(25)+97)) + String.valueOf((char)(mainRandom.nextInt(25)+97)) + String.valueOf((char)(mainRandom.nextInt(25)+97)) + String.valueOf('_');

		heightSeed = mainRandom.nextLong();
		colorSeed = mainRandom.nextLong();
		numberOfColors = mainRandom.nextInt(maxColors + 1);

		octaves = mainRandom.nextInt(8) + 1;
		persistence = mainRandom.nextDouble();
		lacunarity = mainRandom.nextDouble() * 3.0;
		seed = mainRandom.nextLong();
		palette = getRandomHeightPalette(numberOfColors);

		generator = new TerrainGenerator(xLength, yLength, highestValue, lowestValue, octaves, persistence, lacunarity, seed, utilizeStretch, palette);
		generator.generateTerrain();

		frame.setLayout(null);		// use absolute layout
		frame.setSize(960, 666);
		frame.setResizable(false);

		// Enable the window to be closed by 'x'ing out.
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				frame.dispose();
			}
		});

		canvas.setSize(960, 640);
		frame.add(canvas);
		frame.addKeyListener(new KeyListener());

		statusBar.setBounds(7, 642, 953, 14);
		frame.add(statusBar);

		frame.setVisible(true);

		image = generator.getImage();
		canvas.getGraphics().drawImage(image, 0, 0, null);

	}

	HeightPalette getRandomHeightPalette() {

		return getRandomHeightPalette(mainRandom.nextInt(256));

	}

	HeightPalette getRandomHeightPalette(int numberOfColors) {

		colorRandom = new Random(colorSeed);
		heightRandom = new Random(heightSeed);

		HeightPalette tempPalette = new HeightPalette();
		Color color;

		for (int i = 0; i < numberOfColors; i++) {

			color = new Color(colorRandom.nextInt(256), colorRandom.nextInt(256), colorRandom.nextInt(256));
			tempPalette.add(heightRandom.nextInt(256), color);

		}

		return tempPalette;

	}

	class ImageCanvas extends Canvas {

		public void paint(Graphics graphic) {


		}

	}

	class KeyListener extends KeyAdapter {

		public void keyPressed(KeyEvent event) {

			char keyPressCharacter = event.getKeyChar();

			switch (keyPressCharacter) {
				case 'n':
					// generate an entirely new image.
					//updateStatusBar("Generating new image...");
					generateNewImage();
					updateStatusBar("New image generated.");
					break;
				case 'q':
					// regenerate the map, but only with new colors (same quantity and height values).
					if (gradientModeEnabled == false) {

						changePalette();
						updateStatusBar("New image palette generated; number of colors and height values preserved.");

					} else if (gradientModeEnabled == true) {

						changeGradientPalette();
						updateStatusBar("New gradient generated.");

					}

					break;
				case 'e':
					// regenerate the map, only with new colors and heights (new quantity as well).
					changePaletteEntirely();
					updateStatusBar("New image palette generated; new number of colors and new height values generated.");
					break;
				case 's':
					// regenerate the map, but only with a new terrain seed.
					changeSeed();
					updateStatusBar("Image regenerated with new random seed. Seed: " + seed);
					break;
				case 'l':
					// regenerate the map, but only with a new lacunarity value.
					changeLacunarity();
					updateStatusBar("Image regenerated with new random lacunarity value. Lacunarity: " + lacunarity);
					break;
				case 'p':
					// regenerate the map, but only with a new persistence value.
					changePersistence();
					updateStatusBar("Image regenerated with new random persistence value. Persistence: " + persistence);
					break;
				case 'o':
					// regenerate the map, but only with a number of octaves.
					changeOctaves();
					updateStatusBar("Image regenerated with new number of octaves. Number of octaves: " + octaves);
					break;
				case 'g':
					// display gradient version of the map.
					displayGradient();
					break;
				case 'z':
					// save the current image to the hard drive.
					outputImage();
					break;
				case 'r':
					// reset the gradient to high contrast black and yellow.
					resetGradient();
					updateStatusBar("Gradient reset to original high-contrast values.");
					break;
				case 'h':
					// open a help window.
					openHelpWindow();
					break;
			}


		}

	}

	private void generateNewImage() {

		heightSeed = mainRandom.nextLong();
		colorSeed = mainRandom.nextLong();
		numberOfColors = mainRandom.nextInt(maxColors + 1);

		octaves = mainRandom.nextInt(8) + 1;
		persistence = mainRandom.nextDouble();
		lacunarity = mainRandom.nextDouble() * 3.0;
		seed = mainRandom.nextLong();
		palette = getRandomHeightPalette();

		generate();

	}

	private void changePalette() {

		colorSeed = mainRandom.nextLong();
		palette = getRandomHeightPalette(numberOfColors);

		generate();

	}

	private void changePaletteEntirely() {

		colorSeed = mainRandom.nextLong();
		heightSeed = mainRandom.nextLong();
		numberOfColors = mainRandom.nextInt(maxColors);
		palette = getRandomHeightPalette(numberOfColors);

		generate();

	}

	private void displayGradient() {

		if (gradientModeEnabled == false) {

			gradientModeEnabled = true;

			image = generator.getGradientImage();
			updateStatusBar("Gradient mode enabled.");

		} else if (gradientModeEnabled == true) {

			gradientModeEnabled = false;

			image = generator.getImage();
			updateStatusBar("Gradient mode disabled.");

		}

		Graphics graphic = canvas.getGraphics();
		graphic.drawImage(image, 0, 0, null);
		canvas.paint(graphic);

	}

	private void changeSeed() {

		seed = mainRandom.nextLong();

		generate();

	}

	private void changeLacunarity() {

		lacunarity = mainRandom.nextDouble() * 3.0;

		generate();

	}

	private void changeOctaves() {

		octaves = mainRandom.nextInt(8) + 1;

		generate();

	}

	private void changePersistence() {

		persistence = mainRandom.nextDouble();

		generate();

	}

	private void generate() {

		updateStatusBar("Generating...");

		generator = new TerrainGenerator(xLength, yLength, highestValue, lowestValue, octaves, persistence, lacunarity, seed, utilizeStretch, palette);
		generator.generateTerrain();
		generator.setGradient(gradient);

		if (gradientModeEnabled == true) {

			image = generator.getGradientImage();

		} else if (gradientModeEnabled == false) {

			image = generator.getImage();

		}

		Graphics graphic = canvas.getGraphics();
		graphic.drawImage(image, 0, 0, null);
		canvas.paint(graphic);

		updateStatusBar("Done!");

	}

	private void outputImage() {

		String fileName = fileNamePrefix + String.format("%03d", imageCount) + ".png";

		if (gradientModeEnabled == true) {

			generator.outputGradientImage(fileName);
			updateStatusBar("Current gradient saved to hard drive as \'" + fileName + "\' in the working directory.");


		} else if (gradientModeEnabled == false) {

			generator.outputImage(fileName);
			updateStatusBar("Current image saved to hard drive as \'" + fileName + "\' in the working directory.");

		}

		imageCount++;

	}

	private void changeGradientPalette() {

		Color colorOne = new Color(mainRandom.nextInt(256), mainRandom.nextInt(256), mainRandom.nextInt(256));
		Color colorTwo = new Color(mainRandom.nextInt(256), mainRandom.nextInt(256), mainRandom.nextInt(256));
		gradient = new Gradient(colorOne, colorTwo);

		generate();

	}

	private void resetGradient() {

		gradient = new Gradient(0, 0, 0, 255, 255, 0);

		generate();

	}

	private void updateStatusBar(String update) {

		statusBar.setText(update);

	}

	private void openHelpWindow() {

		Frame helpWindow = new Frame("Chromatic Terrain Help");

		helpWindow.setLayout(null);
		helpWindow.setSize(300, 400);
		helpWindow.setResizable(false);

		/*helpWindow.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				helpWindow.dispose();
			}
		});*/

		helpWindow.setVisible(true);

	}

}