package ChromaticTerrain;

import java.awt.*;
import java.awt.event.*;

import java.util.Random;

public class ChromaticTerrainViewer {

	private Frame frame = new Frame("Chromatic Terrain Viewer");
	private ImageCanvas canvas = new ImageCanvas();

	public static void main(String[] args) {

		ChromaticTerrainViewer viewer = new ChromaticTerrainViewer();

	}

	ChromaticTerrainViewer() {

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