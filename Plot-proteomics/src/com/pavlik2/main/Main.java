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

import com.pavlik2.gui.GUI;

public class Main {

	static GUI gui;

	public static void main(String[] args) {

		{
			gui = new GUI();
			while (true)
				try {

					while (gui.next)
						Thread.sleep(1000);
					gui.action();
					// Processing t = new Processing(new File(
					// "trans_contour_plot2.txt"), gui);
					gui.next = true;
					// t.readANDtransform();
				} catch (Exception e) {

					e.printStackTrace();
				}
		}
	}

}
