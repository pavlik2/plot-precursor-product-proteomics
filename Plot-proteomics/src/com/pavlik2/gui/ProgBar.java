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

import javax.swing.JProgressBar;

public class ProgBar extends JProgressBar {

	/**
	 * 
	 */
	public ProgBar(int i, int num) {
		super(0, num);
		setBounds(0, 0, 500, 100);
		setStringPainted(true);
		setValue(0);
		setVisible(true);
	}

	private static final long serialVersionUID = 1L;

	int val = 0;

	public void reset(int total) {
		val = 0;
		setMaximum(total);
		setValue(0);
	}

	public void reset(long total) {
		val = 0;
		setMaximum((int) total);
		setValue(0);
	}

	public void set(int i) {
		val += i;
		setValue(val);

	}

}
