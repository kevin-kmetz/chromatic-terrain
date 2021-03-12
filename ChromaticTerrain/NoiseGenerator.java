package ChromaticTerrain;

import java.lang.Math;
import java.util.Random;

public class NoiseGenerator {

	OpenSimplexNoise openSimplexNoise;
	long seed;

	int octaves;
	double persistence;
	double lacunarity;
	boolean utilizeStretch;
	final double stretchSamples = 1000;			// Real samples taken are the square of this variable!
	boolean stretchAlreadyCalculated = false;
	double stretchCoefficient = 1.0;
	double stretchTranslationInner = 0.0;
	double stretchTranslationOuter = 0.0;
	boolean stretchNoiseCalculated = false;

	int highestValue;
	int lowestValue;
	int spread;
	int offset;

	public NoiseGenerator() {
		
		this(0, 255, 5, 0.5, 2.0, 0L, false);

	}

	public NoiseGenerator(int highestValue, int lowestValue, int octaves, double persistence, double lacunarity, long seed, boolean utilizeStretch) {

		this.highestValue = highestValue;
		this.lowestValue = lowestValue;
		this.octaves = octaves;
		this.persistence = persistence;
		this.lacunarity = lacunarity;
		this.seed = seed;
		this.utilizeStretch = utilizeStretch;

		spread = highestValue - lowestValue;
		offset = 0 - lowestValue;

		openSimplexNoise = new OpenSimplexNoise(seed);

		if (utilizeStretch == true) {
			calculateStretchFactors();
		}

	}

	public double getRawNoise(double x, double y) {

		double totalNoise = 0;
		double currentFrequency = 1;
		double currentAmplitude = 1;

		double summedAmplitudes = 0;

		for (int i = 0; i < octaves; i++) {

			totalNoise += openSimplexNoise.eval(x*currentFrequency, y*currentFrequency) * currentAmplitude;
			summedAmplitudes += currentAmplitude;

			currentAmplitude *= persistence;
			currentFrequency *= lacunarity;

		}

		double outputNoise = totalNoise/summedAmplitudes;

		if (stretchAlreadyCalculated == true) {
			outputNoise = stretchTranslationOuter + stretchCoefficient*(outputNoise - stretchTranslationInner);
		}

		return outputNoise;

	}

	public double getNormalizedNoise(double x, double y) {

		double rawNoise = getRawNoise(x, y);

		double normalizedNoise = (rawNoise + 1.0) / 2.0;

		return normalizedNoise;

	}

	public double getNoise(double x, double y) {

		double normalizedNoise = getNormalizedNoise(x, y);

		double adjustedNoise = (normalizedNoise * (double) spread) - (double) offset;

		return adjustedNoise;

	}

	private void calculateStretchFactors() {

		double lowestValue = 0.0;
		double highestValue = 0.0;

		for (double y = 0.0; y < stretchSamples; y++) {

			for (double x = 0.0; x < stretchSamples; x++) {

				double currentNoise = getRawNoise(x/100, y/100);

				if (currentNoise < lowestValue) {
					lowestValue = currentNoise;
				}

				if (currentNoise > highestValue) {
					highestValue = currentNoise;
				}

			}

		}

		double desiredRangeMin = -1.0;
		double desiredRangeMax = 1.0;

		stretchCoefficient = (desiredRangeMax - desiredRangeMin) / (highestValue - lowestValue);
		stretchTranslationInner = lowestValue;
		stretchTranslationOuter = desiredRangeMin;

		stretchAlreadyCalculated = true;

		/*System.out.println("Stretch coefficient: " + stretchCoefficient);
		System.out.println("Stretch translation inner: " + stretchTranslationInner);
		System.out.println("Stretch translation outer: " + stretchTranslationOuter);
		System.out.println();*/


	}

}