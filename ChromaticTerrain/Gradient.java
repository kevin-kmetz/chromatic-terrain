package ChromaticTerrain;

import java.awt.Color;

public class Gradient {

	private Color firstColor;
	private Color secondColor;

	private double gradientSlopeRed = 1.0;
	private double gradientInterceptRed = 0.0;
	private double gradientSlopeGreen = 1.0;
	private double gradientInterceptGreen = 0.0;
	private double gradientSlopeBlue = 1.0;
	private double gradientInterceptBlue = 0.0;

	public Gradient(int firstRed, int firstGreen, int firstBlue, int secondRed, int secondGreen, int secondBlue) {

		this(new Color(firstRed, firstGreen, firstBlue), new Color(secondRed, secondGreen, secondBlue));

	}

	public Gradient(Color firstColor, Color secondColor) {

		this.firstColor = new Color(firstColor.getRGB());
		this.secondColor = new Color(secondColor.getRGB());

		calculateGradientVariables();

	}

	void calculateGradientVariables() {

		double[] redSlopeIntercept = getSlopeIntercept(firstColor.getRed(), secondColor.getRed());
		gradientSlopeRed = redSlopeIntercept[0];
		gradientInterceptRed = redSlopeIntercept[1];

		double[] greenSlopeIntercept = getSlopeIntercept(firstColor.getGreen(), secondColor.getGreen());
		gradientSlopeGreen = greenSlopeIntercept[0];
		gradientInterceptGreen = greenSlopeIntercept[1];

		double[] blueSlopeIntercept = getSlopeIntercept(firstColor.getBlue(), secondColor.getBlue());
		gradientSlopeBlue = blueSlopeIntercept[0];
		gradientInterceptBlue = blueSlopeIntercept[1];

	}

	private double[] getSlopeIntercept(int newStartingValue, int newEndingValue) {

		// [a, b] -> [c, d]
		// y = (x - a)( (d - c)/(b - a) ) + c
		// y = x*blob - a*bloc + c
		// y = Px + Q

		double newSV = newStartingValue;
		double newEV = newEndingValue;

		double grayScaleStart = 0.0;
		double grayScaleEnd = 255.0;

		double determinant = (newEV - newSV)/(grayScaleEnd - grayScaleStart);

		double slope = determinant;
		double intercept = newSV - grayScaleStart*determinant;

		double[] slopeIntercept = { slope, intercept };

		return slopeIntercept;
	}
	
	public Color getColor(int colorValue) {

		double outputRed = ((double) colorValue)*gradientSlopeRed +gradientInterceptRed;
		double outputGreen = ((double) colorValue)*gradientSlopeGreen +gradientInterceptGreen;
		double outputBlue = ((double) colorValue)*gradientSlopeBlue +gradientInterceptBlue;

		return new Color((int) outputRed, (int) outputGreen, (int) outputBlue);

	}

}