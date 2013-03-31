package com.pavlik2.processing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;

import javax.swing.JOptionPane;

import com.pavlik2.gui.GUI;
import com.pavlik2.gui.ProgBar;
import com.pavlik2.image.ImageProcessing;

public class ProcessingLowMemory {
	ProgBar progress;
	private GUI gui;
	private File file;
	private int maxPrecursor;
	private int maxProduct;
	String[] rowDescription = null;

	public ProcessingLowMemory(File n, GUI gui) {
		file = n;
		this.gui = gui;
		progress = gui.progress;
	}

	public void processLowMemory(boolean write, boolean display, int resolution)
			throws Exception {
		// find max precursor/product
		System.out.println("You selected file:" + file.getName());
		long lengthOfFile = file.length();
		progress.reset(lengthOfFile);

		BufferedReader reader = new BufferedReader(new FileReader(file));

		int countLines = 0;
		String cutter = "\t";
		boolean firstLine = true;

		int length = 0;
		String lineT;
		System.out.println("Reading first pass...");
		while ((lineT = reader.readLine()) != null) {

			final int count = countLines;
			final String line = lineT;

			length += lineT.length() + 1;
			String[] lines = line.split(cutter);
			if (lines.length < 2) {
				cutter = ",";
				lines = line.split(cutter);
			}
			if (lines.length == 2)
				if (firstLine) {
					if (Processing.compareToprecursor(lines))
						rowDescription = lines;
					firstLine = false;
				}

				else {
					int valuePrecursor = (Math.round((Float
							.parseFloat(lines[0]))));
					int valueProduct = (Math
							.round((Float.parseFloat(lines[1]))));
					if (valuePrecursor > maxPrecursor)
						maxPrecursor = valuePrecursor;
					if (valueProduct > maxProduct)
						maxProduct = valueProduct;

				}
			else {
				JOptionPane
						.showMessageDialog(
								null,
								"File must be 2 collumn CSV with decimal values and comma or tab separator",
								"Invalid file", 1);
				throw new Exception("Invalid file type");

			}
			if (count % 1000000 == 0) {
				gui.updateProgress(length);
				length = 0;

			}
			countLines++;
		}
		System.out.println("Done first pass...");
		gui.updateProgress(length);
		progress.reset(lengthOfFile);
		reader.close();
		length = 0;
		reader = new BufferedReader(new FileReader(file));

		double scaling = Math.min(maxPrecursor, maxProduct) / resolution;
		if (resolution == 0)
			scaling = 1.0;
		int HEIGHT = (int) (maxProduct / scaling) + 1;
		int WIDTH = (int) (maxPrecursor / scaling) + 1;
		int rows = 150;
		BufferedImage img = new BufferedImage(WIDTH + rows, HEIGHT + rows - 50,
				BufferedImage.TYPE_INT_RGB);
		System.out.println("Start second pass...");
		firstLine = true;
		while ((lineT = reader.readLine()) != null) {
			final int count = countLines;
			final String line = lineT;

			length += lineT.length() + 1;
			String[] lines = line.split(cutter);
			if (lines.length < 2) {
				cutter = ",";
				lines = line.split(cutter);
			}

			if (firstLine) {

				firstLine = false;
			}

			else {

				int valuePrecursor = (Math.round((Float.parseFloat(lines[0]))));
				int valueProduct = (Math.round((Float.parseFloat(lines[1]))));

				Graphics2D g = (Graphics2D) img.getGraphics();
				// g.setBackground(Color.white);

				int newValuePrecursor = (int) (valuePrecursor / scaling) + rows;

				int newValueProduct = HEIGHT - (int) (valueProduct / scaling);

				int color = img.getRGB(newValuePrecursor, newValueProduct);
				color += 1;
				g.setColor(new Color(color));

				g.fillRect(newValuePrecursor, newValueProduct, 1, 1);

			}

			if (count % 1000000 == 0) {
				gui.updateProgress(length);
				length = 0;
				// System.out.println("Processed " + count + " lines");

			}

			countLines++;
		}
		reader.close();
		System.gc();
		System.out.println("Done second pass...");
		// ---------------------------------

		if (write) {
			System.out.println("Writing output file...");
			File f = new File("output_resolution.csv");

			Writer w = new FileWriter(f);

			BufferedWriter writer = new BufferedWriter(w);
			progress.reset(HEIGHT);
			// --------------------------------------------------
			for (int i = 0; i < HEIGHT; i++) {
				for (int j = 0; j < WIDTH; j++)

					if (j != WIDTH - 1)
						writer.write(img.getRGB(i, j) + ",");
					else
						writer.write(img.getRGB(i, j) + "\n");
				gui.setProgress(i);
			}
			System.out.println("Done output file...");
			writer.close();
			// -------------------------------------------
		}

		if (display) {
			System.out.println("Displaying and saving image...");
			if (rowDescription != null)
				ImageProcessing.drawScreen(img, rows, scaling, WIDTH, HEIGHT,
						rowDescription[0], rowDescription[1]);
			else
				ImageProcessing.drawScreen(img, rows, scaling, WIDTH, HEIGHT,
						null, null);
			System.out.println("Done displaying and saving image...");
		}
	}

}
