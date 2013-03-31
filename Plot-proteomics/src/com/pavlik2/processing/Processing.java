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

package com.pavlik2.processing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.pavlik2.gui.GUI;
import com.pavlik2.gui.ProgBar;

public class Processing {
	File file;
	public int[][] arrayToDisplay = null;

	private ArrayList<int[]> product = new ArrayList<>();
	private ArrayList<int[]> precursor = new ArrayList<>();

	int maxPrecursor = 0;
	int maxProduct = 0;
	public GUI gui;
	ProgBar progress;
	public String[] rowDescription = null;

	public Processing(File n, GUI gui) {
		file = n;
		this.gui = gui;
		progress = gui.progress;
	}

	public void readANDtransform() throws Exception {

		String lineT;

		System.out.println("You selected file:" + file.getName());
		progress.reset(file.length());

		BufferedReader reader = new BufferedReader(new FileReader(file));

		int countLines = 0;
		String cutter = "\t";
		boolean firstLine = true;
		int[] precursor = new int[1000];
		int[] product = new int[1000];
		int countVariables = 0;
		int length = 0;
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
					if (!lines[0].equals("precursor_mz")
							&& !lines[1].equals("product_mz"))
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

					if (countVariables == 1000) {
						this.product.add(product);
						this.precursor.add(precursor);
						countVariables = 0;
						precursor = new int[1000];
						product = new int[1000];

						precursor[countVariables] = valuePrecursor;

						product[countVariables] = valueProduct;

					} else {
						precursor[countVariables] = valuePrecursor;
						product[countVariables] = valueProduct;
					}

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
				System.out.println("Processed " + count + " lines");

			}

			countVariables++;
			countLines++;
		}
		gui.updateProgress(length);
		int[] array = new int[countVariables];
		int[] array2 = new int[countVariables];

		for (int i = 0; i < countVariables; i++) {
			array[i] = product[i];
			array2[i] = precursor[i];

		}
		this.product.add(array);
		this.precursor.add(array2);

		product = null;
		precursor = null;
		System.gc();
		reader.close();
		System.out.println("Awaiting termination");
		// while (Transform.activeCount()>0) Thread.sleep(1000);

		setArray();
	}

	private void setArray() {
		arrayToDisplay = new int[++maxPrecursor][++maxProduct];

		int length = Math.min(precursor.size(), product.size());
		progress.reset(length);
		for (int i = 0; i < length; i++) {

			int[] precursor = this.precursor.get(i);
			int[] product = this.product.get(i);

			for (int k = 0; k < precursor.length; k++) {

				arrayToDisplay[precursor[k]][product[k]]++;

			}
			gui.updateProgress(i + 1);
		}

	}

	public void writeTofile(String name) throws IOException {
		System.out.println("Writing to file...");
		File f = new File(name);

		Writer w = new FileWriter(f);

		BufferedWriter writer = new BufferedWriter(w);

		for (int i = 0; i < arrayToDisplay.length; i++) {
			int jLength = arrayToDisplay[i].length;
			for (int j = 0; j < jLength; j++) {

				if (j != jLength - 1)
					writer.write(arrayToDisplay[i][j] + ",");
				else
					writer.write(arrayToDisplay[i][j] + "\n");
			}
		}

		writer.close();

	}

	public static void writeTofileCSV(String name, int[][] arrayToDisplay)
			throws IOException {
		System.out.println("Writing to file...");
		File f = new File(name);

		Writer w = new FileWriter(f);

		BufferedWriter writer = new BufferedWriter(w);

		for (int i = 0; i < arrayToDisplay.length; i++) {
			int jLength = arrayToDisplay[i].length;
			for (int j = 0; j < jLength; j++) {

				if (j != jLength - 1)
					writer.write(arrayToDisplay[i][j] + ",");
				else
					writer.write(arrayToDisplay[i][j] + "\n");
			}
		}

		writer.close();

	}

}
