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

import java.util.ArrayList;

public class Transform extends Thread implements Runnable {

	ArrayList<Integer> arrayTo;
	ArrayList<String> arrayFrom = null;
	String value = null;

	public Transform(ArrayList<Integer> arrayTo, ArrayList<String> arrayFrom) {
		this.arrayTo = arrayTo;
		this.arrayFrom = arrayFrom;
		this.start();
	}

	public Transform(ArrayList<Integer> arrayTo, String arrayFrom) {
		this.arrayTo = arrayTo;
		value = arrayFrom;
		this.start();
	}

	@Override
	public void run() {
		try {
			if (arrayFrom != null)
				for (String t : arrayFrom) {
					arrayTo.add(Math.round((Float.parseFloat(t))));
				}
			else
				arrayTo.add(Math.round((Float.parseFloat(value))));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
