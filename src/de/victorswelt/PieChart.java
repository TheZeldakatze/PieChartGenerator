package de.victorswelt;

import java.awt.Component;

import javax.swing.JOptionPane;

public class PieChart {

	public static void main(String[] args) {
		new PieChart();
	}
	
	public PieChart() {
		new SettingsWindow(this);
	}
	
	public void requestQuit(Component parent) {
		int i = JOptionPane.showConfirmDialog(parent, "Do you really want to close the pie chart generator? Any work that was not saved will be lost!", "Really quit?", JOptionPane.YES_NO_OPTION);
		if(i == JOptionPane.YES_OPTION)
			System.exit(0);
	}
}
