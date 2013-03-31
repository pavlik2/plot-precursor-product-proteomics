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

package com.pavlik2.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import javax.swing.SwingUtilities;

import com.pavlik2.gui.ProgressBar;

public class Rewrite {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String name = "trans_contour_plot2.txt";
		File f2 = new File(name);
		BufferedReader reader = new BufferedReader(new FileReader(f2));
		System.out.println("Writing to file...");
		File f = new File("trans_contour2.txt");

		Writer w = new FileWriter(f);
		final ProgressBar progress = new ProgressBar((int) f2.length());
		progress.show();
		BufferedWriter writer = new BufferedWriter(w);
		String lineT;
		int length = 0;
		while ((lineT = reader.readLine()) != null) {
			if (length > f2.length() / 20) {
				w.close();
				reader.close();
				f = null;
				f2 = null;
				progress.removeAll();

				progress.setVisible(false);
				break;

			}
			length += lineT.length() + 1;
			final int length2 = length;
			writer.write(lineT + "\n");
			if (length2 % 1000000 == 0)
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						progress.set(length2);
					}
				});

		}
		System.out.println("Done...");
		System.exit(0);
	}

}
