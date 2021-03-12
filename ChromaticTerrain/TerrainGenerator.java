package ChromaticTerrain;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class TerrainGenerator {

	private HeightPalette palette;
	private NoiseGenerator generator;
	private Raster heightRaster;

	private Gradient gradient;

	private int xLength;
	private int yLength;

	private int lowestValue;
	private int highestValue;
	private int octaves;
	private double persistence;
	private double lacunarity;
	private long seed;
	private boolean utilizeStretch;

	private int xPosition;
	private int yPosition;
	private double scale;

	private static final int DEFAULT_X_LENGTH = 960;
	private static final int DEFAULT_Y_LENGTH = 640;

	private static final int DEFAULT_LOWEST_VALUE = 0;
	private static final int DEFAULT_HIGHEST_VALUE = 255;
	private static final int DEFAULT_OCTAVES = 5;
	private static final double DEFAULT_PERSISTENCE = 0.5;
	private static final double DEFAULT_LACUNARITY = 2.0;
	private static final long DEFAULT_SEED = 0L;
	private static final boolean DEFAULT_UTILIZE_STRETCH = true;

	private static final int DEFAULT_X_POSITION = 0;
	private static final int DEFAULT_Y_POSITION = 0;
	private static final double DEFAULT_SCALE = 100.0;

	public TerrainGenerator() {

		xLength = DEFAULT_X_LENGTH;
		yLength = DEFAULT_Y_LENGTH;

		highestValue = DEFAULT_HIGHEST_VALUE;
		lowestValue = DEFAULT_LOWEST_VALUE;
		octaves = DEFAULT_OCTAVES;
		persistence = DEFAULT_PERSISTENCE;
		lacunarity = DEFAULT_LACUNARITY;
		seed = DEFAULT_SEED;
		utilizeStretch = DEFAULT_UTILIZE_STRETCH;

		xPosition = DEFAULT_X_POSITION;
		yPosition = DEFAULT_Y_POSITION;
		scale = DEFAULT_SCALE;

		palette = new HeightPalette();
		palette.add(0, 15, 0, 80);			// deep ocean
		palette.add(45, 35, 0, 190);		// ocean
		palette.add(230, 255, 255, 255);	// snow top
		palette.add(90, 255, 230, 175);		// sand
		palette.add(115, 85, 220, 75);		// grass
		palette.add(155, 60, 150, 50);		// elevated grass
		palette.add(185, 75, 50, 20);		// mountain

		generator = new NoiseGenerator(highestValue, lowestValue, octaves, persistence, lacunarity, seed, utilizeStretch);

		heightRaster = new Raster(xLength, yLength);

		gradient = new Gradient(0, 0, 0, 255, 255, 0);

	}

	public TerrainGenerator(int xLength, int yLength, int highestValue, int lowestValue, int octaves, double persistence, double lacunarity, long seed, boolean utilizeStretch, HeightPalette palette) {

		this.xLength = xLength;
		this.yLength = yLength;

		this.highestValue = highestValue;
		this.lowestValue = lowestValue;
		this.octaves = octaves;
		this.persistence = persistence;
		this.lacunarity = lacunarity;
		this.seed = seed;
		this.utilizeStretch = utilizeStretch;

		this.palette = palette;

		xPosition = DEFAULT_X_POSITION;
		yPosition = DEFAULT_X_POSITION;
		scale = DEFAULT_SCALE;

		generator = new NoiseGenerator(highestValue, lowestValue, octaves, persistence, lacunarity, seed, utilizeStretch);

		heightRaster = new Raster(xLength, yLength);

		gradient = new Gradient(0, 0, 0, 255, 255, 0);

	}

	public void generateTerrain() {

		for (int yRaster = 0, yPos = yPosition; yPos < yLength + yPosition; yRaster++, yPos++) {

			for (int xRaster = 0, xPos = xPosition; xPos < xLength + xPosition; xRaster++, xPos++) {

				heightRaster.set(xRaster, yRaster, (int) generator.getNoise( (double) xPos/scale, (double) yPos/scale));

			}

		}

	}

	public void outputImage(String fileName) {

		BufferedImage mapImage = new BufferedImage(xLength, yLength, BufferedImage.TYPE_INT_RGB);

		for (int y = 0; y < yLength; y++) {

			for (int x = 0; x < xLength; x++) {

				Color tempColor = palette.getColor(heightRaster.get(x, y));
				mapImage.setRGB(x, y, tempColor.getRGB());

			}

		}

		try {

				File outputFile = new File(fileName);
				outputFile.createNewFile();

				ImageIO.write(mapImage, "png", outputFile);

			} catch (Exception e) {

				System.out.println("Error - unable to output image!");

			}

	}

	public void outputImage() {

		outputImage("TerrainMap_s" + seed + "x" + xPosition + "y" + yPosition + "o" + octaves + "p" + persistence + "l" + lacunarity + ".png");

	}

	public BufferedImage getImage() {

		BufferedImage mapImage = new BufferedImage(xLength, yLength, BufferedImage.TYPE_INT_RGB);

		for (int y = 0; y < yLength; y++) {

			for (int x = 0; x < xLength; x++) {

				Color tempColor = palette.getColor(heightRaster.get(x, y));
				mapImage.setRGB(x, y, tempColor.getRGB());

			}

		}

		return mapImage;

	}

	public BufferedImage getGradientImage() {

		BufferedImage mapImage = new BufferedImage(xLength, yLength, BufferedImage.TYPE_INT_RGB);

		for (int y = 0; y < yLength; y++) {

			for (int x = 0; x < xLength; x++) {

				Color tempColor = gradient.getColor(heightRaster.get(x, y));
				mapImage.setRGB(x, y, tempColor.getRGB());

			}

		}

		return mapImage;

	}

	public void outputGradientImage(String fileName) {

		BufferedImage mapImage = new BufferedImage(xLength, yLength, BufferedImage.TYPE_INT_RGB);

		for (int y = 0; y < yLength; y++) {

			for (int x = 0; x < xLength; x++) {

				Color tempColor = gradient.getColor(heightRaster.get(x, y));
				mapImage.setRGB(x, y, tempColor.getRGB());

			}

		}

		try {

				File outputFile = new File(fileName);
				outputFile.createNewFile();

				ImageIO.write(mapImage, "png", outputFile);

			} catch (Exception e) {

				System.out.println("Error - unable to output image!");

			}

	}

	public void outputGradientImage() {

		outputGradientImage("TerrainMap_s" + seed + "x" + xPosition + "y" + yPosition + "o" + octaves + "p" + persistence + "l" + lacunarity + "_Gr.png");

	}

	public void setGradient(Gradient gradient) {

		this.gradient = gradient;

	}

	public void setGeneratorToDefaults(int firstRed, int firstGreen, int firstBlue, int secondRed, int secondGreen, int secondBlue) {

		Gradient tempGradient = new Gradient(firstRed, firstGreen, firstBlue, secondRed, secondGreen, secondBlue);
		setGradient(tempGradient);

	}

	public void setPosition(int x, int y) {

		xPosition = x;
		yPosition = y;

	}

}