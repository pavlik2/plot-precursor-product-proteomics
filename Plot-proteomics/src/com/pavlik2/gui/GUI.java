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

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

import com.pavlik2.image.ImageProcessing;
import com.pavlik2.processing.Processing;
import com.pavlik2.processing.ProcessingLowMemory;

/**
 * @author pavel
 * 
 */
public class GUI {
	public GUI() {
		setTextArea();
		progress.setVisible(true);
		display.setSelected(true);
		// display.setEnabled(false);
	}

	public File choose() {

		// Create a file chooser
		final JFileChooser fc = new JFileChooser();
		fc.addChoosableFileFilter(new FileFilter() {

			@Override
			public String getDescription() {

				return "*.csv";
			}

			@Override
			public boolean accept(File arg0) {
				if (arg0.isDirectory()
						|| arg0.getAbsolutePath().endsWith(".csv"))
					return true;
				else
					return false;
			}
		});

		// In response to a button click:
		int returnVal = fc.showOpenDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION)

			return fc.getSelectedFile();
		else
			return null;

	}

	TextArea textArea = new TextArea();
	public ProgBar progress = new ProgBar(0, 100);

	public void updateProgress(final int text) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				progress.set(text);
			}
		});
	}

	public void setProgress(final int value) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				progress.setValue(value);
			}
		});
	}

	private void updateTextArea(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				textArea.append(text);
			}
		});
	}

	private void redirectSystemStreams() {
		OutputStream out = new OutputStream() {
			@Override
			public void write(int b) throws IOException {
				updateTextArea(String.valueOf((char) b));
			}

			@Override
			public void write(byte[] b, int off, int len) throws IOException {
				updateTextArea(new String(b, off, len));
			}

			@Override
			public void write(byte[] b) throws IOException {
				write(b, 0, b.length);
			}
		};

		System.setOut(new PrintStream(out, true));
		System.setErr(new PrintStream(out, true));
	}

	public JCheckBox writeFile = new JCheckBox("Write output file");

	public JCheckBox display = new JCheckBox("Display image");
	public JComboBox<String> choice = new JComboBox<String>();
	public JButton button = new JButton("Start Computation");

	private void processLowMemory(File file) {
		long l1 = System.currentTimeMillis();

		if (file != null)
			try {
				ProcessingLowMemory t = new ProcessingLowMemory(file,
						(GUI) GUI.this);

				if (display.isSelected()) {
					switch (choice.getSelectedIndex()) {
					case 0:
						t.processLowMemory(writeFile.isSelected(),
								display.isSelected(), 512);
						break;
					case 1:
						t.processLowMemory(writeFile.isSelected(),
								display.isSelected(), 1024);
						break;
					case 2:
						t.processLowMemory(writeFile.isSelected(),
								display.isSelected(), 2048);
						break;
					default:
						t.processLowMemory(writeFile.isSelected(),
								display.isSelected(), 0);
						break;
					}
				}
			}

			catch (Exception e) {
				e.printStackTrace();
			}
		System.out.println("Terminated");
		long l2 = System.currentTimeMillis();
		System.out.println("Time executed: " + (l2 - l1) + "ms");
	}

	private void processFullMemory(File file) {
		long l1 = System.currentTimeMillis();

		if (file != null)
			try {
				Processing t = new Processing(file, (GUI) GUI.this);

				t.readANDtransform();
				if (writeFile.isSelected())
					t.writeTofile("output.csv");

				if (display.isSelected()) {
					switch (choice.getSelectedIndex()) {
					case 0:
						ImageProcessing.displayImage(t.arrayToDisplay, 512,
								writeFile.isSelected(), t.rowDescription);
						break;
					case 1:
						ImageProcessing.displayImage(t.arrayToDisplay, 1024,
								writeFile.isSelected(), t.rowDescription);
						break;
					case 2:
						ImageProcessing.displayImage(t.arrayToDisplay, 2048,
								writeFile.isSelected(), t.rowDescription);
						break;
					default:
						ImageProcessing.displayImage(t.arrayToDisplay,
								writeFile.isSelected(), t.rowDescription);
						break;
					}
				}
			}

			catch (Exception e) {
				e.printStackTrace();
			}
		System.out.println("Terminated");
		long l2 = System.currentTimeMillis();
		System.out.println("Time executed: " + (l2 - l1) + "ms");
	}

	public void action() {
		if (display.isSelected() || writeFile.isSelected()) {
			File file = choose();
			// if (Runtime.getRuntime().maxMemory() < file.length() * 1.5)
			processLowMemory(file);
			// else

			// processFullMemory(file);

		} else
			JOptionPane.showMessageDialog(button,
					"Select one of following: display image or write file",
					"Invalid selection", 1);
	}

	public boolean next = true;

	void setTextArea() {
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						next = false;
					}
				});

			};

		});
		String[] values = { "Output resolution 500 px",
				"Output resolution 1 kpx", "Output resolution 2 kpx",
				"Output resolution native" };
		choice = new JComboBox<String>(values);

		JPanel panel = new JPanel(new FlowLayout());

		panel.add(writeFile);
		panel.add(display);
		panel.add(choice);
		panel.add(button);
		JFrame frame = new JFrame("(c) Pavel Kartashev - pavlik2@gmail.com");

		int h = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
		int w = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.getContentPane().setLayout(new GridLayout(3, 1));
		frame.getContentPane().add(panel);
		frame.getContentPane().add(textArea);
		progress.setVisible(true);
		frame.getContentPane().add(progress);
		frame.pack();
		frame.setSize(w / 2, h / 2);
		frame.setVisible(true);
		redirectSystemStreams();
	}
}
