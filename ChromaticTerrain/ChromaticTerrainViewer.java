package ChromaticTerrain;

import java.awt.*;
import java.awt.event.*;

import java.util.Random;

public class ChromaticTerrainViewer {

	private Frame frame = new Frame("Chromatic Terrain Viewer");
	private ImageCanvas canvas = new ImageCanvas();

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

	public static void main(String[] args) {

		ChromaticTerrainViewer viewer = new ChromaticTerrainViewer();

	}

	ChromaticTerrainViewer() {

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
		frame.setSize(960, 640);
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
					generateNewImage();
					break;
				case 'q':
					// regenerate the map, but only with new colors (same quantity and height values).
					changePalette();
					break;
				case 'e':
					// regenerate the map, only with new colors and heights (new quantity as well).
					changePaletteEntirely();
					break;
				case 's':
					// regenerate the map, but only with a new terrain seed.
					changeSeed();
					break;
				case 'l':
					// regenerate the map, but only with a new lacunarity value.
					changeLacunarity();
					break;
				case 'p':
					// regenerate the map, but only with a new persistence value.
					break;
				case 'o':
					// regenerate the map, but only with a number of octaves.
					break;
				case 'g':
					// display gradient version of the map.
					displayGradient();
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

		generator = new TerrainGenerator(xLength, yLength, highestValue, lowestValue, octaves, persistence, lacunarity, seed, utilizeStretch, palette);
		generator.generateTerrain();
		image = generator.getImage();
		Graphics graphic = canvas.getGraphics();
		graphic.drawImage(image, 0, 0, null);
		canvas.paint(graphic);

	}

	private void changePalette() {

		colorSeed = mainRandom.nextLong();
		palette = getRandomHeightPalette(numberOfColors);

		generator = new TerrainGenerator(xLength, yLength, highestValue, lowestValue, octaves, persistence, lacunarity, seed, utilizeStretch, palette);
		generator.generateTerrain();
		image = generator.getImage();
		Graphics graphic = canvas.getGraphics();
		graphic.drawImage(image, 0, 0, null);
		canvas.paint(graphic);

	}

	private void changePaletteEntirely() {

		colorSeed = mainRandom.nextLong();
		heightSeed = mainRandom.nextLong();
		numberOfColors = mainRandom.nextInt(maxColors);
		palette = getRandomHeightPalette(numberOfColors);

		generator = new TerrainGenerator(xLength, yLength, highestValue, lowestValue, octaves, persistence, lacunarity, seed, utilizeStretch, palette);
		generator.generateTerrain();
		image = generator.getImage();
		Graphics graphic = canvas.getGraphics();
		graphic.drawImage(image, 0, 0, null);
		canvas.paint(graphic);

	}

	private void displayGradient() {

		image = generator.getGradientImage();
		Graphics graphic = canvas.getGraphics();
		graphic.drawImage(image, 0, 0, null);
		canvas.paint(graphic);

	}

	private void changeSeed() {

		seed = mainRandom.nextLong();

		generator = new TerrainGenerator(xLength, yLength, highestValue, lowestValue, octaves, persistence, lacunarity, seed, utilizeStretch, palette);
		generator.generateTerrain();
		image = generator.getImage();
		Graphics graphic = canvas.getGraphics();
		graphic.drawImage(image, 0, 0, null);
		canvas.paint(graphic);

	}

	private void changeLacunarity() {

		lacunarity = mainRandom.nextDouble() * 3.0;

		generator = new TerrainGenerator(xLength, yLength, highestValue, lowestValue, octaves, persistence, lacunarity, seed, utilizeStretch, palette);
		generator.generateTerrain();
		image = generator.getImage();
		Graphics graphic = canvas.getGraphics();
		graphic.drawImage(image, 0, 0, null);
		canvas.paint(graphic);

	}

}