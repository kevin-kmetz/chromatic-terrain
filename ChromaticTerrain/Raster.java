package ChromaticTerrain;

import java.util.Arrays;

public class Raster {

	private int width;
	private int height;

	private int[][] raster;		// A LinkedList of rows of integers.

	public Raster(int width, int height) {

		this.width = width;
		this.height = height;

		raster = new int[height][width];

		for (int y = 0; y < height; y++) {

			for (int x = 0; x < width; x++) {

				raster[y][x] = -1;

			}

		}

	}

	public void set(int x, int y, int value) {

		raster[y][x] = value;

	}

	public int get(int x, int y) {

		return raster[y][x];

	}

	public void display() {

		for (int y = 0; y < height; y++) {

			for (int x = 0; x < width; x++) {

				System.out.print(raster[y][x] + " ");

			}

			System.out.println();

		}

	}

	public void appendTop(int[] row) {

		int[][] updatedRaster = new int[height][width];

		int rowsToAppend = 1;
		int rowsToShiftDown = height - rowsToAppend;

		// Shift rows to preserve down.
		for (int insertionIndex = height - rowsToShiftDown, oldRasterIndex = 0; insertionIndex < height; insertionIndex++, oldRasterIndex++) {
			System.arraycopy(raster[oldRasterIndex], 0, updatedRaster[insertionIndex], 0, width);
		}

		// Insert single new row at the top of the raster.
		System.arraycopy(row, 0, updatedRaster[0], 0, width);

		System.arraycopy(updatedRaster, 0, raster, 0, raster.length);

	}

	public void appendTop(int[][] rows) {

		int[][] updatedRaster = new int[height][width];

		int rowsToAppend = rows.length;
		int rowsToShiftDown = height - rowsToAppend;

		// Shift rows to preserve down.
		for (int insertionIndex = height - rowsToShiftDown, oldRasterIndex = 0; insertionIndex < height; insertionIndex++, oldRasterIndex++) {
			System.arraycopy(raster[oldRasterIndex], 0, updatedRaster[insertionIndex], 0, width);
		}

		// Insert new rows at the top of the raster.
		for (int insertionIndex = 0, rowIndex = 0; rowIndex < rowsToAppend; insertionIndex++, rowIndex++) {
			System.arraycopy(rows[rowIndex], 0, updatedRaster[insertionIndex], 0, width);
		}

		System.arraycopy(updatedRaster, 0, raster, 0, raster.length);

	}

	public void appendBottom(int[] row) {

		int[][] updatedRaster = new int[height][width];

		int rowsToAppend = 1;
		int rowsToShiftUp = height - rowsToAppend;

		// Shift (height - 1) rows to preserve up.
		for (int insertionIndex = 0, oldRasterIndex = rowsToAppend; insertionIndex < rowsToShiftUp; insertionIndex++, oldRasterIndex++) {
			System.arraycopy(raster[oldRasterIndex], 0, updatedRaster[insertionIndex], 0, width);
		}

		// Insert single new row at the bottom of the raster, in its very last row.
		System.arraycopy(row, 0, updatedRaster[updatedRaster.length-1], 0, width);

		System.arraycopy(updatedRaster, 0, raster, 0, raster.length);

	}

	public void appendBottom(int[][] rows) {

		int[][] updatedRaster = new int[height][width];

		int rowsToAppend = rows.length;
		int rowsToShiftUp = height - rowsToAppend;

		// Shift rows to preserve up.
		for (int insertionIndex = 0, oldRasterIndex = rowsToAppend; insertionIndex < rowsToShiftUp; insertionIndex++, oldRasterIndex++) {
			System.arraycopy(raster[oldRasterIndex], 0, updatedRaster[insertionIndex], 0, width);
		}

		// Insert new rows at the bottom.
		for (int insertionIndex = height - rowsToAppend, rowIndex = 0; rowIndex < rowsToAppend; insertionIndex++, rowIndex++) {
			System.arraycopy(rows[rowIndex], 0, updatedRaster[insertionIndex], 0, width);
		}

		System.arraycopy(updatedRaster, 0, raster, 0, raster.length);

	}

	public void appendLeft(int[] column) {

		int[][] updatedRaster = new int[height][width];

		for (int i = 0; i < height; i++) {

			int[] tempRow = concat(column[i], Arrays.copyOf(raster[i], width-1));
			System.arraycopy(tempRow, 0, updatedRaster[i], 0, width);

		}

		System.arraycopy(updatedRaster, 0, raster, 0, raster.length);

	}

	public void appendLeft(int[][] rows) {

		int[][] updatedRaster = new int[height][width];

		int rowLength = rows[0].length;
		int numberOfRows = rows.length;

		for (int i = 0; i < numberOfRows; i++) {

			int[] tempRow = concat(rows[i], Arrays.copyOf(raster[i], width-rowLength));
			System.arraycopy(tempRow, 0, updatedRaster[i], 0, width);

		}

		System.arraycopy(updatedRaster, 0, raster, 0, height);

	}

	public void appendRight(int[] column) {

		int[][] updatedRaster = new int[height][width];

		for (int i = 0; i < height; i++) {

			int[] tempRow = concat(Arrays.copyOfRange(raster[i], 1, width), column[i]);
			System.arraycopy(tempRow, 0, updatedRaster[i], 0, width);

		}

		System.arraycopy(updatedRaster, 0, raster, 0, raster.length);

	}

	public void appendRight(int[][] rows) {

		int[][] updatedRaster = new int[height][width];

		int rowLength = rows[0].length;
		int numberOfRows = rows.length;

		for (int i = 0; i < numberOfRows; i++) {

			int[] tempRow = concat(Arrays.copyOfRange(raster[i], rowLength, width), rows[i]);
			System.arraycopy(tempRow, 0, updatedRaster[i], 0, width);

		}

		System.arraycopy(updatedRaster, 0, raster, 0, height);

	}

	private int[] concat(int[] firstArray, int[] secondArray) {

		int[] mergedArray = new int[firstArray.length + secondArray.length];

		System.arraycopy(firstArray, 0, mergedArray, 0, firstArray.length);
		System.arraycopy(secondArray, 0, mergedArray, firstArray.length, secondArray.length);

		return mergedArray;

	}

	private int[] concat(int value, int[] array) {

		int[] mergedArray = new int[1 + array.length];

		mergedArray[0] = value;

		System.arraycopy(array, 0, mergedArray, 1, array.length);

		return mergedArray;

	}

	private int[] concat(int[] array, int value) {

		int[] mergedArray = new int[array.length + 1];

		mergedArray[mergedArray.length-1] = value;

		System.arraycopy(array, 0, mergedArray, 0, array.length);

		return mergedArray;

	}

}