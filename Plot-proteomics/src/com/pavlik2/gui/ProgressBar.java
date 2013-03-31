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

package com.pavlik2.gui;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

/**
 * @author pavel This class is to provide progress bar to provide execution
 *         information
 */

public class ProgressBar extends JFrame {
	private ProgBar progressBar;
	static public int value = 0;

	public void set(int i) {
		// progressBar.setValue(i);
		progressBar.setValue(i);
		value = i;
	}

	public static int get() {
		// progressBar.setValue(i);
		return value;
	}

	/**
	 * Create the panel.
	 */
	public ProgressBar(int num) {

		// initialize(num);

		setLayout(null);
		// create and fill parameters
		progressBar = new ProgBar(0, num);

		setVisible(true);
		setAlwaysOnTop(true);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int X = (screen.width / 2); // Center horizontally.
		int Y = (screen.height / 2); // Center vertically.
		setBounds(X, Y, 500, 100);
		add(progressBar);
	}

}
