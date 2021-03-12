package ChromaticTerrain;

import java.awt.*;
import java.awt.event.*;

import java.util.Random;

public class ChromaticTerrainViewer {

	private Frame frame = new Frame("Chromatic Terrain Viewer");
	private ImageCanvas canvas = new ImageCanvas();

	private Random random = new Random();

	private final int xLength = 960;
	private final int yLength = 640;
	private final int highestValue = 255;
	private final int lowestValue = 0;
	private final boolean utilizeStretch = true;

	private int octaves;
	private double persistence;
	private double lacunarity;
	private long seed;


	private TerrainGenerator generator;
	private HeightPalette palette;
	private Image image;

	public static void main(String[] args) {

		ChromaticTerrainViewer viewer = new ChromaticTerrainViewer();

	}

	ChromaticTerrainViewer() {

		octaves = random.nextInt(8) + 1;
		persistence = random.nextDouble();
		lacunarity = random.nextDouble() * 3.0;
		seed = random.nextLong();
		palette = getRandomHeightPalette();

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

		return getRandomHeightPalette(random.nextInt(256));

	}

	HeightPalette getRandomHeightPalette(int numberOfColors) {

		HeightPalette palette = new HeightPalette();
		Color color;

		for (int i = 0; i < numberOfColors; i++) {

			color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
			palette.add(random.nextInt(256), color);

		}

		return palette;

	}

	class ImageCanvas extends Canvas {

		public void paint(Graphics graphic) {


		}

	}

	class KeyListener extends KeyAdapter {

		public void keyPressed(KeyEvent event) {


		}

	}

}