/*    plot-precursor-product-proteomics
    Plotting program for 2 column CSV file
    Copyright (C) <2013>  <Pavel Kartashev>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.pavlik2.image;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.ScrollPane;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.pavlik2.processing.Processing;

public class ImageProcessing {

	public static void displayImage(int[][] arrayToDisplay, boolean write,
			String[] rowDescr) throws IOException {
		double scaling = 1.0;

		if (rowDescr != null)
			displayImage(arrayToDisplay, scaling, write, rowDescr[0],
					rowDescr[1]);
		else
			displayImage(arrayToDisplay, scaling, write, null, null);
	}

	public static void displayImage(int[][] arrayToDisplay, int resolution,
			boolean write, String[] rowDescr) throws IOException {
		double scaling = Math.min(arrayToDisplay.length,
				arrayToDisplay[0].length) / resolution;

		if (rowDescr != null)
			displayImage(arrayToDisplay, scaling, write, rowDescr[0],
					rowDescr[1]);
		else
			displayImage(arrayToDisplay, scaling, write, null, null);
	}

	public static void displayImage(int[][] arrayToDisplay, double scaling,
			boolean write, String xDef, String yDef) throws IOException {

		int dataXlength = arrayToDisplay.length;

		int dataYlength = arrayToDisplay[0].length;

		int HEIGHT = (int) (dataYlength / scaling) + 1;
		int WIDTH = (int) (dataXlength / scaling) + 1;

		int rows = 150;

		final BufferedImage img = new BufferedImage(WIDTH + rows, HEIGHT + rows
				- 50, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) img.getGraphics();

		g.setBackground(Color.white);

		if (scaling == 1.0)
			for (int i = 0; i < dataXlength; i++) {
				for (int j = 0; j < dataYlength; j++) {

					g.setColor(new Color(arrayToDisplay[i][j]));

					g.fillRect(i + rows, HEIGHT - j, 1, 1);

				}
				// System.out.println("Processing " + i);
			}
		else {
			int colorMap[][] = new int[WIDTH][HEIGHT];

			for (int i = 0; i < dataXlength; i++) {
				for (int j = 0; j < dataYlength; j++) {

					colorMap[(int) (i / scaling)][(int) (j / scaling)] += arrayToDisplay[i][j];

				}

			}

			if (write)
				Processing.writeTofileCSV("output_resolution.csv", colorMap);

			for (int i = 0; i < WIDTH; i++) {
				for (int j = 0; j < HEIGHT; j++) {
					g.setColor(new Color(colorMap[i][j]));

					g.fillRect(i + rows, HEIGHT - j, 1, 1);
				}
			}
		}

		drawScreen(img, rows, scaling, WIDTH, HEIGHT, xDef, yDef);

	}

	public static void drawScreen(final BufferedImage img, int rows,
			double scaling, int WIDTH, int HEIGHT, String xDef, String yDef)
			throws IOException {

		Graphics2D g = (Graphics2D) img.getGraphics();

		g.setColor(Color.WHITE);
		int fontSize = (int) Math.log(WIDTH) * 3;

		g.setFont(new Font("Arial", Font.BOLD, fontSize));
		String precursor = "Precursor m/z";
		if (xDef != null)
			precursor = xDef;
		g.drawString(precursor, img.getWidth() / 2, img.getHeight() - 10);

		int xStart = rows - 2;

		int yStart = HEIGHT + 2;

		// draw x axis
		for (int i = xStart; i < WIDTH + rows; i++) {
			g.fillRect(i, yStart, 2, 2);
		}
		// draw y axis
		for (int i = 0; i < yStart; i++) {
			g.fillRect(xStart, i, 2, 2);
		}

		// dividers

		int dividerX = WIDTH / 10;

		int dividerY = HEIGHT / 10;
		// ------------------------------------------
		for (int x = xStart; x <= WIDTH + rows; x += dividerX) {
			for (int y = yStart; y < yStart + 15; y++) {
				g.fillRect(x, y, 2, 2);

			}

			String mzValue = Integer.toString((int) ((x - xStart) * scaling));
			if (!mzValue.equals("0"))
				g.drawString(mzValue, x - (mzValue.length() - 1)
						* g.getFont().getSize(), yStart + 50);
		}

		// ----------------------------
		for (int y = yStart; y >= 0; y -= dividerY) {
			for (int x = xStart - 15; x < xStart; x++) {
				g.fillRect(x, y, 2, 2);
			}
			String mzValue = Integer.toString((int) ((yStart - y) * scaling));
			g.drawString(mzValue, xStart - 25 - (mzValue.length() - 1)
					* g.getFont().getSize(), y + 25);
		}

		int initialHeigth = HEIGHT / 2;
		String yAxis = "Product m/z";
		if (yDef != null)
			yAxis = yDef;

		for (String t : yAxis.split("(?!^)")) {

			g.drawString(t, 0, initialHeigth);
			initialHeigth += g.getFont().getSize();
		}
		JFrame frame = new JFrame("(c) Pavel Kartashev pavlik2@gmail.com");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ScrollPane t = new ScrollPane();
		final int h = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
		final int w = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
		JPanel panel = new JPanel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 3391674581630740321L;

			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;
				g2d.clearRect(0, 0, getWidth(), getHeight());

				g2d.drawImage(img, 0, 0, this);
			}
		};

		panel.setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));

		t.setSize(w, h);
		t.add(panel);
		frame.getContentPane().add(t);
		frame.pack();
		frame.setVisible(true);
		String file = "output.png";
		ImageIO.write(img, "png", new File(file));
		System.out.println("Image saved to " + file);

	}

}
