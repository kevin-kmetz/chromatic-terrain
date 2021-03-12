package ChromaticTerrain;

import java.util.ArrayList;
import java.util.Random;

import java.awt.Color;

import java.io.File;
import java.io.IOException;

public class VideoGenerator {
	
	public static void main(String[] args) {

		int desiredVideos = 100;
		String videoName = args[0];

		for (int i = 0; i < desiredVideos; i++) {

			VideoGenerator videoGenerator = new VideoGenerator();

			System.out.println("Now creating video " + (i + 1) + "...");
			videoGenerator.generateVideo(videoName, (i + 1));

		}

	}

	void generateVideo(String videoName, int videoNumber) {

		Random random = new Random();

		//int xLength = 960;
		//int yLength = 640;
		int xLength = 640;
		int yLength = 480;
		int maxHeight = 255;
		int minHeight = 0;

		int octaves = random.nextInt(8) + 1;
		double persistence = random.nextDouble();
		double lacunarity = random.nextDouble() * 3.0;
		boolean utilizeStretch = true;

		long seed = random.nextLong();

		ArrayList<ColorHeightPair> colors = new ArrayList<ColorHeightPair>();

		//double velocity = ((random.nextDouble() - 0.5) / 2.0) * 50;
		double velocity = ((random.nextDouble() / 2) + 0.5) * 50.0;

		if (random.nextBoolean()) {

			velocity *= -1.0;

		}

		int videoLength = 15;
		int framesPerSec = 30;

		double velocityIncrement = velocity / framesPerSec;

		//for (int i = 0, colorNum = random.nextInt(256)+1; i < colorNum; i++) {
		for (int i = 0, colorNum = random.nextInt(64)+1; i < colorNum; i++) {
			
			Color tempColor = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
			double tempHeight = random.nextInt(256) * 1.0;

			colors.add(new ColorHeightPair(tempColor, tempHeight));

		}

		String directoryName = videoName + String.format("%03d", videoNumber);

		try {
			File file = new File(directoryName);
			file.mkdir();
		} catch (Exception e) {
			System.out.println("Error - could not create directory!");
		}

		String fileNamePrefix = "./" + directoryName + "/";

		for (int currentFrame = 0; currentFrame < videoLength * framesPerSec; currentFrame++) {

			System.out.println("Video: " + videoNumber + " Frame: " + (currentFrame + 1));

			HeightPalette palette = new HeightPalette();

			for (ColorHeightPair chp : colors) {

				palette.add(chp.getHeight(), chp.getColor());

			}

			TerrainGenerator generator = new TerrainGenerator(xLength, yLength, maxHeight, minHeight, octaves, persistence, lacunarity, seed, utilizeStretch, palette);
			generator.generateTerrain();
			generator.outputImage(fileNamePrefix + generateImageName(videoNumber, seed, octaves, persistence, lacunarity, currentFrame));

			for (ColorHeightPair chp: colors) {

				chp.setHeight(chp.getHeight() + velocityIncrement);
				chp.normalizeHeight((double) minHeight, (double) maxHeight);

			}

		}

	}

	String generateImageName(int videoNumber, long seed, int octaves, double persistence, double lacunarity, int frameNumber) {

		return new String("AcidMap_v" + videoNumber + "s" + seed + "o" + octaves + "p" + persistence + "l" + lacunarity + "_f" + String.format("%05d", frameNumber + 1) + ".png");

	}

	class ColorHeightPair {

		Color color;
		double height;

		ColorHeightPair(Color color, double height) {

			this.color = new Color(color.getRed(), color.getGreen(), color.getBlue());
			this.height = height;

		}

		Color getColor() {

			return color;

		}

		int getHeight() {

			return (int) Math.floor(height);

		}

		double getTrueHeight() {

			return height;

		}

		double setHeight(double newerHeight) {

			height = newerHeight;

			return height;

		}

		double normalizeHeight(double lowerBound, double upperBound) {

			if (height > upperBound) {
				height -= upperBound;
			} else if (height < 0.0) {
				height += upperBound - 0.0001;
			}

			return height;

		}


	}

}